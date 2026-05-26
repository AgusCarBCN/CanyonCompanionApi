package com.canyoncompanion.canyon_api.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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

    // métricas calculadas en cliente
    private Long time;
    private Double distance;
    private Float totalAscent;
    private Float totalDescent;

    @Valid
    @Builder.Default
    private List<WaypointRequestDTO> waypoints = new ArrayList<>();
}