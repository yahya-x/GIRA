package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for verifying user email with a token.
 * Used when users click the verification link sent to their email.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The verification token received via email.
     * Must not be blank.
     */
    @NotBlank(message = "Le token de vérification est obligatoire")
    @JsonProperty("token")
    private String token;
} 