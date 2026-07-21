package com.canyoncompanion.canyon_api.service;

import com.canyoncompanion.canyon_api.dtos.requests.route.RouteRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.RouteResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface RouteService {

    // =====================================================
    // CREATE
    // =====================================================
    //RouteResponseDTO createRoute(RouteRequestDTO dto);
    RouteResponseDTO createRoute(
            RouteRequestDTO dto,
           //MultipartFile gpxFile,
            MultipartFile[] waypointImages
    );
    // =====================================================
    // UPDATE
    // =====================================================
    RouteResponseDTO updateRoute(Long id, RouteRequestDTO dto);

    // =====================================================
    // DELETE
    // =====================================================
    void deleteRoute(Long id);

    // =====================================================
    // GET MY ROUTES
    // =====================================================
    PageResponse<RouteResponseDTO> getMyRoutes(
            String field,
            Boolean desc,
            Integer page,
            Integer size
    );
    // =====================================================
    // PUBLIC METHODS
    // =====================================================
    PageResponse<RouteResponseDTO> getAllRoutes(
            String field,
            Boolean desc,
            Integer page,
            Integer size
    );

    RouteResponseDTO getRouteById(Long id);

    PageResponse<RouteResponseDTO> getRoutesByDescentId(
            Long descentId,
            String field,
            Boolean desc,
            Integer page,
            Integer size
    );

    PageResponse<RouteResponseDTO> getRoutesByUserId(

            String field,
            Boolean desc,
            Integer page,
            Integer size
    );

}
