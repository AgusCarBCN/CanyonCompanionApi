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
        name = "UserRequest",
        description = "Request body to create a new user with basic details and addresses"
)
public class UserRequestDTO {

    @Schema(
            description = "User's first name",
            example = "John",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 20
    )
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Schema(
            description = "User's surname",
            example = "Doe",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minLength = 3,
            maxLength = 100
    )
    @Size(min = 3, max = 100, message = "Surname must be between 3 and 100 characters")
    private String surname;


    @Schema(
            description = "User's email address",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Schema(
            description = "User's password. Must contain at least one uppercase letter, one lowercase letter, one number, and one special character",
            example = "StrongPass1!",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 8
    )
    @NotBlank(message = "Password is required")
    @Size(min=8)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    private String password;


}
