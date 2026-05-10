

package com.canyoncompanion.canyon_api.repository;

import com.canyoncompanion.canyon_api.model.entities.DescentImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository responsible for database access
 * related to descent images.
 *
 * Handles image retrieval and ownership validation
 * for descent image management.
 */
public interface DescentImageRepository
        extends JpaRepository<DescentImageEntity, Long> {

    /**
     * Retrieves all images associated with
     * a specific descent.
     *
     * Main use cases:
     * - descent detail screen
     * - image galleries
     * - slideshow/carousel rendering
     */
    List<DescentImageEntity> findByDescentId(Long descentId);

    /**
     * Retrieves an image only if it belongs
     * to a descent owned by the authenticated user.
     *
     * Main use cases:
     * - secure image deletion
     * - secure image update
     * - ownership validation
     *
     * Prevents users from modifying images
     * belonging to descents owned by others.
     */
    Optional<DescentImageEntity> findByIdAndDescentId(
            Long id,
            Long descentId
    );
}