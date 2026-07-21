package com.canyoncompanion.canyon_api.dtos.requests.route;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackPointRequestDTO {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private Float elevation;

    private LocalDateTime time;
}