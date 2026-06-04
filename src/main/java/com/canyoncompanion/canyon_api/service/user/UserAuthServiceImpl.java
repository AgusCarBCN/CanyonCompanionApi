package com.canyoncompanion.canyon_api.service.user;


import com.canyoncompanion.canyon_api.dtos.requests.AuthRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.VerificationEmailResponse;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import com.canyoncompanion.canyon_api.model.entities.RoleEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import com.canyoncompanion.canyon_api.repository.RefreshTokenRepository;
import com.canyoncompanion.canyon_api.repository.RoleRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.security.JwtService;
import com.canyoncompanion.canyon_api.service.EmailService;
import com.canyoncompanion.canyon_api.service.TokenService;
import com.canyoncompanion.canyon_api.service.TokenServiceImpl;
import com.canyoncompanion.canyon_api.util.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenServiceImpl tokenService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // =====================================================
    // REGISTER
    // =====================================================
    @Override
    public String registerUser(UserRequestDTO request) {

        UserEntity existingUser=userRepository.findByEmail(request.getEmail()).orElse(null);
        if (existingUser!=null) {
            if(existingUser.getStatus().equals(UserStatus.ACTIVE)) {
                throw new BusinessException(
                        "USER_ALREADY_EXISTS AND ACTIVE",
                        "User already exists with email",
                        HttpStatus.CONFLICT
                );
            }else{
                String verificationToken = tokenService.generateValidationToken(request.getEmail());
                existingUser.setVerificationToken(verificationToken);
                userRepository.save(existingUser);
                emailService.sendVerificationEmail(existingUser.getEmail(), verificationToken);
                return "Verification Email resent. Check your inbox";
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

        return "Verification is successful";
    }

    // =====================================================
    // LOGIN
    // =====================================================
    @Override
    public AuthResponse login(AuthRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND.name(),
                        ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        return generateAuthSession(user);
    }

    // =====================================================
    // REFRESH TOKEN
    // =====================================================
    @Override
    public AuthResponse refreshToken(TokenRequestDTO request) {

        RefreshTokenEntity oldToken =
                tokenService.findByToken(request.getToken());

        UserEntity user = oldToken.getUser();

        String newRefreshToken = tokenService.rotateToken(oldToken);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String newAccessToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .username(userDetails.getUsername())
                /*.roles(userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())*/
                .build();
    }

    // =====================================================
    // LOGOUT
    // =====================================================
    @Override
    @Transactional
    public void logout(TokenRequestDTO request) {

        refreshTokenRepository.deleteByToken(request.getToken());

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
        if (user == null || user.getVerificationToken() == null) {
            return VerificationEmailResponse.builder().message("Token Expired!").build();
        }
        if (!tokenService.validateToken(verificationToken) || !user.getVerificationToken().equals(verificationToken)) {
            return VerificationEmailResponse.builder().message("Token Expired!").build();
        }
        user.setVerificationToken(null);
        user.setStatus(UserStatus.ACTIVE);
        user.setStatusDescription("User email verified and account activated");
        userRepository.save(user);
        return VerificationEmailResponse.builder().message("Email verified successfully!").build();
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

        String accessToken = jwtService.generateToken(userDetails);

        String refreshToken =
                tokenService.generateLoginToken(userDetails);

        return buildAuthResponse(userDetails, accessToken, refreshToken);
    }

    private AuthResponse buildAuthResponse(
            UserDetails userDetails,
            String accessToken,
            String refreshToken
    ) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(userDetails.getUsername())
                /*.roles(userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())*/
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