package org.example.commonservice.exception;

// 401 - Unauthorized
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}