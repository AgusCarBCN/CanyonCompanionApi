package com.canyoncompanion.canyon_api.model.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "waypoint_image")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WayPointImageEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="image_path", nullable=false)
    private String imagePath;


    // Dueño de la relación
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="waypoint_id",
            nullable=false,
            unique=true
    )
    private WaypointEntity waypoint;
}
