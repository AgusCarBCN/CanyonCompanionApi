package com.canyoncompanion.canyon_api.service.user;


import com.canyoncompanion.canyon_api.dtos.requests.*;
import com.canyoncompanion.canyon_api.dtos.responses.*;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import org.springframework.security.core.Authentication;

public interface UserAuthService {

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);

    /**
     * Registers a new user in the system.
     *
     * @param request the user registration data
     * @return the created user information
     * @throws BusinessException if the user already exists in the system
     */
    //AuthResponse
    RegisterResponse registerUser(UserRequestDTO request);

     ResetPasswordResponse newPassword(ResetPasswordRequest request);

    /**
     * Login user in the system.
     *
     * @param loginRequest the request data user
     * @return LoginResponse returns user information
     * @throws BusinessException if the user already exists in the system
     */

     AuthResponse login(AuthRequestDTO loginRequest);
    /**
     * Resends the account verification email.
     *
     * @param email the user's email address
     * @return true if the verification email was successfully resent
     */

    /**
     * Refresh token
     *
     * @param request
     * @return
     */
    AuthResponse refreshToken(TokenRequestDTO request);

    /**
     * Logout session
     *
     * @param refreshToken
     */
    void logout(TokenRequestDTO refreshToken);

    /**
     * Get
     * @param authentication
     * @return
     */
    UserResponseDTO me(Authentication authentication);


    VerificationEmailResponse VerificationEmail(String verificationToken);
    VerificationEmailResponse VerificationEmailForgotPassword(String verificationToken);
    /**
     * Confirms a user account using a verification token.
     *
     * @param verificationToken the verification token
     * @return true if the account was successfully confirmed
     */
    boolean confirmUserAccount(String verificationToken);


}
