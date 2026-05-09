package com.canyoncompanion.canyon_api.repository;


import com.canyoncompanion.canyon_api.model.entities.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

    List<RouteEntity> findByDescentId(Long descentId);
}