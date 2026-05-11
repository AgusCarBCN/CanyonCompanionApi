package com.canyoncompanion.canyon_api.service.user;


import com.canyoncompanion.canyon_api.dtos.requests.AuthRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.model.entities.RoleEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.repository.RoleRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.security.JwtService;
import com.canyoncompanion.canyon_api.service.RefreshTokenService;
import com.canyoncompanion.canyon_api.util.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    // =====================================================
    // REGISTER USER
    // =====================================================
    @Override
    public AuthResponse registerUser(UserRequestDTO request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BusinessException(
                    "USER_ALREADY_EXISTS",
                    "User already exists with email",
                    HttpStatus.CONFLICT
            );
        }

        UserEntity user = userMapper.toUserEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<RoleEntity> roles = buildRoles(false);
        user.setRoles(roles);

        userRepository.save(user);

        return authenticateAndGenerateTokens(request.getEmail(), request.getPassword());
    }

    // =====================================================
    // REGISTER ADMIN
    // =====================================================
    @Override
    public AuthResponse registerAdminUser(UserRequestDTO request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BusinessException(
                    "USER_ALREADY_EXISTS",
                    "User already exists with email",
                    HttpStatus.CONFLICT
            );
        }
        log.info("REGISTER USER START - email: {}", request.getEmail());
        try {

            UserEntity user = userMapper.toUserEntity(request);
            log.info("MAPPER OK");
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            //user.addRolesToUser(true);
            // 🔥 AQUÍ USAS TU MÉTODO
            Set<RoleEntity> roles = buildRoles(false);
            user.setRoles(roles);
            userRepository.save(user);
            log.info("REGISTER USER SUCCESS - email: {}", request.getEmail());
        } catch (Exception e) {
            log.error("REGISTER USER FAILED - email: {}, error: {}", request.getEmail(), e.getMessage());
            throw new BusinessException(
                    "USER_REGISTRATION_FAILED",
                    "Failed to register user",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return authenticateAndGenerateTokens(request.getEmail(), request.getPassword());
    }

    // =====================================================
    // LOGIN
    // =====================================================
    @Override
    public AuthResponse login(AuthRequestDTO loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        return authenticateAndGenerateTokens(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
    }

    // =====================================================
    // REFRESH TOKEN
    // =====================================================
    @Override
    public AuthResponse refreshToken(TokenRequestDTO request) {

        var refreshToken = refreshTokenService.findByToken(request.getToken());

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = refreshTokenService.createOrRefreshToken(userDetails);

        return buildAuthResponse(userDetails, newAccessToken, newRefreshToken);
    }

    // =====================================================
    // NOT IMPLEMENTED (PLACEHOLDERS)
    // =====================================================
    @Override
    public boolean resendVerificationEmail(String email) {
        return false;
    }

    @Override
    public boolean confirmUserAccount(String verificationToken) {
        return false;
    }

    // =====================================================
    // CORE METHODS
    // =====================================================
    private AuthResponse authenticateAndGenerateTokens(String email, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(email);

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createOrRefreshToken(userDetails);

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
                .roles(userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority
                                ::getAuthority)
                        .toList())
                .build();
    }
    public Set<RoleEntity> buildRoles(boolean isAdmin) {

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