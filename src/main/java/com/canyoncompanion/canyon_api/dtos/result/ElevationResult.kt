package com.canyoncompanion.canyon_api.dtos.result

import lombok.AllArgsConstructor
import lombok.Getter


@Getter
@AllArgsConstructor
class ElevationResult(ascent1: Float, descent1: Float) {
    private val ascent: Float? = null

    private val descent: Float? = null
}