package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for updating user information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * New username for the user (optional).
     */
    @JsonProperty("username")
    private String username;

    /**
     * New email for the user (optional, must be valid if provided).
     */
    @JsonProperty("email")
    @Email
    private String email;
} 