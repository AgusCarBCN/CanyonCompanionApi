package com.canyoncompanion.canyon_api.controller;

import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import com.canyoncompanion.canyon_api.repository.DescentRepository;
import com.canyoncompanion.canyon_api.service.DescentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descents")
@RequiredArgsConstructor
public class DescentController {

    private final DescentService descentService;
    private final DescentRepository repo;
    // =========================
    // CREATE DESCENT
    // =========================
    @PostMapping
    public DescentResponseDTO createDescent(
            @RequestParam Long userId,
            @Valid @RequestBody DescentRequestDTO requestDTO
    ) {

        return descentService.createDescent(
                userId,
                requestDTO
        );
    }

    // =========================
    // GET USER DESCENTS
    // =========================
    @GetMapping("/user/{userId}")
    public List<DescentResponseDTO> getUserDescents(
            @PathVariable Long userId
    ) {

        return descentService.getDescentsByUser(userId);
    }
    @GetMapping("/debug")
    public List<DescentEntity> debug() {
        return repo.findAll();
    }
}

