package com.canyoncompanion.canyon_api.controller;

import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.service.DescentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/descents")
public class DescentController {

    private final DescentService descentService;

    // ------------------------------------------------
    // Get all descents (public)
    // ------------------------------------------------
    @Operation(
            summary = "Get all descents",
            description = "Returns a paginated list of all descents."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Descents retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PageResponse<DescentResponseDTO>> getAllDescents(

            @RequestParam(defaultValue = "createdAt")
            String field,

            @RequestParam(defaultValue = "true")
            Boolean desc,

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "10")
            Integer size
    ) {

        var response = descentService.getAllDescents(
                field,
                desc,
                page,
                size
        );

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------
    // Get descent by ID (public)
    // ------------------------------------------------
    @Operation(
            summary = "Get descent by ID",
            description = "Returns detailed information about a specific descent."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Descent retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Descent not found")
    })
    @GetMapping("/{descentId}")
    public ResponseEntity<DescentResponseDTO> getDescentById(
            @PathVariable Long descentId
    ) {

        var response = descentService.getDescentById(descentId);

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------
    // Get authenticated user descents
    // ------------------------------------------------
    @Operation(
            summary = "Get authenticated user descents",
            description = "Returns paginated descents belonging to the authenticated user.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Descents retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/me")
    public ResponseEntity<PageResponse<DescentResponseDTO>> getMyDescents(

            Authentication authentication,

            @RequestParam(defaultValue = "createdAt")
            String field,

            @RequestParam(defaultValue = "true")
            Boolean desc,

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "10")
            Integer size
    ) {

        var response = descentService.getMyDescents(
                authentication.getName(),
                field,
                desc,
                page,
                size
        );

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------
    // Create descent
    // ------------------------------------------------
    @Operation(
            summary = "Create a new descent",
            description = "Creates a new descent for the authenticated user.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Descent created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/me")
    public ResponseEntity<DescentResponseDTO> createDescent(
            @Valid @RequestBody DescentRequestDTO requestDTO,
            Authentication authentication
    ) {

        var response = descentService.createDescent(
                authentication.getName(),
                requestDTO
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ------------------------------------------------
    // Update descent
    // ------------------------------------------------
    @Operation(
            summary = "Update a descent",
            description = "Updates a descent owned by the authenticated user.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Descent updated successfully"),
            @ApiResponse(responseCode = "404", description = "Descent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PutMapping("/me/{descentId}")
    public ResponseEntity<DescentResponseDTO> updateDescent(
            @PathVariable Long descentId,
            @Valid @RequestBody DescentRequestDTO requestDTO,
            Authentication authentication
    ) {

        var response = descentService.updateDescent(
                descentId,
                authentication.getName(),
                requestDTO
        );

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------
    // Delete descent
    // ------------------------------------------------
    @Operation(
            summary = "Delete a descent",
            description = "Deletes a descent owned by the authenticated user.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Descent deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Descent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/me/{descentId}")
    public ResponseEntity<Void> deleteDescent(
            @PathVariable Long descentId,
            Authentication authentication
    ) {

        descentService.deleteDescent(
                descentId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}