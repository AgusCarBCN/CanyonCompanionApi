package com.canyoncompanion.canyon_api.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Slf4j
@Table(name = "waypoints")
public class WaypointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private RouteEntity route;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Double elevation;

    private Integer icon;

    private String imagePath;

    private LocalDateTime time;
}
