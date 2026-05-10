package com.canyoncompanion.canyon_api.dtos.responses;

import com.canyoncompanion.canyon_api.model.enums.Roles;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        name = "Roles",
        description = "Represents a user role in the system"
)
public class RolesDTO {

    @Schema(
            description = "Unique identifier of the role",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long id;

    @Schema(
            description = "Role name or type",
            example = "ROLE_USER",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Roles role;
}


