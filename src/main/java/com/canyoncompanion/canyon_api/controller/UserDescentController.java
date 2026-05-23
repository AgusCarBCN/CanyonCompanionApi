package com.canyoncompanion.canyon_api.controller;


import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.service.DescentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user/descents")
@RequiredArgsConstructor

public class UserDescentController {

    private final DescentService descentService;

    // =====================================================
    // MY DESCENTS (AUTH USER)
    // =====================================================
    @Operation(
            summary = "Get my descents",
            security = @SecurityRequirement(name = "bearerAuth")
    )


    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",
                    description = "Descent not found"),
            @ApiResponse(
                    responseCode = "200",
                    description = "descents retrieved successfully"
            )
    })
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
    @Operation(
            summary = "Create descent",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Descent registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createDescent(

            @RequestPart("data") @Valid DescentRequestDTO requestDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {

        descentService.createDescent(requestDTO, files);

        return ResponseEntity.ok().build();
    }

    // =====================================================
    // UPDATE DESCENT
    // =====================================================
    @Operation(
            summary = "Update descent",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Descent updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(
            summary = "Delete descent",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Descent removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(
            summary = "Add image to descent",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(
            summary = "Remove image from descent",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
