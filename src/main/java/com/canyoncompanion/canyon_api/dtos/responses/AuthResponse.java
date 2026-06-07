package com.canyoncompanion.canyon_api.dtos.responses;

import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "AuthResponse",
        description = "Response returned after successful authentication containing access and refresh tokens, username, and roles"
)
public class AuthResponse {

    @Schema(
            description = "JWT access token to authorize requests",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String accessToken;

    @Schema(
            description = "JWT refresh token used to generate a new access token",
            example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4..."
    )
    private String refreshToken;

    @Schema(
            description = "Authenticated user's username (email or username)",
            example = "john.doe@example.com"
    )
    private String username;

    private UserStatus status;
/*
    @Schema(
            description = "List of roles assigned to the user",
            example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]"
    )
    private List<String> roles;*/
}
