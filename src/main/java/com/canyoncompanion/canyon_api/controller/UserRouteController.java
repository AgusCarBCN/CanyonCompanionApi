package com.canyoncompanion.canyon_api.controller;


import com.canyoncompanion.canyon_api.dtos.requests.route.RouteRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.RouteResponseDTO;
import com.canyoncompanion.canyon_api.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user/routes")
@RequiredArgsConstructor
@Tag(name = "User Routes", description = "User routes management API (authenticated)")
@SecurityRequirement(name = "bearerAuth")
public class UserRouteController {

    private final RouteService routeService;
    // =====================================================
    // CREATE ROUTE
    // =====================================================
    @PostMapping
    @Operation(
            summary = "Create a new route",
            description = "Creates a route owned by the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Route created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<RouteResponseDTO> createRoute(
            @RequestPart("data") RouteRequestDTO dto,
            @RequestPart(value = "waypointImages", required = false)
            MultipartFile[] waypointImages
    ) {

        RouteResponseDTO route =
                routeService.createRoute(dto, waypointImages);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(route);
    }
    /*// =====================================================
    // CREATE ROUTE
    // =====================================================
    @PostMapping
    @Operation(
            summary = "Create a new route",
            description = "Creates a route owned by the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Route created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<RouteResponseDTO> createRoute(
            @RequestPart("data") RouteRequestDTO dto,
            @RequestPart(value = "waypointImages", required = false) MultipartFile[] waypointImages
    ) {

        RouteResponseDTO route = routeService.createRoute(dto, waypointImages);

        return ResponseEntity.status(HttpStatus.CREATED).body(route);
    }*/
    /*public ResponseEntity<RouteResponseDTO> createRoute(
            @RequestBody @Valid RouteRequestDTO dto

    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.createRoute(dto));
    }*/

    // =====================================================
    // GET MY ROUTES
    // =====================================================
    @GetMapping("/me")
    @Operation(
            summary = "Get my routes",
            description = "Returns paginated routes created by the authenticated user"
    )
    public ResponseEntity<PageResponse<RouteResponseDTO>> getMyRoutes(
            @RequestParam(defaultValue = "date") String field,
            @RequestParam(defaultValue = "true") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size

    ) {
        return ResponseEntity.ok(
                routeService.getMyRoutes ( field, desc, page, size)
        );
    }

    // =====================================================
    // UPDATE ROUTE
    // =====================================================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update route",
            description = "Updates a route only if the authenticated user is the owner"
    )
    public ResponseEntity<RouteResponseDTO> updateRoute(
            @PathVariable Long id,
            @RequestBody @Valid RouteRequestDTO dto

    ) {
        return ResponseEntity.ok(
                routeService.updateRoute(id, dto)
        );
    }

    // =====================================================
    // DELETE ROUTE
    // =====================================================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete route",
            description = "Deletes a route only if the authenticated user is the owner"
    )
    public ResponseEntity<Void> deleteRoute(
            @PathVariable Long id
    ) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}