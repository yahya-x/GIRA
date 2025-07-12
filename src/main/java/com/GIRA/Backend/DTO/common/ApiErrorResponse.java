package com.GIRA.Backend.DTO.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard error response for API errors.
 * <p>
 * Used to provide consistent error information in API responses.
 * </p>
 *
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * HTTP status code of the error.
     */
    @JsonProperty("status")
    private int status;
    /**
     * Error type or code.
     */
    @JsonProperty("error")
    private String error;
    /**
     * Human-readable error message.
     */
    @JsonProperty("message")
    private String message;
    /**
     * Timestamp when the error occurred.
     */
    @JsonProperty("timestamp")
    private Instant timestamp;
    /**
     * Request path where the error occurred.
     */
    @JsonProperty("path")
    private String path;
} 