package com.canyoncompanion.canyon_api.repository;


import com.canyoncompanion.canyon_api.model.entities.WaypointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WaypointRepository extends JpaRepository<WaypointEntity, Long> {

    List<WaypointEntity> findByRouteId(Long routeId);
}