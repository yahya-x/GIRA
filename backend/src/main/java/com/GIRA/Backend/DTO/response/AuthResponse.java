package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for authentication responses.
 * Contains authentication tokens and user information after successful login.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JWT access token for API authentication.
     */
    @JsonProperty("accessToken")
    private String accessToken;

    /**
     * JWT refresh token for obtaining new access tokens.
     */
    @JsonProperty("refreshToken")
    private String refreshToken;

    /**
     * Type of the token (usually "Bearer").
     */
    @JsonProperty("tokenType")
    private String tokenType;

    /**
     * Duration of the access token in seconds.
     */
    @JsonProperty("expiresIn")
    private Long expiresIn;

    /**
     * User information.
     */
    @JsonProperty("user")
    private UserResponse user;
} 