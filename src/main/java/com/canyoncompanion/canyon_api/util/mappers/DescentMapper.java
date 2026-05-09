package com.canyoncompanion.canyon_api.util.mappers;


import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DescentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "verticalCharacter", target = "verticalCharacter")
    @Mapping(source = "aquaticCharacter", target = "aquaticCharacter")
    @Mapping(source = "commitment", target = "commitment")
    DescentResponseDTO toDTO(DescentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    DescentEntity toEntity(DescentRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(DescentRequestDTO dto, @MappingTarget DescentEntity entity);
}