package com.canyoncompanion.canyon_api.dtos.responses;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WayPointImageResponse {

    private Long id;

    private Long wayPointId;

    private String imagePath;
}
