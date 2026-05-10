package com.canyoncompanion.canyon_api.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "UserStatus",
        description = "Current status of a user account"
)
public enum UserStatus {

    @Schema(description = "Normal active user account")
    ACTIVE,

    @Schema(description = "Deactivated user account (soft delete)")
    DEACTIVATED,

    @Schema(description = "Suspended account, blocked by admin")
    SUSPENDED
}


