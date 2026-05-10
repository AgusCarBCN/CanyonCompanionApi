package com.canyoncompanion.canyon_api.service.user;

import com.canyoncompanion.canyon_api.dtos.requests.AuthRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.security.JwtService;
import com.canyoncompanion.canyon_api.security.UserDetailImpl;
import com.canyoncompanion.canyon_api.service.RefreshTokenServiceImpl;
import com.canyoncompanion.canyon_api.util.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenServiceImpl refreshTokenService;

    // =====================================================
    // REGISTER USER
    // =====================================================
    @Override
    public AuthResponse registerUser(UserRequestDTO request) {
        return register(request, false);
    }

    @Override
    public AuthResponse registerAdminUser(UserRequestDTO request) {
        return register(request, true);
    }

    private AuthResponse register(UserRequestDTO request, boolean isAdmin) {

        validateEmailAvailability(request.getEmail());

        UserEntity user = userMapper.toUserEntity(request);
        user.addRolesToUser(isAdmin);
        user.encodePassword(request.getPassword());

        UserEntity savedUser = userRepository.save(user);

        UserDetails userDetails = new UserDetailImpl(savedUser);

        return buildAuthResponse(userDetails);
    }

    // =====================================================
    // LOGIN
    // =====================================================
    @Override
    public AuthResponse login(AuthRequestDTO request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return buildAuthResponse(userDetails);
    }

    // =====================================================
    // REFRESH TOKEN
    // =====================================================
    @Override
    public AuthResponse refreshToken(TokenRequestDTO request) {

        RefreshTokenEntity storedToken =
                refreshTokenService.findByToken(request.getToken());

        UserEntity user = storedToken.getUser();

        UserDetails userDetails = new UserDetailImpl(user);

        // opcional: rotación de refresh token
        String newRefreshToken =
                refreshTokenService.rotateToken(storedToken);

        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(userDetails))
                .refreshToken(newRefreshToken)
                .userName(userDetails.getUsername())
                .roles(extractRoles(userDetails))
                .build();
    }

    // =====================================================
    // CORE AUTH RESPONSE BUILDER
    // =====================================================
    private AuthResponse buildAuthResponse(UserDetails userDetails) {

        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(userDetails))
                .refreshToken(refreshTokenService.createOrRefreshToken(userDetails))
                .userName(userDetails.getUsername())
                .roles(extractRoles(userDetails))
                .build();
    }

    // =====================================================
    // VALIDATIONS
    // =====================================================
    private void validateEmailAvailability(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException(
                    ErrorCode.INVALID_CREDENTIALS_EMAIL.name(),
                    "Email already exists",
                    HttpStatus.CONFLICT
            );
        }
    }

    // =====================================================
    // UTILS
    // =====================================================
    private List<String> extractRoles(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    // =====================================================
    // NOT USED (future features)
    // =====================================================
    @Override
    public boolean resendVerificationEmail(String email) {
        return false;
    }

    @Override
    public boolean confirmUserAccount(String verificationToken) {
        return false;
    }
}