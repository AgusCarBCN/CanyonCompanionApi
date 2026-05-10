package com.canyoncompanion.canyon_api.dtos.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "AuthRequest",
        description = "Request body for user authentication (login)"
)
public class AuthRequestDTO {
    @Schema(
            description = "User email address used for authentication",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Schema(
            description = """
                User password.
                Must contain at least:
                - 1 uppercase letter
                - 1 lowercase letter
                - 1 number
                - 1 special character
                - Minimum length of 8 characters
                """,
            example = "StrongP@ssw0rd",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Size(min=8)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    private String password;
}
