package com.canyoncompanion.canyon_api.dtos.requests;


import com.canyoncompanion.canyon_api.model.enums.AquaticCharacter;
import com.canyoncompanion.canyon_api.model.enums.Commitment;
import com.canyoncompanion.canyon_api.model.enums.VerticalCharacter;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DescentRequestDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "location is required")
    private String location;

    @NotBlank(message = "province is required")
    private String province;

    @NonNull
    private VerticalCharacter verticalCharacter;

    @NonNull
    private AquaticCharacter aquaticCharacter;

    @NonNull
    private Commitment commitment;

    private String descriptionLink;

    private String comments;

}