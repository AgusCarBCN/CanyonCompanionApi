package com.canyoncompanion.canyon_api.controller;


import com.canyoncompanion.canyon_api.dtos.responses.MapResponseDTO;
import com.canyoncompanion.canyon_api.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
@Tag(name = "Maps", description = "Offline maps API")
public class MapController {

    private final MapService mapService;

    @Operation(
            summary = "Get all offline maps",
            description = "Returns all available offline maps with their associated MBTiles and preview image paths"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Offline maps retrieved successfully"
            )
    })

    @GetMapping
    public ResponseEntity<?> test() {

        var maps = mapService.getAllMaps();

        System.out.println("MAPS -> " + maps);

        return ResponseEntity.ok(maps);
    }
    /*@GetMapping
    public ResponseEntity<List<MapResponseDTO>> getAllMaps() {
        System.out.println("👉 CONTROLLER /api/maps HIT");
        return ResponseEntity.ok(
                mapService.getAllMaps()
        );
    }*/
}