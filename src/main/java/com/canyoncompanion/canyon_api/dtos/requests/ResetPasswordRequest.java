package com.canyoncompanion.canyon_api.dtos.requests;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    private String newPassword;
    private String token;
}
