package com.canyoncompanion.canyon_api.controller.user;


import com.canyoncompanion.canyon_api.dtos.requests.SuspensionRequestDTO;
import com.canyoncompanion.canyon_api.service.user.UserAccountSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserAccountSettingController {

    private final UserAccountSettingService useAccountSetting;

    // ---------------------------
    // Deactivate User Account
    // ---------------------------
    @Operation(
            summary = "Deactivate user account",
            description = "Deactivates the authenticated user's account with a provided reason.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User account deactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or reason"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivatedUser(@RequestBody SuspensionRequestDTO reason,
                                            Authentication authentication) {
        String email = authentication.getName();
        useAccountSetting.deactivateAccount(email, reason.getReason());
        return ResponseEntity.noContent().build();
    }

    // ---------------------------
    // Activate User Account
    // ---------------------------
    @Operation(
            summary = "Activate user account",
            description = "Activates the authenticated user's account. Only users with DEACTIVATED status can activate their account.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User account activated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PatchMapping("/activate")
    public ResponseEntity<Void> activatedUserById() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        useAccountSetting.activateAccount(email);
        return ResponseEntity.noContent().build();
    }

}
