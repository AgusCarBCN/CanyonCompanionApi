package com.canyoncompanion.canyon_api.repository;


import com.canyoncompanion.canyon_api.model.entities.DescentImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DescentImageRepository extends JpaRepository<DescentImageEntity, Long> {

    List<DescentImageEntity> findByDescentId(Long descentId);
}