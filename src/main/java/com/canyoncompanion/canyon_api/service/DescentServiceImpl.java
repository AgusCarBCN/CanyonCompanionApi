package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.DescentRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.util.mappers.DescentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DescentServiceImpl implements DescentService {

    private final DescentRepository descentRepository;
    private final UserRepository userRepository;
    private final DescentMapper descentMapper;

    @Override
    public DescentResponseDTO createDescent(
            Long userId,
            DescentRequestDTO requestDTO
    ) {

        // =========================
        // FIND USER
        // =========================
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // =========================
        // MAP DTO → ENTITY
        // =========================
        DescentEntity descent = descentMapper.toEntity(requestDTO);

        // =========================
        // ASSIGN USER
        // =========================
        descent.setUser(user);

        // =========================
        // SAVE
        // =========================
        DescentEntity saved = descentRepository.save(descent);

        // =========================
        // RETURN DTO
        // =========================
        return descentMapper.toDTO(saved);
    }

    @Override
    public List<DescentResponseDTO> getDescentsByUser(Long userId) {

        List<DescentEntity> descents =
                descentRepository.findByUserId(userId);

        return descents.stream()
                .map(descentMapper::toDTO)
                .toList();
    }
}