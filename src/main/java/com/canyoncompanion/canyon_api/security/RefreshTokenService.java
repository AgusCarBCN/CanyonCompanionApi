package com.canyoncompanion.canyon_api.security;


import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.RefreshTokenRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Value("${JWT_REFRESH_EXPIRATION}")
    private Long refreshTokenExpiration;

    // Crear o refrescar refresh token
    public String createOrRefreshToken(UserDetails userDetails) {
        var user = userRepository.findByEmailWithRoles(userDetails.getUsername())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND.name(),
                        ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND));

        var refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElse(null);

        if (refreshToken == null || isExpired(refreshToken)) {
            return generateAndSaveNewToken(userDetails, user);
        }

        return refreshToken.getToken();
    }

    private boolean isExpired(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            // Eliminar token vencido
            refreshTokenRepository.delete(token);
            return true;
        }
        return false;
    }

    private String generateAndSaveNewToken(UserDetails userDetails, UserEntity user) {
        String token = jwtService.generateToken(userDetails);
        Instant expiryDate = Instant.now().plusMillis(refreshTokenExpiration);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .token(token)
                .expiryDate(expiryDate)
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public RefreshTokenEntity findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_REFRESH_TOKEN.name(),
                        ErrorCode.INVALID_REFRESH_TOKEN.getDefaultMessage(),
                        HttpStatus.BAD_REQUEST
                ));
    }
}

