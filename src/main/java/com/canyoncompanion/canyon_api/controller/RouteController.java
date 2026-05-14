package com.canyoncompanion.canyon_api.controller;

import com.canyoncompanion.canyon_api.dtos.requests.RouteRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.RouteResponseDTO;
import com.canyoncompanion.canyon_api.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Tag(name = "Routes", description = "Routes and waypoints API")
public class RouteController {

    private final RouteService routeService;

    @Operation(summary = "Create a route", description = "Creates a new route with optional descent relation and its waypoints")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Route created successfully"),
            @ApiResponse(responseCode = "404", description = "Related descent not found")
    })
    @PostMapping
    public ResponseEntity<RouteResponseDTO> createRoute(
            @RequestBody @Valid RouteRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.createRoute(dto));
    }

    @Operation(summary = "Get all routes", description = "Returns paginated routes with sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes retrieved successfully"),
            @ApiResponse(responseCode = "406", description = "Invalid sort field")
    })
    @GetMapping
    public ResponseEntity<PageResponse<RouteResponseDTO>> getAllRoutes(
            @RequestParam(defaultValue = "date") String field,
            @RequestParam(defaultValue = "true") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(routeService.getAllRoutes(field, desc, page, size));
    }

    @Operation(summary = "Get route by id", description = "Returns one route including all waypoints")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RouteResponseDTO> getRouteById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @Operation(summary = "Get routes by descent", description = "Returns paginated routes associated to a descent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes retrieved successfully"),
            @ApiResponse(responseCode = "406", description = "Invalid sort field")
    })
    @GetMapping("/descent/{descentId}")
    public ResponseEntity<PageResponse<RouteResponseDTO>> getRoutesByDescent(
            @PathVariable Long descentId,
            @RequestParam(defaultValue = "date") String field,
            @RequestParam(defaultValue = "true") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(routeService.getRoutesByDescentId(descentId, field, desc, page, size));
    }
    @GetMapping("/me")
    public ResponseEntity<PageResponse<RouteResponseDTO>> getMyRoutes(
            @RequestParam(defaultValue = "date") String field,
            @RequestParam(defaultValue = "true") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                routeService.getRoutesByUserId (field, desc, page, size)
        );
    }
}
