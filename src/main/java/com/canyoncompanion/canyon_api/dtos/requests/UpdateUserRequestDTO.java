package com.canyoncompanion.canyon_api.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
        name = "UpdateUserRequest",
        description = "Request body to update user details. All fields are optional except for validations."
)
public class UpdateUserRequestDTO {

    @Schema(
            description = "User's first name",
            example = "John",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String name;

    @Schema(
            description = "User's surname",
            example = "Doe",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String surname;

    @Schema(
            description = "User's email address",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Email(message = "Email must be a valid email address")
    private String email;

    @Schema(
            description = "User's password. Must contain at least one uppercase letter, one lowercase letter, one number, and one special character",
            example = "StrongPass1!",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Size(min=8)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    private String password;

    @Schema(
            description = "URL or Base64 of the user's profile image",
            example = "https://example.com/images/profile123.png",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @JsonIgnore
    private String profileImage;
}
