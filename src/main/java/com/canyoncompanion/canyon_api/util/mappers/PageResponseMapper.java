package com.canyoncompanion.canyon_api.util.mappers;

import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import org.springframework.data.domain.Page;

public class PageResponseMapper<T> {

    public static <T> PageResponse<T> mapToPageResponse(Page<T> page) {

        PageResponse<T> response = new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSort()

        );

        return response;
    }
}
