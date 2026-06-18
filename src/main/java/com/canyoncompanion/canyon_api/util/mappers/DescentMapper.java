package com.canyoncompanion.canyon_api.util.mappers;

import com.canyoncompanion.canyon_api.dtos.responses.DescentImageResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentPreviewDTO;
import com.canyoncompanion.canyon_api.dtos.responses.DescentResponseDTO;
import com.canyoncompanion.canyon_api.dtos.requests.DescentRequestDTO;
import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import com.canyoncompanion.canyon_api.model.entities.DescentImageEntity;
import org.mapstruct.*;

import java.util.List;
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                DescentImageMapper.class
        }
)
public interface DescentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "images", target = "images")
    DescentResponseDTO toDTO(DescentEntity entity);



    List<DescentResponseDTO> toDTOList(List<DescentEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt",
            expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt",
            expression = "java(java.time.LocalDateTime.now())")
    DescentEntity toEntity(DescentRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt",
            expression = "java(java.time.LocalDateTime.now())")
    void updateEntityFromDto(
            DescentRequestDTO dto,
            @MappingTarget DescentEntity entity
    );

    @Mapping(target = "thumbnailUrl", ignore = true)
    DescentPreviewDTO toPreviewDTO(DescentEntity entity);


}