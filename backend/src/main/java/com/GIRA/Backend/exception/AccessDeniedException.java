package com.GIRA.Backend.exception;

/**
 * Exception thrown when access to a resource is denied.
 * Results in a 403 Forbidden response.
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
} 