package com.GIRA.Backend.exception;

/**
 * Exception thrown when a request is invalid or malformed.
 * Results in a 400 Bad Request response.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
} 