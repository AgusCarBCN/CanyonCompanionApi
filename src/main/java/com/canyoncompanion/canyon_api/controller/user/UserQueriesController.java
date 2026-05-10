package com.canyoncompanion.canyon_api.controller.user;


import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import com.canyoncompanion.canyon_api.service.user.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path="/search-users")
@PreAuthorize("hasRole('ADMIN')")
public class UserQueriesController {
    private final UserQueryService useCase;

    // ---------------------------
    // Get User by ID
    // ---------------------------
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve user details by user ID.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/id/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        var user = useCase.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // ---------------------------
    // Get User by Email
    // ---------------------------
    @Operation(
            summary = "Get user by email",
            description = "Retrieve user details by email.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/by-email")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@RequestParam String email) {
        var user = useCase.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // ---------------------------
    // Get User by Username
    // ---------------------------
    @Operation(
            summary = "Get user by username",
            description = "Retrieve user details by username.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/by-username")
    public ResponseEntity<UserResponseDTO> getUserByUserName(@RequestParam String userName) {
        var user = useCase.getUserByUsername(userName);
        return ResponseEntity.ok(user);
    }

    // ---------------------------
    // Get Active Users
    // ---------------------------
    @Operation(
            summary = "Get active users",
            description = "Retrieve paginated list of active users.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/active-users")
    public ResponseEntity<PageResponse<UserResponseDTO>> getActiveUsers(
            @RequestParam String field,
            @RequestParam boolean desc,
            @RequestParam Integer page
    ) {
        var userPage = useCase.getActiveUsers(field, desc, page);
        return ResponseEntity.ok(userPage);
    }

    // ---------------------------
    // Get Users by Role
    // ---------------------------
    @Operation(
            summary = "Get users by role",
            description = "Retrieve paginated users with a specific role.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/by-role")
    public ResponseEntity<PageResponse<UserResponseDTO>> getUsersByRole(
            @RequestParam String field,
            @RequestParam boolean desc,
            @RequestParam Integer page,
            @RequestParam Roles role
    ) {
        var userPage = useCase.getUsersByRole(field, desc, page, role);
        return ResponseEntity.ok(userPage);
    }

    // ---------------------------
    // Get Users by Status
    // ---------------------------
    @Operation(
            summary = "Get users by status",
            description = "Retrieve paginated users filtered by their status.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @GetMapping("/by-status")
    public ResponseEntity<PageResponse<UserResponseDTO>> getUsersByStatus(
            @RequestParam String field,
            @RequestParam boolean desc,
            @RequestParam Integer page,
            @RequestParam UserStatus status
    ) {
        var userPage = useCase.getUsersByStatus(field, desc, page, status);
        return ResponseEntity.ok(userPage);
    }

}


