package com.canyoncompanion.canyon_api.service;


import com.canyoncompanion.canyon_api.dtos.requests.route.TrackPointRequestDTO;
import com.canyoncompanion.canyon_api.dtos.result.ElevationResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoService {


    public ElevationResult calculateElevation(
            List<TrackPointRequestDTO> points
    ) {

        float ascent = 0f;
        float descent = 0f;


        for (int i = 1; i < points.size(); i++) {

            Float previousElevation =
                    points.get(i - 1).getElevation();

            Float currentElevation =
                    points.get(i).getElevation();


            if (previousElevation == null ||
                    currentElevation == null) {
                continue;
            }


            float difference =
                    currentElevation - previousElevation;


            if (difference > 0) {
                ascent += difference;
            }
            else {
                descent += Math.abs(difference);
            }
        }


        return new ElevationResult(
                ascent,
                descent
        );
    }
}