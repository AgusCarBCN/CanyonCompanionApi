package com.canyoncompanion.canyon_api.controller.user;


import com.canyoncompanion.canyon_api.dtos.requests.AuthRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.service.user.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth") // centralizamos todos los endpoints de autenticación
public class UserAuthController {

    private final UserAuthService registrationService;

    // ---------------------------
    // Register User
    // ---------------------------
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user and immediately authenticates them, returning access and refresh tokens.",
            security = @SecurityRequirement(name = "")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or email already exists")
    })
    @PostMapping("/register/user")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> registerUser(
            @RequestBody @Valid UserRequestDTO userRequestDTO
    ) {
        // Crear usuario
        var response=registrationService.registerUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------
    // Login
    // ---------------------------
    @Operation(
            summary = "User login",
            description = "Authenticates a user with email and password, returning access and refresh tokens.",
            security = @SecurityRequirement(name = "")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    @PreAuthorize("permitAll()") // Permite que cualquier usuario (incluso no autenticado) acceda a este endpoint
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequestDTO request) {

        // 6️⃣ Devolver la respuesta HTTP 200 con los tokens y la info del usuario
        var response=registrationService.login(request);
        return ResponseEntity.ok(response);
    }


    // ---------------------------
    // Refresh Token
    // ---------------------------
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token. The refresh token remains the same.",
            security = @SecurityRequirement(name = "")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/refresh-token")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRequestDTO request) {

        var response=registrationService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
    // ---------------------------
    // Logout
    // ---------------------------
    @Operation(
            summary = "User logout",
            description = "Invalidates the current refresh token, effectively logging the user out.",
            security = @SecurityRequirement(name = "")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/logout")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> logout(@RequestBody TokenRequestDTO request) {

        registrationService.logout(request);
        return ResponseEntity.ok().build();
    }
    // ---------------------------
// Me
// ---------------------------
    @Operation(
            summary = "Get authenticated user",
            description = "Returns the data of the currently authenticated user.",
            security = @SecurityRequirement(name = "")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {

        var response = registrationService.me(authentication);
        return ResponseEntity.ok(response);
    }
}
