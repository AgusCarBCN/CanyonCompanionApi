package com.canyoncompanion.canyon_api.model.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // BASIC INFO
    // =========================
    @Column(nullable = false, length = 120, unique = true)
    private String name;

    // =========================
    // FILE PATHS
    // =========================

    /**
     * Relative path served by Nginx.
     *
     * Example:
     * maps/mbtiles/guara.mbtiles
     */
    @Column(name = "mbtiles_path", nullable = false)
    private String mbtilesPath;

    /**
     * Relative preview image path served by Nginx.
     *
     * Example:
     * maps/previews/Guara.jpg
     */
    @Column(name = "image_path",nullable = false)
    private String imagePath;
}