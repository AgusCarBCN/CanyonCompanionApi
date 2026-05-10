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
        name = "SuspensionRequest",
        description = "Request body containing the reason for suspending or deactivating a user"
)
public class SuspensionRequestDTO {

    @Schema(
            description = "Reason for suspending or deactivating the user",
            example = "Violation of terms of service",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String reason;
}
