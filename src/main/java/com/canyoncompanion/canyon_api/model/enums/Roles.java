package com.canyoncompanion.canyon_api.model.enums;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "Roles",
        description = "Available roles for users in the system"
)
public enum Roles {

    @Schema(description = "Administrator role with full permissions")
    ROLE_ADMIN,


    @Schema(description = "Regular user role with standard access")
    ROLE_USER

}

