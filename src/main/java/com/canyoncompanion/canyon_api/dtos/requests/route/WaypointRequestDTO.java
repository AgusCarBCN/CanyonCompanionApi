package com.canyoncompanion.canyon_api.dtos.requests.route;

import com.canyoncompanion.canyon_api.model.enums.WayPointSymbol;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaypointRequestDTO {

    private String name;

    private String description;

    @NotNull(message = "latitude is required")
    private Double latitude;

    @NotNull(message = "longitude is required")
    private Double longitude;

    private Float elevation;

    @NotNull(message = "symbol is required")
    private WayPointSymbol symbol;

    private LocalDateTime time;
}