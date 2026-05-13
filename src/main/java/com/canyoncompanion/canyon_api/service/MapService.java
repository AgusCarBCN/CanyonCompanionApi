package com.canyoncompanion.canyon_api.service;

import com.canyoncompanion.canyon_api.dtos.responses.MapResponseDTO;

import java.util.List;

public interface MapService {
    List<MapResponseDTO> getAllMaps();
}
