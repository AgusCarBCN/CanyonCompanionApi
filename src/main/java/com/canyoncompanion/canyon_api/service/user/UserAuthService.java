package com.canyoncompanion.canyon_api.service.user;


import com.canyoncompanion.canyon_api.dtos.requests.AuthRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
import com.canyoncompanion.canyon_api.exception.BusinessException;

public interface UserAuthService {

    /**
     * Registers a new user in the system.
     *
     * @param request the user registration data
     * @return the created user information
     * @throws BusinessException if the user already exists in the system
     */
    AuthResponse registerUser(UserRequestDTO request);


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
    public AuthResponse refreshToken(TokenRequestDTO request);

    boolean resendVerificationEmail(String email);

    /**
     * Confirms a user account using a verification token.
     *
     * @param verificationToken the verification token
     * @return true if the account was successfully confirmed
     */
    boolean confirmUserAccount(String verificationToken);
}
