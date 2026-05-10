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
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccountSettingController {

    private final UserAccountSettingService userAccountSettingService;

    // ---------------------------
    // Suspend User
    // ---------------------------
    @Operation(
            summary = "Suspend a user account",
            description = "Suspends a user account with a given reason. Admin access required.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User suspended successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or reason"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PatchMapping("/suspend")
    public ResponseEntity<Void> suspendUserById(
            @RequestBody SuspensionRequestDTO reason,
            @RequestParam Long userId,
            Authentication authentication

    ) {
        String email = authentication.getName();
        userAccountSettingService.suspendAccount(email, reason.getReason(), userId);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------
    // Reactivate User
    // ---------------------------
    @Operation(
            summary = "Reactivate a suspended user account",
            description = "Reactivates a previously suspended user account. Admin access required.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User reactivated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PatchMapping("/reactivate")
    public ResponseEntity<Void> reactivateUserById(
            @RequestParam Long userId,
            Authentication authentication

    ) {
        String email = authentication.getName();
        userAccountSettingService.reactivateAccount(email, userId);
        return ResponseEntity.noContent().build();
    }


}
