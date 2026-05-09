package com.canyoncompanion.canyon_api.dtos.responses;


import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DescentImageResponseDTO {

    private Long id;

    private String imageUrl;

    private Long descentId;

    private LocalDateTime createdAt;
}