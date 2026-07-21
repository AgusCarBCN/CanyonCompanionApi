package com.canyoncompanion.canyon_api.controller;

import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.RouteResponseDTO;
import com.canyoncompanion.canyon_api.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Tag(name = "Routes", description = "Public routes catalog API")
public class RouteController {

    private final RouteService routeService;

    // =====================================================
    // GET ALL ROUTES (PUBLIC FEED)
    // =====================================================
    @GetMapping
    @Operation(
            summary = "Get all routes",
            description = "Returns paginated list of all routes available in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes retrieved successfully"),
            @ApiResponse(responseCode = "406", description = "Invalid sort field")
    })
    public ResponseEntity<PageResponse<RouteResponseDTO>> getAllRoutes(
            @RequestParam(defaultValue = "date") String field,
            @RequestParam(defaultValue = "true") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                routeService.getAllRoutes(field, desc, page, size)
        );
    }

    // =====================================================
    // GET ROUTE BY ID
    // =====================================================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get route by id",
            description = "Returns a single route with all its waypoints"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<RouteResponseDTO> getRouteById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                routeService.getRouteById(id)
        );
    }

    // =====================================================
    // GET ROUTES BY DESCENT (RELATION VIEW)
    // =====================================================
    @GetMapping("/descent/{descentId}")
    @Operation(
            summary = "Get routes by descent",
            description = "Returns paginated routes linked to a specific descent"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Descent not found")
    })
    public ResponseEntity<PageResponse<RouteResponseDTO>> getRoutesByDescent(
            @PathVariable Long descentId,
            @RequestParam(defaultValue = "date") String field,
            @RequestParam(defaultValue = "true") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                routeService.getRoutesByDescentId(descentId, field, desc, page, size)
        );
    }
}