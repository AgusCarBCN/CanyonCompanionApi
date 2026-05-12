package com.canyoncompanion.canyon_api.service.user;

/**
 * Service responsible for managing user account settings.
 * Includes operations such as activation, deactivation, suspension, deletion,
 * and account status verification.
 */
public interface UserAccountSettingService {

    /**
     * Activates a user's account.
     *
     * @param email the user email
     */
    void activateAccount(String email);

    /**
     * Temporarily deactivates a user's account.
     *
     * @param email the user identifier
     * @param reason the reason for deactivation
     *
     */
    void deactivateAccount(String email, String reason);


}
