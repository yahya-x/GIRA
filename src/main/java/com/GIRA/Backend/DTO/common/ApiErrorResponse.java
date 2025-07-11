package com.GIRA.Backend.DTO.common;

import lombok.Value;
import java.time.Instant;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard error response for API errors.
 */
@Value
public class ApiErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("status")
    int status;
    @JsonProperty("error")
    String error;
    @JsonProperty("message")
    String message;
    @JsonProperty("timestamp")
    Instant timestamp;
    @JsonProperty("path")
    String path;
} 