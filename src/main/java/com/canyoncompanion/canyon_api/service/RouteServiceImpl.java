package com.canyoncompanion.canyon_api.service;

import com.canyoncompanion.canyon_api.dtos.requests.RouteRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.WaypointRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.RouteResponseDTO;
import com.canyoncompanion.canyon_api.dtos.responses.WaypointResponseDTO;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.*;
import com.canyoncompanion.canyon_api.repository.DescentRepository;
import com.canyoncompanion.canyon_api.repository.RouteRepository;
import com.canyoncompanion.canyon_api.repository.WaypointRepository;
import com.canyoncompanion.canyon_api.util.helpers.Sort;
import com.canyoncompanion.canyon_api.util.mappers.PageResponseMapper;
import com.canyoncompanion.canyon_api.util.mappers.RouteMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.RouteMatcher;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final WaypointRepository waypointRepository;
    private final DescentRepository descentRepository;
    private final CurrentUserService currentUserService;
    private final StorageService storageService;
    private final RouteMapper routeMapper;

   /* @Transactional
    @Override
    public RouteResponseDTO createRoute(RouteRequestDTO dto) {
        // 👤 usuario autenticado
        val user = currentUserService.getCurrentUser();
        // 🗺️ map DTO → entity
        val routeEntity = routeMapper.toEntity(dto);
        // 🔗 asignar usuario (OBLIGATORIO)
        routeEntity.setUser(user);

        // 🧗 descent opcional

        if (dto.getDescentId() != null) {
            DescentEntity descent = descentRepository.findById(dto.getDescentId())
                    .orElseThrow(() -> new BusinessException(
                            "Descent not found with id: " + dto.getDescentId(),
                            ErrorCode.DESCENT_NOT_FOUND.getDefaultMessage(),
                            HttpStatus.NOT_FOUND
                    ));
            routeEntity.setDescent(descent);
        }
        // 🧭 waypoints
        List<WaypointEntity> waypoints = new ArrayList<>();

        if (dto.getWaypoints() != null) {
            waypoints = dto.getWaypoints()
                    .stream()
                    .map(routeMapper::toWaypointEntity)
                    .toList();
        }

       // 🔗 relación bidireccional
        waypoints.forEach(wp -> wp.setRoute(routeEntity));
        routeEntity.setWaypoints(waypoints);

        // 💾 guardar todo (cascade)
        RouteEntity saved = routeRepository.save(routeEntity);

        // 📤 response
        return routeMapper.toResponse(saved);

    }*/

    @Override
    public RouteResponseDTO createRoute(RouteRequestDTO dto, MultipartFile gpxFile, MultipartFile[] waypointImages) {
        // 👤 usuario autenticado
        val user = currentUserService.getCurrentUser();

        // =====================================
        // 1. SAVE ROUTE
        // =====================================

        // 🗺️ map DTO → entity
        val routeEntity = routeMapper.toEntity(dto);
        // 🔗 asignar usuario (OBLIGATORIO)
        routeEntity.setUser(user);
        routeRepository.save(routeEntity);

        // =====================================
        // 2. SAVE GPX FILE
        // =====================================
        if (gpxFile != null && !gpxFile.isEmpty()) {

            String gpxUrl = storageService.saveGpxFile(gpxFile);

            routeEntity.setResourcePath(gpxUrl);
        }

        // =====================================
        // 3. WAYPOINTS + IMAGES
        // =====================================
        List<WaypointEntity> waypoints = new ArrayList<>();

        if (dto.getWaypoints() != null) {

            for (int i = 0; i < dto.getWaypoints().size(); i++) {

                WaypointRequestDTO wp = dto.getWaypoints().get(i);

                WaypointEntity entity = new WaypointEntity();
                entity.setName(wp.getName());
                entity.setDescription(wp.getDescription());
                entity.setLatitude(wp.getLatitude());
                entity.setLongitude(wp.getLongitude());
                entity.setElevation(wp.getElevation());
                entity.setIcon(wp.getIcon());
                entity.setTime(wp.getTime());
                entity.setRoute(routeEntity);

                // =====================================
                // 3.1 WAYPOINT IMAGE (BY INDEX)
                // =====================================
                if (waypointImages != null &&
                        i < waypointImages.length &&
                        waypointImages[i] != null &&
                        !waypointImages[i].isEmpty()) {

                    String imageUrl = storageService.saveImage(
                            waypointImages[i],
                            StorageType.WAYPOINT_IMAGE
                    );

                    entity.setImagePath(imageUrl);
                }

                waypoints.add(entity);
            }
        }

        // cascade persist
        routeEntity.setWaypoints(waypoints);

        val routeSaved= routeRepository.save(routeEntity);

        return routeMapper.toResponse(routeSaved);
    }

    @Override
    public RouteResponseDTO updateRoute(Long id, RouteRequestDTO dto) {
        UserEntity user = currentUserService.getCurrentUser();

        RouteEntity route = routeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Route not found",
                        ErrorCode.ROUTE_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        // 🔒 OWNERSHIP CHECK
        if (!route.getUser().getId().equals(user.getId())) {
            throw new BusinessException(
                    "You are not the owner of this route",
                    ErrorCode.FORBIDDEN.getDefaultMessage(),
                    HttpStatus.FORBIDDEN
            );
        }

        // 🔄 UPDATE FIELDS
        route.setName(dto.getName());
        route.setDescription(dto.getDescription());


        RouteEntity saved = routeRepository.save(route);

        return routeMapper.toResponse(saved);

    }

    @Override
    public void deleteRoute(Long id) {
        UserEntity user = currentUserService.getCurrentUser();

        // =====================================
        // 1. LOAD ROUTE WITH WAYPOINTS
        // =====================================
        RouteEntity route = routeRepository.findByIdWithWaypoints(id)
                .orElseThrow(() -> new BusinessException(
                        "Route not found",
                        ErrorCode.ROUTE_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        // =====================================
        // 2. CHECK OWNER
        // =====================================
        if (!route.getUser().getId().equals(user.getId())) {
            throw new BusinessException(
                    "Forbidden",
                    ErrorCode.NO_OWNER_ROUTE .name(),
                    HttpStatus.FORBIDDEN
            );
        }

        // =====================================
        // 3. DELETE GPX FILE
        // =====================================
        if (route.getResourcePath() != null) {
            storageService.deleteFile(
                    route.getResourcePath(),
                    StorageType.GPX_FILE
            );
        }

        // =====================================
        // 4. DELETE WAYPOINT IMAGES
        // =====================================
        if (route.getWaypoints() != null) {
            for (WaypointEntity wp : route.getWaypoints()) {

                if (wp.getImagePath() != null) {
                    storageService.deleteFile(
                            wp.getImagePath(),
                            StorageType.WAYPOINT_IMAGE
                    );
                }
            }
        }

        // =====================================
        // 5. DELETE ROUTE (CASCADE WAYPOINTS)
        // =====================================
        routeRepository.delete(route);

      /*  UserEntity user = currentUserService.getCurrentUser();
        RouteEntity route = routeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "Route not found",
                        ErrorCode.ROUTE_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));

        // 🔒 OWNERSHIP CHECK
        if (!route.getUser().getId().equals(user.getId())) {
            throw new BusinessException(
                    "You are not the owner of this route",
                    ErrorCode.FORBIDDEN.getDefaultMessage(),
                    HttpStatus.FORBIDDEN
            );
        }

        routeRepository.delete(route);*/
    }

    @Override
    public PageResponse<RouteResponseDTO> getMyRoutes(String field, Boolean desc, Integer page, Integer size) {
        UserEntity user = currentUserService.getCurrentUser();
        org.springframework.data.domain.Sort sort = Sort.getRouteSort(field, desc);
        Pageable pageable = PageRequest.of(page, size, sort);

        var routesPage = routeRepository.findByUserId(user.getId(), pageable)
                .map(routeMapper::toResponse);

        return PageResponseMapper.mapToPageResponse(routesPage);

    }

    @Override
    public PageResponse<RouteResponseDTO> getAllRoutes(String field, Boolean desc, Integer page, Integer size) {
        org.springframework.data.domain.Sort sort = Sort.getRouteSort(field, desc);
        Pageable pageable = PageRequest.of(page, size, sort);

        var routesPage = routeRepository.findAll(pageable)
                .map(routeMapper::toResponse);

        return PageResponseMapper.mapToPageResponse(routesPage);
    }

    @Override
    public RouteResponseDTO getRouteById(Long routeId) {

        RouteEntity route = routeRepository.findById(routeId)
                .orElseThrow(() -> new BusinessException(
                        "Route not found with id: " + routeId,
                        ErrorCode.ROUTE_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND
                ));
        return routeMapper.toResponse(route);

    }

    @Override
    public PageResponse<RouteResponseDTO> getRoutesByUserId(String field, Boolean desc, Integer page, Integer size) {

        // 👤 usuario autenticado
        val user = currentUserService.getCurrentUser();

        org.springframework.data.domain.Sort sort = Sort.getRouteSort(field, desc);
        Pageable pageable = PageRequest.of(page, size, sort);

        var routesPage = routeRepository.findByUserId(user.getId(), pageable)
                .map(routeMapper::toResponse);

        return PageResponseMapper.mapToPageResponse(routesPage);
    }

    @Override
    public PageResponse<RouteResponseDTO> getRoutesByDescentId(Long descentId, String field, Boolean desc, Integer page, Integer size) {

        org.springframework.data.domain.Sort sort = Sort.getRouteSort(field, desc);
        Pageable pageable = PageRequest.of(page, size, sort);

        var routesPage = routeRepository.findByDescentId(descentId, pageable)
                .map(routeMapper::toResponse);
        //.map(route -> toRouteResponse(route, waypointRepository.findByRouteId(route.getId())));

        return PageResponseMapper.mapToPageResponse(routesPage);
    }

    private RouteResponseDTO toRouteResponse(RouteEntity route, List<WaypointEntity> waypoints) {
        return RouteResponseDTO.builder()
                .id(route.getId())
                .descentId(route.getDescent() != null ? route.getDescent().getId() : null)
                .descentName(route.getDescent() != null ? route.getDescent().getName() : null)
                .name(route.getName())
                .resourcePath(route.getResourcePath())
                .description(route.getDescription())
                .time(route.getTime())
                .distance(route.getDistance())
                .totalAscent(route.getTotalAscent())
                .totalDescent(route.getTotalDescent())
                .date(route.getDate())
                .waypoints(waypoints.stream().map(this::toWaypointResponse).toList())
                .build();
    }

    private WaypointResponseDTO toWaypointResponse(WaypointEntity waypoint) {
        return WaypointResponseDTO.builder()
                .id(waypoint.getId())
                .name(waypoint.getName())
                .description(waypoint.getDescription())
                .latitude(waypoint.getLatitude())
                .longitude(waypoint.getLongitude())
                .elevation(waypoint.getElevation())
                .icon(waypoint.getIcon())
                .imagePath(waypoint.getImagePath())
                .time(waypoint.getTime())
                .build();
    }
}
