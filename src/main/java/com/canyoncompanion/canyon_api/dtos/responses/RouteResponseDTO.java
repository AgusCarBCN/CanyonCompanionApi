package com.canyoncompanion.canyon_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponseDTO {

    private Long id;
    private Long descentId;
    private String descentName;
    private String name;
    private String resourcePath;
    private String description;
    private Long time;
    private Double distance;
    private Float totalAscent;
    private Float totalDescent;
    private LocalDateTime date;
    private List<WaypointResponseDTO> waypoints;
}
