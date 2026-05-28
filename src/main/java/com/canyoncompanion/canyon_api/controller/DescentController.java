package com.canyoncompanion.canyon_api.controller;

import com.canyoncompanion.canyon_api.dtos.responses.DescentPreviewDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.model.enums.AquaticCharacter;
import com.canyoncompanion.canyon_api.model.enums.Commitment;
import com.canyoncompanion.canyon_api.model.enums.VerticalCharacter;
import com.canyoncompanion.canyon_api.service.DescentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    public ResponseEntity<PageResponse<DescentPreviewDTO>> getDescents(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String location,

            @RequestParam(required = false)
            String province,

            @RequestParam(required = false)
            VerticalCharacter verticalCharacter,

            @RequestParam(required = false)
            AquaticCharacter aquaticCharacter,

            @RequestParam(required = false)
            Commitment commitment,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to


    ) {

        return ResponseEntity.ok(
                descentService.getDescents(name,
                        location,
                        province,
                        verticalCharacter,
                        aquaticCharacter,
                        commitment,
                        from,
                        to)
        );

    }
   /* @GetMapping

    public ResponseEntity<PageResponse<DescentPreviewDTO>> getAllDescents(
            @RequestParam(defaultValue = "name") String field,
            @RequestParam(defaultValue = "false") Boolean desc,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                descentService.getAllDescents(field, desc, page, size)
        );
    }*/

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