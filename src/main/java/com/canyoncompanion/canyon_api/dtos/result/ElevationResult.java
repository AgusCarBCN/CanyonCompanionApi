package com.canyoncompanion.canyon_api.dtos.result;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ElevationResult {
    private final Float ascent;
    private final Float descent;
}