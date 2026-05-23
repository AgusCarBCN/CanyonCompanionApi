package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.dtos.responses.MapResponseDTO;
import com.canyoncompanion.canyon_api.model.entities.MapEntity;
import com.canyoncompanion.canyon_api.repository.MapRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final MapRepository mapRepository;

    @Override
    public List<MapResponseDTO> getAllMaps() {

        var entities = mapRepository.findAll();

        System.out.println("ENTITIES SIZE -> " + entities.size());

        return entities.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private MapResponseDTO mapToResponseDTO(MapEntity map) {

        return MapResponseDTO.builder()
                .name(map.getName())
                .mbtilesPath(map.getMbtilesPath())
                .imagePath(map.getImagePath())
                .build();
    }

}