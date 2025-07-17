package com.GIRA.Backend.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Results in a 404 Not Found response.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 