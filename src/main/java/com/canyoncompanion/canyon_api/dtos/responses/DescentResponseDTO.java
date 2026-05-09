package com.canyoncompanion.canyon_api.dtos.responses;

import com.canyoncompanion.canyon_api.model.enums.AquaticCharacter;
import com.canyoncompanion.canyon_api.model.enums.Commitment;
import com.canyoncompanion.canyon_api.model.enums.VerticalCharacter;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DescentResponseDTO {

    private Long id;

    private String name;

    private String location;

    private String province;

    private VerticalCharacter verticalCharacter;

    private AquaticCharacter aquaticCharacter;

    private Commitment commitment;

    private String descriptionLink;

    private String comments;

    // =========================
    // RELACIÓN SIMPLIFICADA
    // =========================
    private Long userId;

    private String username;

    // =========================
    // AUDITORÍA
    // =========================
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}