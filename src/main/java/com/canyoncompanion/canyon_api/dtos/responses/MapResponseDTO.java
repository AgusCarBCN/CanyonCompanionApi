package com.canyoncompanion.canyon_api.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class MapResponseDTO {

    @Schema(
            description = "Name's map",
            example = "Huesca-Guara",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
    @Schema(
            description = "Relative path served by Nginx.",
            example = "maps/mbtiles/guara.mbtiles",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String mbtilesPath;
    @Schema(
            description = "Relative path served by Nginx.",
            example = "maps/previews/guara.jpg",
            requiredMode = Schema.RequiredMode.REQUIRED
    )

    private String imagePath;

}
