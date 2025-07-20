package com.mdv.post.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INTERNAL_ERROR(9999, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    
    AUTHENTICATION_REQUIRED(1006, "Authentication required", HttpStatus.UNAUTHORIZED),  
    ACCESS_DENIED(1007, "Access denied: insufficient permissions", HttpStatus.FORBIDDEN),
    VALIDATION_FAILED(1009, "Data validation failed", HttpStatus.UNPROCESSABLE_ENTITY);
    

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public String format() {
        return String.format("ERR-%04d: %s", code, message);
    }
}