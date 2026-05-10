package com.canyoncompanion.canyon_api.controller.user;


import com.canyoncompanion.canyon_api.dtos.requests.UpdateUserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.service.user.UserUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/user/update")
@PreAuthorize("hasRole('USER')")
public class UserUpdateController {

    private final UserUpdateService userUpdateService;

    // ---------------------------
    // Update user fields
    // ---------------------------
    @Operation(
            summary = "Update user profile fields",
            description = "Update authenticated user's profile information such as name, surname, or email.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PatchMapping()
    public ResponseEntity<UserResponseDTO> updateUser(
            @RequestBody UpdateUserRequestDTO request,
            Authentication authentication
    ) {
        UserResponseDTO updatedUser = userUpdateService.updateUserFields(authentication.getName(), request);
        return ResponseEntity.ok(updatedUser);
    }


    // ---------------------------
    // Update profile image
    // ---------------------------
    @Operation(
            summary = "Update user profile image",
            description = "Update profile image of the user by uploading a file.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile image updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image file"),
            @ApiResponse(responseCode = "500", description = "Failed to upload image"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PutMapping("/{userId}/profile-image")
    public ResponseEntity<String> updateProfileImage(
            @PathVariable Long userId,
            @RequestParam("image") MultipartFile file
    ) {
        try {
            byte[] imageData = file.getBytes();
            String imageType = file.getContentType();
            String imageUrl = userUpdateService.updateProfileImage(userId, imageData, imageType);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        }
    }

}
