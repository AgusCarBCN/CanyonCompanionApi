package com.canyoncompanion.canyon_api.service;



import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentPreviewDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import com.canyoncompanion.canyon_api.model.enums.AquaticCharacter;
import com.canyoncompanion.canyon_api.model.enums.Commitment;
import com.canyoncompanion.canyon_api.model.enums.VerticalCharacter;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface DescentService {

    // =====================================================
    // READ
    // =====================================================

    /**
     * Retrieves all descents (public feed) with pagination and sorting.
     */
    PageResponse<DescentPreviewDTO> getAllDescents(
            String field,
            Boolean desc,
            Integer page,
            Integer size
    );

    /**
     * Retrieves a single descent by its ID.
     */
    DescentResponseDTO getDescentById(Long descentId);

    /**
     * Retrieves descents owned by the authenticated user.
     */
    PageResponse<DescentPreviewDTO> getMyDescents(
            String name,
            String location,
            String province,
            VerticalCharacter verticalCharacter,
            AquaticCharacter aquaticCharacter,
            Commitment commitment
    );

    // =====================================================
    // CREATE
    // =====================================================

    /**
     * Creates a new descent for the authenticated user.
     */
    // DescentResponseDTO createDescent(DescentRequestDTO dto);

    // =====================================================
    // UPDATE
    // =====================================================
    //public void updateDescent(Long id, List<Long> imagesToDelete,DescentRequestDTO dto, MultipartFile[] files);
    /**
     * Updates a descent.
     * Only allowed if the authenticated user is the owner.
     */
    DescentResponseDTO updateDescent(
            Long descentId,
            DescentRequestDTO dto
    );

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    void updateDescent(
            Long id,
            DescentRequestDTO dto,
            List<Long> imagesToDelete,
            MultipartFile[] files
    );

    /**
     * Deletes a descent.
     * Only allowed if the authenticated user is the owner.
     */
    void deleteDescent(Long descentId);

    // =====================================================
    // IMAGES
    // =====================================================

    /**
     * Adds an image to a descent.
     * Only owner can modify.
     */
    DescentResponseDTO addImage(Long descentId, String imageUrl);

    /**
     * Removes an image from a descent.
     * Only owner can modify.
     */
    DescentResponseDTO removeImage(Long descentId, Long imageId);


    void createDescent(
            DescentRequestDTO dto,
            List<MultipartFile> files
    );
     PageResponse<DescentPreviewDTO> getDescents(

            String name,
            String location,
            String province,
            VerticalCharacter verticalCharacter,
            AquaticCharacter aquaticCharacter,
            Commitment commitment
    );

}