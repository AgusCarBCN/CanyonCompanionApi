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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth") // centralizamos todos los endpoints de autenticación
public class UserAuthController {

    private final UserAuthService userAuthService;


    // ---------------------------
    // Register User
    // ---------------------------
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user and immediately authenticates them, returning access and refresh tokens."

    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or email already exists")
    })
    @PostMapping("/register")

    public ResponseEntity<AuthResponse> registerUser(
            @RequestBody @Valid UserRequestDTO userRequestDTO
    ) {
        // Crear usuario
        var response=userAuthService.registerUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------
    // Login
    // ---------------------------
    @Operation(
            summary = "User login",
            description = "Authenticates a user with email and password, returning access and refresh tokens."

    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")

    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequestDTO request) {

        // 6️⃣ Devolver la respuesta HTTP 200 con los tokens y la info del usuario
        var response=userAuthService.login(request);
        return ResponseEntity.ok(response);
    }


    // ---------------------------
    // Refresh Token
    // ---------------------------
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token. The refresh token remains the same.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/refresh")

    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRequestDTO request) {

        var response=userAuthService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
    // ---------------------------
    // Logout
    // ---------------------------
    @Operation(
            summary = "User logout",
            description = "Invalidates the current refresh token, effectively logging the user out.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/logout")

    public ResponseEntity<Void> logout(@RequestBody TokenRequestDTO request
                                       ) {

        userAuthService.logout(request);
        return ResponseEntity.ok().build();
    }
    // ---------------------------
// Me
// ---------------------------
    @Operation(
            summary = "Get authenticated user",
            description = "Returns the currently authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")

    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {

        var response = userAuthService.me(authentication);
        return ResponseEntity.ok(response);
    }
}
