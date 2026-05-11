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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public PageResponse<DescentResponseDTO> getMyDescents(String email, String field, Boolean desc,Integer page, Integer size) {
        val sort =  Sort.getDescentSort(field,desc);
        Pageable pageable = PageRequest.of(page,size, sort);
        val descentPage= repository.findByUserEmail(email,pageable).map(descentMapper::toDTO);
        return PageResponseMapper.mapToPageResponse(descentPage);
    }


    @Override
    @Transactional
    public DescentResponseDTO createDescent(String email, DescentRequestDTO dto) {
       // Find the user by email
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(
                "User not found with email: " + email,
                ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                HttpStatus.NOT_FOUND
        ));
        // 1️⃣ Create Descent
        DescentEntity descent = descentMapper.toEntity(dto);
        // 2️⃣ Set user
        descent.setUser(user);
        // 3️⃣ Sync images
        syncImages(descent, dto);
        // 5️⃣ Save
        DescentEntity savedDescent = repository.save(descent);

        return descentMapper.toDTO(savedDescent);
    }

    @Override
    @Transactional
    public DescentResponseDTO updateDescent(Long descentId,
                                            String email,
                                            DescentRequestDTO dto) {

        DescentEntity entity = repository
                .findByIdAndUserEmail(descentId, email)
                .orElseThrow(() ->
                        new BusinessException(
                                "Descent not found with id: " + descentId + " for user: " + email,
                                ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                                HttpStatus.NOT_FOUND
                        ));


        // Update scalar fields
        descentMapper.updateEntityFromDto(dto, entity);

        // Replace images safely
        syncImages(entity, dto);

        entity.setUpdatedAt(LocalDateTime.now());

        DescentEntity updated = repository.save(entity);

        return descentMapper.toDTO(updated);


    }
    @Override
    @Transactional
    public void deleteDescent(Long descentId, String email) {

        DescentEntity descent = repository.findByIdAndUserEmail(descentId,email).orElseThrow(() -> new BusinessException(
                "Descent not found with id: " + descentId,
                ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                HttpStatus.NOT_FOUND
        ));
        repository.delete(descent);
    }
    private void syncImages(
            DescentEntity descent,
            DescentRequestDTO dto
    ) {

        descent.getImages().clear();

        if (dto.getImages() != null) {

            dto.getImages().forEach(imageDto -> {

                DescentImageEntity image =
                        descentImageMapper.toEntity(imageDto);

                descent.addImage(image);
            });
        }
    }
}