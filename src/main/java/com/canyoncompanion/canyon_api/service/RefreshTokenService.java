package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import org.springframework.security.core.userdetails.UserDetails;
import javax.annotation.Nullable;

public interface RefreshTokenService {

    @Nullable
    RefreshTokenEntity findByToken(@Nullable String token);

    @Nullable
    String generateLoginToken(@Nullable UserDetails userDetails);

    @Nullable
    String rotateToken(@Nullable RefreshTokenEntity oldToken);
}