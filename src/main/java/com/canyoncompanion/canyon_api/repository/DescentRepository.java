package com.canyoncompanion.canyon_api.repository;

import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DescentRepository extends JpaRepository<DescentEntity, Long>, JpaSpecificationExecutor<DescentEntity> {

    // =====================================================
    // OWNERSHIP QUERIES (DESCENSOS DEL USUARIO)
    // =====================================================

    /**
     * Devuelve todos los descensos de un usuario con paginación.
     * Usado para "Mis descensos".
     */
    Page<DescentEntity> findAllByUserId(Long userId, Pageable pageable);

    /**
     * Devuelve todos los descensos de un usuario sin paginación.
     * Útil en casos simples o internos.
     */
    List<DescentEntity> findAllByUserId(Long userId);

    /**
     * Verifica si un descenso pertenece a un usuario.
     * Útil para validación rápida de permisos (ownership).
     */
    boolean existsByIdAndUserId(Long id, Long userId);

    // =====================================================
    // PUBLIC QUERIES (LISTADOS Y BÚSQUEDA)
    // =====================================================

    /**
     * Devuelve todos los descensos con paginación.
     * Usado para feed público.
     */
    Page<DescentEntity> findAll(Pageable pageable);

    /**
     * Filtra descensos por provincia (case insensitive).
     * Usado para búsquedas geográficas.
     */
    Page<DescentEntity> findByProvinceIgnoreCase(String province, Pageable pageable);

    /**
     * Búsqueda por nombre parcial (case insensitive).
     * Útil para buscador de la app.
     */
    Page<DescentEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Búsqueda por ubicación parcial (case insensitive).
     * Permite buscar por ciudad o zona.
     */
    Page<DescentEntity> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    @Query("""
            SELECT DISTINCT d
            FROM DescentEntity d
            LEFT JOIN FETCH d.images
            WHERE d.id = :id
            """)
    Optional<DescentEntity> findByIdWithImages(@Param("id") Long id);

    Page<DescentEntity> findAll(Specification<DescentEntity> spec, Pageable pageable);
}

