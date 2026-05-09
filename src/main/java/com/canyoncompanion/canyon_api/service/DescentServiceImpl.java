package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.DescentRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.util.helpers.Sort;
import com.canyoncompanion.canyon_api.util.mappers.DescentMapper;
import com.canyoncompanion.canyon_api.util.mappers.PageResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DescentServiceImpl implements DescentService {

    private final DescentRepository repository;
    private final DescentMapper mapper;
    private final UserRepository userRepository;

    @Override
    public List<DescentResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public PageResponse<DescentResponseDTO> findAll(Integer numberOfPages) {
        return null;
    }

    @Override
    public DescentResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.DESCENT_NOT_FOUND.name(),
                        ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND));

    }

    @Override
    public List<DescentResponseDTO> findByUser(Long userId) {
        return List.of();
    }

    @Override
    public PageResponse<DescentResponseDTO> findByUserId(Long userId, Integer numberOfPages) {

        return null;
    }

    @Override
    public PageResponse<DescentResponseDTO> findByUserEmail(String email, Integer numberOfPages) {
        var page = repository.findByUserEmail(email, PageRequest.of(numberOfPages, Sort.PAGE_SIZE))
                .map(mapper::toDTO);

        return PageResponseMapper.mapToPageResponse(page);

    }

    @Override
    public DescentResponseDTO create(DescentRequestDTO dto) {
        return null;
    }

    @Override
    public DescentResponseDTO update(Long id, DescentRequestDTO dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

}