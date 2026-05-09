package com.canyoncompanion.canyon_api.repository;


import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DescentRepository extends JpaRepository<DescentEntity, Long> {

    List<DescentEntity> findByUserId(Long userId);
}