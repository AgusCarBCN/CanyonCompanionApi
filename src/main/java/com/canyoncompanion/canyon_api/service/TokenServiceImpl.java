package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.model.entities.RefreshToken;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.RefreshTokenRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 86400000;

    // =====================================================
    // FIND TOKEN
    // =====================================================
    @Override

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

    // =====================================================
    // LOGIN FLOW (crear sesión nueva)
    // =====================================================
    @Override
    public String generateLoginToken(UserDetails userDetails) {

        assert userDetails != null;
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // si quieres 1 sola sesión por usuario
        repository.deleteByUser(user);

        return generateAndSaveToken(user);
    }

    @Override
    public String generateValidationToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();


    }


    // =====================================================
    // REFRESH FLOW (rotación real)
    // =====================================================
    @Override
    public String rotateToken(RefreshToken oldToken) {

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

    private String generateAndSaveToken(UserEntity user) {
        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        repository.save(token);
        return token.getToken();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public String extractEmail(String token) {

        try {

            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build();

            return jwtParser.parseClaimsJws(token)
                    .getBody()
                    .getSubject();

        } catch (ExpiredJwtException e) {

            throw new BusinessException(
                    "EXPIRED_TOKEN",
                    "Reset token has expired",
                    HttpStatus.GONE
            );

        } catch (JwtException e) {

            throw new BusinessException(
                    "INVALID_TOKEN",
                    "Invalid reset token",
                    HttpStatus.FORBIDDEN
            );
        }

}


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build();

        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

}