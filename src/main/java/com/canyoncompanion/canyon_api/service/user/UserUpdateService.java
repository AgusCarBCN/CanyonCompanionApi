package com.canyoncompanion.canyon_api.service.user;


import com.canyoncompanion.canyon_api.dtos.requests.UpdateUserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;

/**
 * Service responsible for updating user data.
 */
public interface UserUpdateService {


    /**
     * Updates user profile fields (contact and personal information).
     *
     * @param email the user's email
     * @param request the fields to be updated
     * @return the updated user profile
     */
    UserResponseDTO updateUserFields(
            String email,
            UpdateUserRequestDTO request
    );

    /**
     * Updates the user's profile image.
     *
     * @param userId the user identifier
     * @param imageData the image data (bytes or Base64-decoded bytes)
     * @param imageType the image type (jpg, png, etc.)
     * @return the URL of the updated profile image
     */
    String updateProfileImage(
            Long userId,
            byte[] imageData,
            String imageType
    );
}
