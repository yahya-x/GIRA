package com.GIRA.Backend.DTO.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * DTO for providing detailed validation errors.
 * Extends ApiErrorResponse to include field-specific validation errors.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The base error response.
     */
    @JsonProperty("error")
    private ApiErrorResponse error;

    /**
     * Map of field names to their corresponding error messages.
     */
    @JsonProperty("fieldErrors")
    private Map<String, String> fieldErrors;

    /**
     * Constructor for ValidationErrorResponse.
     * 
     * @param status HTTP status code
     * @param errorType error type
     * @param message general error message
     * @param timestamp when the error occurred
     * @param path the request path that caused the error
     * @param fieldErrors map of field-specific validation errors
     */
    public ValidationErrorResponse(int status, String errorType, String message, Instant timestamp, String path, Map<String, String> fieldErrors) {
        this.error = new ApiErrorResponse(status, errorType, message, timestamp, path);
        this.fieldErrors = fieldErrors;
    }
} 