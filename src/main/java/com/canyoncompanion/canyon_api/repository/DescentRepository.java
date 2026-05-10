package com.canyoncompanion.canyon_api.repository;

import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository responsible for database access related to descents.
 *
 * Contains public and private queries used throughout
 * the application for canyon/descent management.
 */
public interface DescentRepository
        extends JpaRepository<DescentEntity, Long> {

    /**
     * Retrieves all descents using pagination.
     *
     * Main use cases:
     * - public descent listing
     * - exploration feed
     * - infinite scroll
     */
    @Override
    Page<DescentEntity> findAll(Pageable pageable);

    /**
     * Searches descents by name.
     *
     * Features:
     * - case-insensitive
     * - partial matching
     *
     * Example:
     * "fou" -> "Barranco de la Fou"
     */
    Page<DescentEntity> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    /**
     * Retrieves descents belonging to a specific user.
     *
     * Main use cases:
     * - public user profiles
     * - viewing another user's descents
     */
    Page<DescentEntity> findByUserId(
            Long userId,
            Pageable pageable
    );

    /**
     * Retrieves descents owned by the authenticated user.
     *
     * Uses the authenticated user's email
     * from the security context.
     *
     * Main use cases:
     * - "My Descents" section
     * - authenticated endpoints
     */
    Page<DescentEntity> findByUserEmail(
            String email,
            Pageable pageable
    );

    /**
     * Retrieves a descent only if it belongs
     * to the authenticated user.
     *
     * Main use cases:
     * - secure update operations
     * - secure delete operations
     * - ownership validation
     *
     * Prevents users from modifying
     * descents owned by others.
     */
    Optional<DescentEntity> findByIdAndUserEmail(
            Long id,
            String email
    );
}