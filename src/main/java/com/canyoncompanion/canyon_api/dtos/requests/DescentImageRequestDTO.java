package com.canyoncompanion.canyon_api.dtos.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DescentImageRequestDTO {

    @NotBlank
    private String imageUrl;
}
