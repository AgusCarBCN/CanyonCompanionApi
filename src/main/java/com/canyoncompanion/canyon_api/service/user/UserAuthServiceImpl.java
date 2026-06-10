package com.canyoncompanion.canyon_api.service.user;


import com.canyoncompanion.canyon_api.dtos.requests.*;
import com.canyoncompanion.canyon_api.dtos.responses.*;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.RefreshToken;
import com.canyoncompanion.canyon_api.model.entities.RoleEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import com.canyoncompanion.canyon_api.repository.RoleRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.security.JwtService;
import com.canyoncompanion.canyon_api.security.RefreshTokenService;
import com.canyoncompanion.canyon_api.service.EmailService;
import com.canyoncompanion.canyon_api.service.validationtokens.TokenValidationServiceImpl;
import com.canyoncompanion.canyon_api.util.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenValidationServiceImpl tokenService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // =====================================================
    // FORGOT PASSWORD - (solo genera token)
    // =====================================================
    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        // Siempre responder igual por seguridad
        if (user == null || user.getStatus() != UserStatus.ACTIVE) {

            return ForgotPasswordResponse.builder().message("If the email exists, you will receive a password reset link.").build();
        }

        // Generar token específico para reset password
        String resetToken = tokenService.generateValidationToken(request.getEmail());
        user.setResetToken(resetToken);
        user.setStatusDescription("Password reset requested, pending token validation");
        userRepository.save(user);

        // Enviar email
        emailService.sendForgotPasswordEmail(
                user.getEmail(),
                resetToken
        );

        return ForgotPasswordResponse.builder().message("The email has been sent.").build();
    }

    // =====================================================
    // REGISTER
    // =====================================================
    @Override
    public RegisterResponse registerUser(UserRequestDTO request) {

        UserEntity existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (existingUser != null) {
            if (existingUser.getStatus().equals(UserStatus.ACTIVE)) {
                throw new BusinessException(
                        "USER_ALREADY_EXISTS AND ACTIVE",
                        "User already exists with email",
                        HttpStatus.CONFLICT
                );
            } else {
                String verificationToken = tokenService.generateValidationToken(request.getEmail());
                existingUser.setVerificationToken(verificationToken);
                userRepository.save(existingUser);
                emailService.sendVerificationEmail(existingUser.getEmail(), verificationToken);
                return RegisterResponse.builder().message("Verification Email resent. Check your inbox").build();
            }
        }
        //Nuevo usuario

        UserEntity user = userMapper.toUserEntity(request);
        String verificationToken = tokenService.generateValidationToken(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(buildRoles(false));
        user.setVerificationToken(verificationToken);
        user.setStatus(UserStatus.PENDING_VERIFICATION);
        user.setStatusDescription("User registered but pending email verification");
        userRepository.save(user);
        //Send email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        return RegisterResponse.builder().message("Verification is successful").build();

    }

    @Override
    public ResetPasswordResponse newPassword(ResetPasswordRequest request) {
        String email = tokenService.extractEmail(request.getToken());

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND.name(),
                        ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        if (user.getResetToken() == null) {
            throw new BusinessException(
                    ErrorCode.EMPTY_TOKEN.name(),
                    ErrorCode.EMPTY_TOKEN.getDefaultMessage(),
                    HttpStatus.NOT_FOUND
            );
        }

        if (!user.getResetToken().equals(request.getToken())) {
            throw new BusinessException(
                    ErrorCode.INVALID_TOKEN.name(),
                    ErrorCode.INVALID_TOKEN.getDefaultMessage(),
                    HttpStatus.FORBIDDEN
            );
        }
        if (!tokenService.validateToken(request.getToken())) {
            throw new BusinessException(
                    ErrorCode.EXPIRED_TOKEN.name(),
                    ErrorCode.EXPIRED_TOKEN.getDefaultMessage(),
                    HttpStatus.GONE
            );

        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword())); // 🔥 AQUÍ
        user.setResetToken(null);
        user.setStatusDescription("Password updated");
        userRepository.save(user);
        return ResetPasswordResponse.builder().message("Password updated successfully!").build();
    }


    // =====================================================
    // LOGIN
    // =====================================================
    @Override
    public AuthResponse login(AuthRequestDTO request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND.name(),
                        ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        if (user.getStatus().equals(UserStatus.PENDING_VERIFICATION)) {
            throw new BusinessException(
                    ErrorCode.USER_VERIFICATION_PENDING.name(),
                    ErrorCode.USER_VERIFICATION_PENDING.getDefaultMessage(),
                    HttpStatus.FORBIDDEN
            );
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BusinessException(
                    ErrorCode.INVALID_CREDENTIALS_PASSWORD.name(),
                    ErrorCode.INVALID_CREDENTIALS_PASSWORD.getDefaultMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }

        return generateAuthSession(user);


    }

    // =====================================================
    // REFRESH TOKEN
    // =====================================================
    @Override
    public AuthResponse refreshToken(TokenRequestDTO request) {

        RefreshToken oldToken =
                refreshTokenService.findByToken(request.getToken());

        assert oldToken != null;
        UserEntity user = oldToken.getUser();
        //Generate RefreshToken
        RefreshToken refreshToken=refreshTokenService.rotateToken(oldToken);
        String newRefreshToken = refreshToken.getToken();

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        //Generate Access token
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .username(userDetails.getUsername())
                .status(user.getStatus())
                .build();
    }

    // =====================================================
    // LOGOUT
    // =====================================================
    @Override
    @Transactional
    public void logout(TokenRequestDTO request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getToken());
        refreshTokenService.deleteToken(refreshToken);

    }

    // =====================================================
    // ME
    // =====================================================
    @Override
    public UserResponseDTO me(Authentication authentication) {

        String email = authentication.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND.name(),
                        ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        return UserResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                //.surname(user.getSurname())
                .build();
    }

    @Override
    public VerificationEmailResponse VerificationEmail(String verificationToken) {
        String emailString = tokenService.extractEmail(verificationToken);
        UserEntity user = userRepository.findByEmail(emailString).orElseThrow();
        if (user == null) {
            throw new BusinessException(
                    ErrorCode.USER_NOT_FOUND.name(),
                    ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                    HttpStatus.NOT_FOUND
            );

        }
        if (user.getVerificationToken() == null) {
            throw new BusinessException(
                    ErrorCode.EMPTY_TOKEN.name(),
                    ErrorCode.EMPTY_TOKEN.getDefaultMessage(),
                    HttpStatus.NOT_FOUND
            );
        }

        if (!user.getVerificationToken().equals(verificationToken)) {
            throw new BusinessException(
                    ErrorCode.INVALID_TOKEN.name(),
                    ErrorCode.INVALID_TOKEN.getDefaultMessage(),
                    HttpStatus.FORBIDDEN
            );

        }
        if (!tokenService.validateToken(verificationToken)) {
            throw new BusinessException(
                    ErrorCode.EXPIRED_TOKEN.name(),
                    ErrorCode.EXPIRED_TOKEN.getDefaultMessage(),
                    HttpStatus.GONE
            );

        }
        user.setVerificationToken(null);
        user.setStatus(UserStatus.ACTIVE);
        user.setStatusDescription("User email verified and account activated");
        userRepository.save(user);
        return VerificationEmailResponse.builder().message("Email verified successfully!").build();
    }

    @Override
    public VerificationEmailResponse VerificationEmailForgotPassword(String resetToken) {

        // 1. Validar JWT primero (firma + expiración)
        String email = tokenService.extractEmail(resetToken);

        if (!tokenService.validateToken(resetToken)) {
            throw new BusinessException(
                    ErrorCode.EXPIRED_TOKEN.name(),
                    ErrorCode.EXPIRED_TOKEN.getDefaultMessage(),
                    HttpStatus.GONE
            );
        }

        // 2. Buscar usuario
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND.name(),
                        ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        // 3. Token vacío
        if (user.getResetToken() == null) {
            throw new BusinessException(
                    ErrorCode.EMPTY_TOKEN.name(),
                    ErrorCode.EMPTY_TOKEN.getDefaultMessage(),
                    HttpStatus.NOT_FOUND
            );
        }

        // 4. Token mismatch
        if (!user.getResetToken().equals(resetToken)) {
            throw new BusinessException(
                    ErrorCode.INVALID_TOKEN.name(),
                    ErrorCode.INVALID_TOKEN.getDefaultMessage(),
                    HttpStatus.FORBIDDEN
            );
        }

        // 5. OK
        user.setStatus(UserStatus.ACTIVE);
        user.setStatusDescription("User email verified to reset password");
        userRepository.save(user);

        return VerificationEmailResponse.builder()
                .message("Email verified successfully!")
                .build();
    }


    @Override
    public boolean confirmUserAccount(String verificationToken) {
        return false;
    }

    // =====================================================
    // CORE SESSION CREATION (ÚNICO PUNTO DE TOKENS)
    // =====================================================
    private AuthResponse generateAuthSession(UserEntity user) {

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String accessToken = jwtService.generateAccessToken(userDetails);

        String refreshToken =
                refreshTokenService.createToken(user).getToken();

        return buildAuthResponse(userDetails, accessToken, refreshToken, user.getStatus());
    }

    private AuthResponse buildAuthResponse(
            UserDetails userDetails,
            String accessToken,
            String refreshToken,
            UserStatus status
    ) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(userDetails.getUsername())
                .status(status)
                .build();
    }

    private Set<RoleEntity> buildRoles(boolean isAdmin) {

        Set<RoleEntity> roles = new HashSet<>();

        RoleEntity userRole = roleRepository.findByRole(Roles.ROLE_USER)
                .orElseThrow();

        roles.add(userRole);

        if (isAdmin) {
            RoleEntity adminRole = roleRepository.findByRole(Roles.ROLE_ADMIN)
                    .orElseThrow();
            roles.add(adminRole);
        }

        return roles;
    }

}