package com.canyoncompanion.canyon_api.model.entities;


import com.canyoncompanion.canyon_api.model.enums.AquaticCharacter;
import com.canyoncompanion.canyon_api.model.enums.Commitment;
import com.canyoncompanion.canyon_api.model.enums.VerticalCharacter;
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
    @Column(name = "vertical_character")
    @Enumerated(EnumType.STRING)
    private VerticalCharacter verticalCharacter;

    @Column(name = "aquatic_character")
    @Enumerated(EnumType.STRING)
    private AquaticCharacter aquaticCharacter;

    @Column(name = "commitment")
    @Enumerated(EnumType.STRING)
    private Commitment commitment;

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