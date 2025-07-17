package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for updating a user's password.
 * <p>
 * Contains the current and new password for secure password change.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The user's current password (for verification).
     */
    @JsonProperty("currentPassword")
    @NotBlank
    private String currentPassword;

    /**
     * The new password to set.
     */
    @JsonProperty("newPassword")
    @NotBlank
    private String newPassword;
} 