package com.canyoncompanion.canyon_api.util.mappers;


import com.canyoncompanion.canyon_api.dtos.requests.route.RouteRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.route.WaypointRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.RouteResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.WayPointImageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.WaypointResponseDTO;
import com.canyoncompanion.canyon_api.model.entities.RouteEntity;
import com.canyoncompanion.canyon_api.model.entities.WayPointImageEntity;
import com.canyoncompanion.canyon_api.model.entities.WaypointEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RouteMapper {


    // ============================
    // ENTITY -> RESPONSE
    // ============================

    @Mapping(source = "descent.id", target = "descentId")
    @Mapping(source = "descent.name", target = "descentName")
    RouteResponseDTO toResponse(RouteEntity entity);


    WaypointResponseDTO toWaypointResponse(
            WaypointEntity entity
    );


    List<WaypointResponseDTO> toWaypointResponseList(
            List<WaypointEntity> entities
    );


    @Mapping(
            source = "waypoint.id",
            target = "wayPointId"
    )
    WayPointImageResponse toWayPointImageResponse(
            WayPointImageEntity entity
    );


    // ============================
    // REQUEST -> ENTITY
    // ============================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "descent", ignore = true)
    @Mapping(target = "waypoints", ignore = true)
    @Mapping(target = "resourcePath", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "totalAscent", ignore = true)
    @Mapping(target = "totalDescent", ignore = true)
    RouteEntity toEntity(RouteRequestDTO dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "image", ignore = true)
    WaypointEntity toWaypointEntity(
            WaypointRequestDTO dto
    );
}

/*@Mapper(componentModel = "spring")
public interface RouteMapper {

    // =====================================
    // ENTITY → RESPONSE
    // =====================================

    @Mapping(source = "descent.id", target = "descentId")
    @Mapping(source = "descent.name", target = "descentName")
    RouteResponseDTO toResponse(RouteEntity entity);

    WaypointResponseDTO toWaypointResponse(WaypointEntity entity);

    List<WaypointResponseDTO> toWaypointResponseList(List<WaypointEntity> entities);

    // =====================================
    // REQUEST → ENTITY
    // =====================================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trackPoints", ignore = true)
    @Mapping(target = "waypoints", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "resourcePath", ignore = true)
    RouteEntity toEntity(RouteRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    WaypointEntity toWaypointEntity(WaypointRequestDTO dto);
}*/
