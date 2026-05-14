package com.canyoncompanion.canyon_api.service;

import com.canyoncompanion.canyon_api.dtos.requests.RouteFilterRequest;
import com.canyoncompanion.canyon_api.dtos.requests.RouteRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.RouteResponseDTO;

import java.util.List;

public interface RouteService {

   // RouteResponseDTO createRoute(RouteRequestDTO dto);

    PageResponse<RouteResponseDTO> getAllRoutes(
            String field,
            Boolean desc,
            Integer page,
            Integer size
    );

    RouteResponseDTO getRouteById(Long routeId);

    PageResponse<RouteResponseDTO> getRoutesByUserId(

            String field,
            Boolean desc,
            Integer page,
            Integer size
    );

    PageResponse<RouteResponseDTO> getRoutesByDescentId(
            Long descentId,
            String field,
            Boolean desc,
            Integer page,
            Integer size
    );
    RouteResponseDTO createRoute(RouteRequestDTO dto);


   // PageResponse<RouteResponseDTO> getAllRoutes(RouteFilterRequest filter);

   // PageResponse<RouteResponseDTO> getRoutesByDescentId(RouteFilterRequest filter, Long descentId);
}
