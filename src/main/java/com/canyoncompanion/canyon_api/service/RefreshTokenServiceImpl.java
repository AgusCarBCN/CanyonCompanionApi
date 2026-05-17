package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.RefreshTokenRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;


    // =====================================================
    // FIND TOKEN
    // =====================================================
    @Override
    @Nullable
    public RefreshTokenEntity findByToken(@Nullable String token) {

        RefreshTokenEntity refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new BusinessException(
                        "Invalid refresh token",
                        "REFRESH_TOKEN_INVALID",
                        HttpStatus.UNAUTHORIZED
                ));

        // solo afecta a este token, no a toda la cuenta
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(refreshToken);

            throw new BusinessException(
                    "Refresh token expired",
                    "REFRESH_TOKEN_EXPIRED",
                    HttpStatus.UNAUTHORIZED
            );
        }

        return refreshToken;
    }

    // =====================================================
    // LOGIN FLOW (crear sesión nueva)
    // =====================================================
    @Override
    public String generateLoginToken(@Nullable UserDetails userDetails) {

        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // si quieres 1 sola sesión por usuario
        repository.deleteByUser(user);

        return generateAndSaveToken(user);
    }


    // =====================================================
    // REFRESH FLOW (rotación real)
    // =====================================================
    @Override
    public String rotateToken(@Nullable RefreshTokenEntity oldToken) {

        if (oldToken == null || oldToken.getUser() == null) {
            throw new BusinessException(
                    "Invalid refresh token",
                    "REFRESH_TOKEN_INVALID",
                    HttpStatus.UNAUTHORIZED
            );
        }

        // opcional pero recomendable: validar expiración aquí también
        if (oldToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(oldToken);
            throw new BusinessException(
                    "Refresh token expired",
                    "REFRESH_TOKEN_EXPIRED",
                    HttpStatus.UNAUTHORIZED
            );
        }

        // invalida SOLO este token
        repository.delete(oldToken);

        // crea nuevo
        return generateAndSaveToken(oldToken.getUser());
    }


    // =====================================================
    // CORE GENERATION
    // =====================================================
    @Nullable
    private String generateAndSaveToken(@Nullable UserEntity user) {
        RefreshTokenEntity token = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        repository.save(token);
        return token.getToken();
    }
}