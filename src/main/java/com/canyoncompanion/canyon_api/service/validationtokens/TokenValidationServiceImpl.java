package com.canyoncompanion.canyon_api.service.validationtokens;


import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.repository.RefreshTokenRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 86400000;



    @Override
    public String generateValidationToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();


    }


    @Override
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    @Override
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