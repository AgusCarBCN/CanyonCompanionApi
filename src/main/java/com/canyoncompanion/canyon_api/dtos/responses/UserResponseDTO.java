package com.canyoncompanion.canyon_api.dtos.responses;

import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "UserResponse",
        description = "Represents a user including personal information, status, and addresses"
)
public class UserResponseDTO {

    @Schema(
            description = "User's first name",
            example = "John",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Schema(
            description = "User's surname",
            example = "Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String surname;

    @Schema(
            description = "Tax ID of the user",
            example = "123456789",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String taxId;

    @Schema(
            description = "Email address of the user",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "Current status of the user",
            example = "ACTIVE",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UserStatus status;

    @Schema(
            description = "Description of the user's status",
            example = "User active"
    )
    private String statusDescription;


}

