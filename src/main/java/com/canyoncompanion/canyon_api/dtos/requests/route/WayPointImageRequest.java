package com.canyoncompanion.canyon_api.dtos.requests.route;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WayPointImageRequest {
 /*
 * La imagen no se envía como URL, porque todavía no existe.
 *Se envía un identificador temporal generado por Android.
 * */
    private String imageKey;
}
