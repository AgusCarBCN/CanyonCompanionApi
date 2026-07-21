package com.canyoncompanion.canyon_api.model.entities;


import com.canyoncompanion.canyon_api.model.enums.WayPointSymbol;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    private Float elevation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WayPointSymbol symbol;

    private String imagePath;

    private LocalDateTime time;
}
