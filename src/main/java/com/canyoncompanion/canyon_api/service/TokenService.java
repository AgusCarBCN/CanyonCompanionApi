package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.model.entities.RefreshToken;
import org.springframework.security.core.userdetails.UserDetails;
import javax.annotation.Nullable;

public interface TokenService {

    @Nullable
    RefreshToken findByToken(@Nullable String token);

    @Nullable
    String generateLoginToken(@Nullable UserDetails userDetails);

    ;

    String generateValidationToken(String email);

    @Nullable
    String rotateToken(@Nullable RefreshToken oldToken);
}