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

    /**
     * Suspends a user's account.
     *
     * @param email the admin user identifier
     * @param userId the userId identifier
     * @param reason the reason for suspension
     */
    void suspendAccount(String email, String reason,Long userId);

    /**
     * Reactivate account by admin
     *
     * @param email
     * @param userId
     */
    void reactivateAccount(String email, Long userId);
    /**
     * Checks whether the user's account is currently active.
     *
     * @param userId the user identifier
     * @return true if the account is active, false otherwise
     */
    boolean isAccountActive(Long userId);

    /**
     * Checks whether the user's account has been verified via email.
     *
     * @param userId the user identifier
     * @return true if the account is verified, false otherwise
     */
    boolean isAccountVerified(Long userId);

    /**
     * Deletes the user's profile image.
     *
     * @param userId the user identifier
     */
    void deleteProfileImage(Long userId);
}
