package com.canyoncompanion.canyon_api.service;



import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;

import java.util.List;

public interface DescentService {

    DescentResponseDTO createDescent(
            Long userId,
            DescentRequestDTO requestDTO
    );

    List<DescentResponseDTO> getDescentsByUser(Long userId);
}