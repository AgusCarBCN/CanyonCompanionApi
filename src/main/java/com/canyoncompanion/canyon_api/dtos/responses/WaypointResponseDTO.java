package com.canyoncompanion.canyon_api.dtos.responses;

import com.canyoncompanion.canyon_api.model.enums.WayPointSymbol;
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
public class WaypointResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double elevation;
    private WayPointSymbol symbol;
    private String imagePath;
    private LocalDateTime time;
}
