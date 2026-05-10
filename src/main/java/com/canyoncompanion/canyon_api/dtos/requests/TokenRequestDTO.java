package com.canyoncompanion.canyon_api.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "TokenRequest",
        description = "Request body containing the refresh token to obtain a new access token"
)
public class TokenRequestDTO {

    @Schema(
            description = "Refresh token previously issued by the authentication endpoint",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String token;
}

