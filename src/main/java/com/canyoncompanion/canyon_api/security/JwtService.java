package com.canyoncompanion.canyon_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secretKey;

    @Value("${JWT_ACCESS_EXPIRATION}")
    private Long accessTokenExpiration;

    // =========================
    // GENERATE ACCESS TOKEN
    // =========================
    public String generateAccessToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // VALIDATION
    // =========================
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // =========================
    // INTERNAL PARSER
    // =========================
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}