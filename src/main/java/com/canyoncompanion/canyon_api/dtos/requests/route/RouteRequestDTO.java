package com.canyoncompanion.canyon_api.dtos.requests.route;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
/*@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequestDTO {

    private Long descentId;

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    // métricas calculadas en cliente
    private Long time;
    private Double distance;
    private Float totalAscent;
    private Float totalDescent;

    @Valid
    @Builder.Default
    private List<WaypointRequestDTO> waypoints = new ArrayList<>();
}*/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequestDTO {

    private Long descentId;

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    /**
     * Tiempo medido por la aplicación móvil
     */
    private Long time;

    /**
     * Distancia calculada en tiempo real por la aplicación móvil (metros)
     */
    private Double distance;

    /**
     * Puntos GPS para generar GPX
     */
    @Valid
    @Builder.Default
    private List<TrackPointRequestDTO> trackPoints = new ArrayList<>();

    /**
     * Waypoints del descenso
     */
    @Valid
    @Builder.Default
    private List<WaypointRequestDTO> waypoints = new ArrayList<>();
}