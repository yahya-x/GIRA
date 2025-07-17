package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO for requesting a password reset link.
 * Used when users forget their password and need a reset link sent to their email.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The email address of the user requesting password reset.
     * Must be a valid email format and not blank.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @JsonProperty("email")
    private String email;
} 