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
    @GetMapping("/{id}")

    public ResponseEntity<DescentResponseDTO> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                descentService.getDescentById(id)
        );
    }

    // =====================================================
    // MY DESCENTS (AUTH USER)
    // =====================================================
    @GetMapping("/me")

    public ResponseEntity<PageResponse<DescentResponseDTO>> getMyDescents(
            @RequestParam(defaultValue = "name") String field,
            @RequestParam(defaultValue = "false") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                descentService.getMyDescents(field, desc, page, size)
        );
    }

    // =====================================================
    // CREATE DESCENT
    // =====================================================
    @PostMapping

    public ResponseEntity<DescentResponseDTO> createDescent(
            @RequestBody @Valid DescentRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(descentService.createDescent(dto));
    }

    // =====================================================
    // UPDATE DESCENT
    // =====================================================
    @PutMapping("/{id}")

    public ResponseEntity<DescentResponseDTO> updateDescent(
            @PathVariable Long id,
            @RequestBody @Valid DescentRequestDTO dto
    ) {
        return ResponseEntity.ok(
                descentService.updateDescent(id, dto)
        );
    }

    // =====================================================
    // DELETE DESCENT
    // =====================================================
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteDescent(
            @PathVariable Long id
    ) {
        descentService.deleteDescent(id);
        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // ADD IMAGE
    // =====================================================
    @PostMapping("/{id}/images")
    public ResponseEntity<DescentResponseDTO> addImage(
            @PathVariable Long id,
            @RequestParam String imageUrl
    ) {
        return ResponseEntity.ok(
                descentService.addImage(id, imageUrl)
        );
    }

    // =====================================================
    // REMOVE IMAGE
    // =====================================================
    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<DescentResponseDTO> removeImage(
            @PathVariable Long id,
            @PathVariable Long imageId
    ) {
        return ResponseEntity.ok(
                descentService.removeImage(id, imageId)
        );
    }
}