package com.mdv.identity.exception;

public class JWTSigningException extends RuntimeException {
    public JWTSigningException(String message) {
        super(message);
    }
}
