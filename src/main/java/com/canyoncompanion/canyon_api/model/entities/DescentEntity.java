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
@Table(name = "descents")
public class DescentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // RELATIONSHIP
    // =========================
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // =========================
    // BASIC INFO
    // =========================
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String province;

    // =========================
    // CHARACTERISTICS
    // =========================
    @Column(name = "vertical_character", length = 3)
    private String verticalCharacter;

    @Column(name = "aquatic_character", length = 3)
    private String aquaticCharacter;

    @Column(length = 3)
    private String commitment;

    // =========================
    // LINKS / DESCRIPTION
    // =========================
    @Column(name = "description_link", length = 255)
    private String descriptionLink;

    @Column(columnDefinition = "TEXT")
    private String comments;

    // =========================
    // AUDIT
    // =========================
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

}