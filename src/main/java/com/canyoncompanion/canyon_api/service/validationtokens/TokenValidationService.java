package com.canyoncompanion.canyon_api.service.validationtokens;


public interface TokenValidationService {

    boolean validateToken(String token);

    String extractEmail(String token);

    String generateValidationToken(String email);


}