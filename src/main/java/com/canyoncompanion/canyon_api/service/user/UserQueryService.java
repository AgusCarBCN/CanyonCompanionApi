package com.canyoncompanion.canyon_api.service.user;

import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;

import java.time.LocalDate;

/**
 * Service responsible for user queries and searches.
 * Read-only operations with no side effects.
 */
public interface UserQueryService {

    /**
     * Retrieves a user by ID.
     *
     * @param userId the user identifier
     * @return the found user
     */
    UserResponseDTO getUserById(Long userId);

    /**
     * Retrieves a user by email address.
     *
     * @param email the user's email
     * @return the found user
     */
    UserResponseDTO getUserByEmail(String email);

    /**
     * Retrieves a user by username.
     *
     * @param username the user's username
     * @return the found user
     */
    UserResponseDTO getUserByUsername(String username);

    /**
     * Retrieves a paginated list of users by role.
     *
     * @param field the field used for sorting
     * @param desc sorting order: true for descending, false for ascending
     * @param size the page number to retrieve
     * @param role the user role
     * @return a paginated response containing users with the specified role
     */
    PageResponse<UserResponseDTO> getUsersByRole(
            String field,
            Boolean desc,
            Integer size,
            Roles role
    );

    /**
     * Retrieves a paginated list of users by account status.
     *
     * @param field the field used for sorting
     * @param desc sorting order: true for descending, false for ascending
     * @param size the page number to retrieve
     * @param status the user account status
     * @return a paginated response containing users with the specified status
     */
    PageResponse<UserResponseDTO> getUsersByStatus(
            String field,
            Boolean desc,
            Integer size,
            UserStatus status
    );

    /**
     * Retrieves a paginated list of active users.
     *
     * @param field the field used for sorting
     * @param desc sorting order: true for descending, false for ascending
     * @param size the page number to retrieve
     * @return a paginated response containing active users
     */
    PageResponse<UserResponseDTO> getActiveUsers(
            String field,
            Boolean desc,
            Integer size
    );


    /**
     * Returns the total number of users.
     *
     * @return the total user count
     */
    long countAllUsers();

    /**
     * Returns the number of users by account status.
     *
     * @param status the user account status
     * @return the number of users with the given status
     */
    long countUsersByStatus(UserStatus status);

    /**
     * Retrieves the user's profile image (avatar).
     *
     * @param userId the user identifier
     * @return the URL or path of the user's profile image
     */
    String getUserProfileImage(Long userId);
}
