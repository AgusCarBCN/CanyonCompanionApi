package com.canyoncompanion.canyon_api.controller.user;


import com.canyoncompanion.canyon_api.dtos.requests.AuthRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // Register Admin
    // ---------------------------
    @Operation(
            summary = "Register a new admin user",
            description = "Registers a new admin user. Only accessible to users with ADMIN role.",
            security = @SecurityRequirement(name = "Security Token")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin user registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or email already exists"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin role required")
    })
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody @Valid UserRequestDTO userRequestDTO) {

        // Crear usuario administrador
       var response= registrationService.registerAdminUser(userRequestDTO);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
}
