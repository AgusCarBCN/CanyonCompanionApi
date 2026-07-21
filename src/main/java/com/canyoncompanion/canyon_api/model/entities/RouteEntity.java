package com.canyoncompanion.canyon_api.model.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "routes")
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👤 propietario de la ruta (OBLIGATORIO)
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 🧗 descenso opcional
    @ManyToOne(optional = true)
    @JoinColumn(name = "descent_id")
    private DescentEntity descent;

    // 🗺️ waypoints

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WaypointEntity> waypoints;

    // 📌 datos básicos
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String resourcePath;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 📊 métricas del tracking (cliente)
    private Long time;
    private Double distance;

    @Column(name = "ascent")
    private Float totalAscent;

    @Column(name = "descent")
    private Float totalDescent;

    // 📅 fecha creación
    private LocalDateTime date = LocalDateTime.now();
}