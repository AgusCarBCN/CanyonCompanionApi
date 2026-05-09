package com.canyoncompanion.canyon_api.model.entities;


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
@Table(name = "routes")
public class RouteEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "descent_id", nullable = false)
    private DescentEntity descent;

    private String name;

    @Column(nullable = false)
    private String resourcePath;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long time;
    private Double distance;
    private Float ascent;
    //private Float descent;

    private LocalDateTime date = LocalDateTime.now();
}
