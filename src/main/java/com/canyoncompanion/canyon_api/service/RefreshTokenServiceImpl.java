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
        return repository.findByToken(token)
                .orElseThrow(() -> new BusinessException(
                        "Invalid refresh token",
                        "REFRESH_TOKEN_INVALID",
                        HttpStatus.UNAUTHORIZED
                ));
    }

    @Override
    @Nullable
    public String createOrRefreshToken(@Nullable UserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")); // or appropriate exception
        return generateAndSaveToken(user);
    }

    @Override
    @Nullable
    public String rotateToken(@Nullable RefreshTokenEntity oldToken) {
        // 1. invalidate previous token
        repository.delete(oldToken);

        // 2. generate new token
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