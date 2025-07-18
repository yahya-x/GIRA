package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for confirming a password reset with a token.
 * Used when users click the reset link and provide a new password.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetConfirmRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The reset token received via email.
     * Must not be blank.
     */
    @NotBlank(message = "Le token de réinitialisation est obligatoire")
    @JsonProperty("token")
    private String token;

    /**
     * The new password to set.
     * Must be at least 8 characters long.
     */
    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caractères")
    @JsonProperty("newPassword")
    private String newPassword;
} 