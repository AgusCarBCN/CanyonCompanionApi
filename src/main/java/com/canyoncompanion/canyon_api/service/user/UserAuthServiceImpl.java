package com.canyoncompanion.canyon_api.service.user;


import com.canyoncompanion.canyon_api.dtos.requests.AuthRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import com.canyoncompanion.canyon_api.model.entities.RoleEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.repository.RefreshTokenRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.util.ClassUtils.ifPresent;


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
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // =====================================================
    // REGISTER
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
        user.setRoles(buildRoles(false));

        userRepository.save(user);

        return generateAuthSession(user);
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
                refreshTokenService.findByToken(request.getToken());

        UserEntity user = oldToken.getUser();

        String newRefreshToken = refreshTokenService.rotateToken(oldToken);

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
    public boolean resendVerificationEmail(String email) {
        return false;
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
                refreshTokenService.generateLoginToken(userDetails);

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