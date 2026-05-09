package com.canyoncompanion.canyon_api.service;



import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;

import java.util.List;
public interface DescentService {

    List<DescentResponseDTO> findAll();

    PageResponse<DescentResponseDTO> findAll(Integer numberOfPages);

    DescentResponseDTO findById(Long id);

    List<DescentResponseDTO> findByUser(Long userId);

    PageResponse<DescentResponseDTO> findByUserId(Long userId, Integer numberOfPages);

    PageResponse<DescentResponseDTO> findByUserEmail(String email, Integer numberOfPages);

    DescentResponseDTO create(DescentRequestDTO dto);

    DescentResponseDTO update(Long id, DescentRequestDTO dto);

    void delete(Long id);
}
