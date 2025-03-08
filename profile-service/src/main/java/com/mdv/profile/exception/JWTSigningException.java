package com.mdv.profile.exception;

public class JWTSigningException extends RuntimeException {
    public JWTSigningException(String message) {
        super(message);
    }
}
