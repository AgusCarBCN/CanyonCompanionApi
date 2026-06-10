package com.canyoncompanion.canyon_api.security;


import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.RefreshToken;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
@Service

public class RefreshTokenService {


    private final Long refreshTokenDuration=1209600000L;

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    // =====================================================
    // CREATE TOKEN (BASE)
    // =====================================================
    public RefreshToken createToken(UserEntity user) {

        RefreshToken token = new RefreshToken();

        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDuration)
        );


        return repository.save(token);
    }

    // =====================================================
    // CREATE SESSION (LOGIN FLOW)
    // =====================================================
    public RefreshToken createSession(UserEntity user) {

        return createToken(user);
    }

    // =====================================================
    // VALIDATE TOKEN
    // =====================================================
    public RefreshToken validateToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN.name(),
                                ErrorCode.INVALID_REFRESH_TOKEN.getDefaultMessage(),
                                HttpStatus.UNAUTHORIZED));

        if (refreshToken.isRevoked()) {
            throw new BusinessException(ErrorCode.REVOKED_TOKEN.name(),
                    ErrorCode.REVOKED_TOKEN.getDefaultMessage(),
                    HttpStatus.UNAUTHORIZED);
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN.name(),
                    ErrorCode.EXPIRED_TOKEN.getDefaultMessage(),
                    HttpStatus.UNAUTHORIZED);
        }

        return refreshToken;
    }

    // =====================================================
    // REVOKE SINGLE TOKEN
    // =====================================================
    public void revokeToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Token not found"));

        refreshToken.setRevoked(true);

        repository.save(refreshToken);
    }



    // =====================================================
    // OPTIONAL: CLEAN EXPIRED TOKENS (CRON JOB FRIENDLY)
    // =====================================================
    public void deleteExpiredTokens() {

        List<RefreshToken> expired =
                repository.findAll()
                        .stream()
                        .filter(t -> t.getExpiryDate().isBefore(Instant.now()))
                        .toList();

        repository.deleteAll(expired);
    }
    // =====================================================
    // REFRESH FLOW (rotación real)
    // =====================================================

    public RefreshToken rotateToken(RefreshToken oldToken) {

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
        return createToken(oldToken.getUser());
    }
    public void deleteToken(RefreshToken refreshToken) {
        repository.delete(refreshToken);
    }
    public RefreshToken findByToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
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

}