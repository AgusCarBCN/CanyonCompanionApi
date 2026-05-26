package com.canyoncompanion.canyon_api.exception;

public enum ErrorCode {

    ADMIN_NOT_FOUND("Admin not found"),
    USER_CANNOT_NULL("User cannot be null"),
    USER_NOT_FOUND("User not found"),
    USER_IS_DEACTIVATE("User has been deactivated"),
    USER_IS_ACTIVATE("User has been activated"),
    USER_IS_SUSPEND("User has been suspende because "),
    ADMIN_AUTHORIZE("Only an admin user can "),
    USER_DEACTIVATED_REASON("User deactivated because"),
    INVALID_CREDENTIALS_PASSWORD("Invalid password"),
    INVALID_CREDENTIALS_EMAIL("Invalid email")   ,
    DESCENT_NOT_FOUND("Descent not found"),
    IMAGE_NOT_FOUND("Image not found in descent"),
    ROUTE_NOT_FOUND("Route not found"),
    TOO_MANY_ITEMS("Cannot remove more items than ordered"),
    EMAIL_ALREADY_EXISTS("Email already registered"),
    INVALID_FIELD("Invalid field"),
    INVALID_REFRESH_TOKEN("Invalid refresh token"),
    NO_OWNER_DESCENT("Only the owner of the descent can perform this action"),
    NO_OWNER_ROUTE("Only the owner of the route can be performed"),
    IMAGE_NOT_UPLOADED("Image is not uploaded"),
    FILE_NOT_FOUND("File not found"),
    IMAGE_ALREADY_EXISTS("Image already exists"),
    IMAGE_NOT_DOWNLOADED("Image is not downloaded"),
    IMAGE_NOT_DELETED("Failed to delete image"),
    FORBIDDEN("You do not have permission to perform this action"),
    FILE_IS_EMPTY("File is empty"),
    FAILED_GPX_UPLOAD("Failed to upload GPX"),
    INVALID_IMAGE_TYPE("Invalid image type"),
    INVALID_FILE_TYPE("Invalid file type"),
    INVALID_EXTENSION("Invalid file extension");
    private final String defaultMessage;

    ErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
