package com.canyoncompanion.canyon_api.util.mappers;


import com.canyoncompanion.canyon_api.dtos.requests.DescentImageRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentImageResponseDTO;
import com.canyoncompanion.canyon_api.model.entities.DescentImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DescentImageMapper {

    // =========================
    // ENTITY → RESPONSE DTO
    // =========================
    @Mapping(source = "descent.id", target = "descentId")
    DescentImageResponseDTO toDTO(DescentImageEntity entity);

    // =========================
    // REQUEST DTO → ENTITY
    // =========================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "descent", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    DescentImageEntity toEntity(DescentImageRequestDTO dto);
}