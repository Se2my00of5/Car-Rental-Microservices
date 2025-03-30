package org.example.userservice.exception;

// 400 - Bad Request
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}