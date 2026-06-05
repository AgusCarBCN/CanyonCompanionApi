package com.canyoncompanion.canyon_api.controller.user;


import com.canyoncompanion.canyon_api.dtos.requests.*;
import com.canyoncompanion.canyon_api.dtos.responses.*;
import com.canyoncompanion.canyon_api.service.TokenService;
import com.canyoncompanion.canyon_api.service.TokenServiceImpl;
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

    public ResponseEntity<RegisterResponse> registerUser(
            @RequestBody @Valid UserRequestDTO userRequestDTO
    ) {
        // Crear usuario
        var response=userAuthService.registerUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // ---------------------------
    // Verification email
    // ---------------------------
    @GetMapping("/register/verify")
    public VerificationEmailResponse verifyEmail(@RequestParam("token") String token) {
        return userAuthService.VerificationEmail(token);
    }
    // ---------------------------
    // Forgot password
    // ---------------------------
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request
            ) {
        var response=userAuthService.forgotPassword(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // ---------------------------
    // Verification email forgot password
    // ---------------------------
    @GetMapping("/forgot-password/verify")
    public VerificationEmailResponse verifyEmailForgotPassword(@RequestParam("token") String token) {
        return userAuthService.VerificationEmailForgotPassword(token);
    }
    // ---------------------------
    // Forgot password
    // ---------------------------
    @PatchMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request
    ) {
        var response=userAuthService.newPassword(request);
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
    /*@GetMapping("/register/verify")
    public ResponseEntity verifyEmail(@RequestParam("token") String token) {
        String emailString = tokenService.extractEmail(token);
        MyAppUser user = myAppUserRepository.findByEmail(emailString);
        if (user == null || user.getVerficationToken() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired!");
        }

        if (!jwtUtil.validateToken(token) || !user.getVerficationToken().equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired!");
        }
        user.setVerficationToken(null);
        user.setVerified(true);
        myAppUserRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Email successfully verified!");
    }*/

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
