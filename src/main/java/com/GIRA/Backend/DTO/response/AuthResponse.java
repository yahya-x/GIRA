package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO representing the authentication response after a successful login or registration.
 * <p>
 * Contains the JWT token and user information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * JWT token for authenticating subsequent requests.
     */
    @JsonProperty("token")
    private String token;

    /**
     * User information associated with the authenticated session.
     */
    @JsonProperty("user")
    private UserResponse user;
} 