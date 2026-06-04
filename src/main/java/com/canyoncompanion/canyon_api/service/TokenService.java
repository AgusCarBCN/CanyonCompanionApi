package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import javax.annotation.Nullable;

public interface TokenService {

    @Nullable
    RefreshTokenEntity findByToken(@Nullable String token);

    @Nullable
    String generateLoginToken(@Nullable UserDetails userDetails);

    ;

    String generateValidationToken(String email);

    @Nullable
    String rotateToken(@Nullable RefreshTokenEntity oldToken);
}