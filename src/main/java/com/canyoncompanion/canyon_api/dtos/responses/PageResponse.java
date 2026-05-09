package com.canyoncompanion.canyon_api.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "PageResponse",
        description = "Generic wrapper for paginated responses"
)
public class PageResponse<T> {

    @Schema(
            description = "List of items for the current page",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<T> content;

    @Schema(
            description = "Current page number (0-indexed)",
            example = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int page;

    @Schema(
            description = "Number of items per page",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int size;

    @Schema(
            description = "Total number of elements across all pages",
            example = "150",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private long totalElements;

    @Schema(
            description = "Total number of pages available",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int totalPages;

    @Schema(
            description = "Sorting information for the page"
    )
    private Sort sort;
}

