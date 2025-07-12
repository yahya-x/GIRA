package com.GIRA.Backend.DTO.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Generic API response wrapper for consistent API responses.
 * Used to wrap successful data responses with metadata.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Indicates if the operation was successful.
     */
    @JsonProperty("success")
    private boolean success;

    /**
     * Human-readable message describing the result.
     */
    @JsonProperty("message")
    private String message;

    /**
     * The actual data payload.
     */
    @JsonProperty("data")
    private T data;

    /**
     * Creates a successful response with data.
     * 
     * @param data the data to include in the response
     * @param <T> the type of data
     * @return ApiResponse with success=true and the provided data
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(true, "Operation successful", data);
    }

    /**
     * Creates a successful response with custom message and data.
     * 
     * @param message the success message
     * @param data the data to include in the response
     * @param <T> the type of data
     * @return ApiResponse with success=true, custom message and data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(true, message, data);
    }

    /**
     * Creates an error response with message.
     * 
     * @param message the error message
     * @param <T> the type of data (will be null)
     * @return ApiResponse with success=false and the error message
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(false, message, null);
    }
} 