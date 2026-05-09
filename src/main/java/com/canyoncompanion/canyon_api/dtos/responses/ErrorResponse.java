package com.canyoncompanion.canyon_api.dtos.responses;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class ErrorResponse {

    @Schema(
            description = "Error code identifying the type of error",
            example = "PAYMENT_NOT_FOUND",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String code;

    @Schema(
            description = "Human-readable error message",
            example = "Payment not found",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String message;

    @Schema(
            description = "Timestamp when the error occurred",
            example = "2026-01-17T12:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Path of the endpoint that generated the error",
            example = "/api/payments/42/refund",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String path;
}

