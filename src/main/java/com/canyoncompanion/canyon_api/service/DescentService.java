package com.canyoncompanion.canyon_api.service;



import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing descents (canyons).
 *
 * Defines the core business operations:
 * - creation
 * - retrieval
 * - update
 * - deletion
 *
 * All write operations are secured so that only
 * the owner of a descent can modify it.
 */
public interface DescentService {

    /**
     * Retrieves all descents with pagination.
     */
    PageResponse<DescentResponseDTO> getAllDescents(String field,
                                                    Boolean desc,
                                                    Integer page,
                                                    Integer size);

    /**
     * Retrieves a single descent by its ID.
     */
    DescentResponseDTO getDescentById(Long descentId);

    /**
     * Retrieves descents belonging to the authenticated user.
     */
    PageResponse<DescentResponseDTO> getMyDescents(String email,String field,
                                                   Boolean desc,
                                                   Integer page,
                                                   Integer size );

    /**
     * Creates a new descent for the authenticated user.
     */
    DescentResponseDTO createDescent(String email, DescentRequestDTO dto);

    /**
     * Updates an existing descent.
     *
     * Only allowed if the descent belongs to the authenticated user.
     */
    DescentResponseDTO updateDescent(
            Long descentId,
            String email,
            DescentRequestDTO dto
    );

    /**
     * Deletes a descent.
     *
     * Only allowed if the descent belongs to the authenticated user.
     */
    void deleteDescent(Long descentId, String email);
}