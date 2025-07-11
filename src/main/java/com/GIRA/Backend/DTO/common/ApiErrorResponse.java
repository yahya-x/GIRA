package com.GIRA.Backend.DTO.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard error response for API errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("status")
    private int status;
    
    @JsonProperty("error")
    private String error;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("timestamp")
    private Instant timestamp;
    
    @JsonProperty("path")
    private String path;
} 