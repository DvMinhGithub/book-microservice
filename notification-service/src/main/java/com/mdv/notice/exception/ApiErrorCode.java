package com.mdv.notice.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // Email related errors
    EMAIL_SEND_FAILED(500, "Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_INVALID_RECIPIENT(400, "Invalid email recipient", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID_SENDER(400, "Invalid email sender", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID_SUBJECT(400, "Email subject cannot be empty", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID_CONTENT(400, "Email content cannot be empty", HttpStatus.BAD_REQUEST),
    EMAIL_SERVICE_UNAVAILABLE(503, "Email service is currently unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    EMAIL_RATE_LIMIT_EXCEEDED(429, "Email rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS),
    EMAIL_QUOTA_EXCEEDED(429, "Email quota exceeded", HttpStatus.TOO_MANY_REQUESTS),
    EMAIL_AUTHENTICATION_FAILED(401, "Email service authentication failed", HttpStatus.UNAUTHORIZED),
    EMAIL_INVALID_API_KEY(401, "Invalid email service API key", HttpStatus.UNAUTHORIZED),

    // Validation errors
    INVALID_KEY(400, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(400, "Invalid request", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(400, "Missing required field", HttpStatus.BAD_REQUEST),

    // Authentication and Authorization
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),

    // Resource not found
    EMAIL_TEMPLATE_NOT_FOUND(404, "Email template not found", HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_FOUND(404, "Notification not found", HttpStatus.NOT_FOUND);

    private final int httpCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
} 