package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import com.canyoncompanion.canyon_api.model.entities.DescentImageEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.DescentRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.util.helpers.Sort;
import com.canyoncompanion.canyon_api.util.mappers.DescentImageMapper;
import com.canyoncompanion.canyon_api.util.mappers.DescentMapper;
import com.canyoncompanion.canyon_api.util.mappers.PageResponseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DescentServiceImpl implements DescentService {

    private final DescentRepository repository;
    private final DescentMapper descentMapper;
    private final DescentImageMapper descentImageMapper;
    private final UserRepository userRepository;


    @Override
    public PageResponse <DescentResponseDTO> getAllDescents(String field, Boolean desc,Integer page, Integer size) {

        val sort =  Sort.getDescentSort(field,desc);
        Pageable pageable = PageRequest.of(page,size, sort);
        val descentsPage = repository.findAll(pageable).map(descentMapper::toDTO);
        return PageResponseMapper.mapToPageResponse(descentsPage);
    }

    @Override
    public DescentResponseDTO getDescentById(Long descentId) {
        return repository.findById(descentId).map(descentMapper::toDTO).orElseThrow(() -> new BusinessException(
                "Descent not found with id: " + descentId,
                ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                HttpStatus.NOT_FOUND
        ));
    }

    @Override
    public PageResponse<DescentResponseDTO> getMyDescents(String field, Boolean desc, Integer page, Integer size) {
        val user =getCurrentUser();
        val sort =  Sort.getDescentSort(field,desc);
        Pageable pageable = PageRequest.of(page,size, sort);
        val descentsPage = repository.findAllByUserId (user.getId(),pageable).map(descentMapper::toDTO);
        return PageResponseMapper.mapToPageResponse(descentsPage);
    }
    @Transactional
    @Override
    public DescentResponseDTO createDescent(DescentRequestDTO dto) {
        val user = getCurrentUser();
        val descentEntity = descentMapper.toEntity(dto);
        descentEntity.setUser(user);
        descentEntity.setCreatedAt(LocalDateTime.now());
        val savedDescent = repository.save(descentEntity);
        return descentMapper.toDTO(savedDescent);
    }
    @Transactional
    @Override
    public DescentResponseDTO updateDescent(Long descentId, DescentRequestDTO dto) {
        val user = getCurrentUser();
        val descentEntity = repository.findById(descentId).orElseThrow(() -> new BusinessException(
                "Descent not found with id: " + descentId,
                ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                HttpStatus.NOT_FOUND
        ));
        if (!descentEntity.getUser().getId().equals(user.getId())) {
            throw new BusinessException(
                    "You are not authorized to update this descent",
                    ErrorCode.NO_OWNER_DESCENT.name(),
                    HttpStatus.FORBIDDEN
            );
        }
        descentMapper.updateEntityFromDto(dto, descentEntity);
        val updatedDescent = repository.save(descentEntity);
        return descentMapper.toDTO(updatedDescent);
    }
    @Transactional
    @Override
    public void deleteDescent(Long descentId) {
        UserEntity user = getCurrentUser();

        DescentEntity descent = repository.findById(descentId)
                .orElseThrow(() -> new BusinessException(
                        "Descent not found with id: " + descentId,
                        ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        if (!descent.getUser().getId().equals(user.getId())) {
            throw new BusinessException(
                    "You are not authorized to delete this descent",
                    ErrorCode.NO_OWNER_DESCENT.name(),
                    HttpStatus.FORBIDDEN
            );
        }

        repository.delete(descent);
    }
    @Transactional
    @Override
    public DescentResponseDTO addImage(Long descentId, String imageUrl) {
        UserEntity user = getCurrentUser();

        DescentEntity descent = repository.findById(descentId)
                .orElseThrow(() -> new BusinessException(
                        "Descent not found with id: " + descentId,
                        ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        if (!descent.getUser().getId().equals(user.getId())) {
            throw new BusinessException(
                    "You are not authorized to add an image to this descent",
                    ErrorCode.NO_OWNER_DESCENT.name(),
                    HttpStatus.FORBIDDEN
            );
        }
        DescentImageEntity image = new DescentImageEntity();
        image.setImageUrl(imageUrl);
        image.setDescent(descent);
        descent.getImages().add(image);

        return descentMapper.toDTO(descent);
    }
    @Transactional
    @Override
    public DescentResponseDTO removeImage(Long descentId, Long imageId) {
        UserEntity user = getCurrentUser();

        DescentEntity descent = repository.findById(descentId)
                .orElseThrow(() -> new BusinessException(
                        "Descent not found with id: " + descentId,
                        ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        if (!descent.getUser().getId().equals(user.getId())) {
            throw new BusinessException(
                    "You are not authorized to remove an image from this descent",
                    ErrorCode.NO_OWNER_DESCENT.name(),
                    HttpStatus.FORBIDDEN
            );
        }

        DescentImageEntity imageToRemove = descent.getImages().stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        "Image not found with id: " + imageId,
                        ErrorCode.IMAGE_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        descent.getImages().remove(imageToRemove);

        return descentMapper.toDTO(descent);

    }
    // =====================================================
    // HELPER: CURRENT USER
    // =====================================================
    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        "Authenticated user not found",
                        ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));
    }
}