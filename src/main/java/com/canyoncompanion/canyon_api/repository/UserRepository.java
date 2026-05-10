package com.canyoncompanion.canyon_api.repository;

import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /* =========================
       BÚSQUEDAS
       ========================= */

    Optional<UserEntity> findByNameIgnoreCase(String userName);

    Optional<UserEntity> findByEmail(String email);

    /* =========================
       FILTROS
       ========================= */
    @Query("from UserEntity  where status= 'ACTIVE'")
    Page<UserEntity> findAllActive (Pageable pageable);

    Page<UserEntity> findAllByStatus(UserStatus status,Pageable pageable);

    Page<UserEntity> findAllByCreatedAtAfter(Pageable pageable, LocalDate createdAt);

    Page<UserEntity> findAllByCreatedAtBefore(LocalDate createdAt, Pageable pageable);

    Page<UserEntity> findByCreatedAt(LocalDate createdAt, Pageable pageable);

    Page<UserEntity> findAllByCreatedAtBetween(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    Page<UserEntity> findAllByRoles_Role(Roles role, Pageable pageable);

    /* =========================
       VALIDACIONES
       ========================= */

    boolean existsByNameIgnoreCase(String userName);

    boolean existsByEmailIgnoreCase(String email);

    /* =========================
       MÉTRICAS
       ========================= */

    long count();

    long countByStatus(UserStatus status);

    /* =========================
       ELIMINACIÓN
       ========================= */

    @Modifying
    @Transactional
    @Query("DELETE FROM UserEntity u WHERE u.id = :id")
    void deleteByIdDirect(@Param("id") Long id);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<UserEntity> findByEmailWithRoles(@Param("email") String email);

}

