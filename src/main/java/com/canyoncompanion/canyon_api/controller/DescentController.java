package com.canyoncompanion.canyon_api.controller;

import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.service.DescentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/descents")
@RequiredArgsConstructor
public class DescentController {

    private final DescentService descentService;

    // =====================================================
    // GET ALL (PUBLIC FEED)
    // =====================================================
    @Operation(
            summary = "Get all descents (public feed)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "descents retrieved successfully"
            )
    })
    @GetMapping

    public ResponseEntity<PageResponse<DescentResponseDTO>> getAllDescents(
            @RequestParam(defaultValue = "name") String field,
            @RequestParam(defaultValue = "false") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                descentService.getAllDescents(field, desc, page, size)
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @Operation(
            summary = "Get descent by id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "user's descent retrieved successfully"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Descent not found")

    })
    @GetMapping("/{id}")

    public ResponseEntity<DescentResponseDTO> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                descentService.getDescentById(id)
        );
    }

}