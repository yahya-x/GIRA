package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for user creation requests. Used by admin and registration endpoints.
 * Supports all user profile fields and optional role assignment.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Email address of the user. Must be unique.
     */
    @JsonProperty("email")
    @Email
    @NotBlank
    private String email;
    /**
     * Password for the user account.
     */
    @JsonProperty("password")
    @NotBlank
    private String password;
    /**
     * Username (last name) of the user.
     */
    @JsonProperty("username")
    @NotBlank
    private String username;
    /**
     * First name of the user (optional).
     */
    @JsonProperty("prenom")
    private String prenom;
    /**
     * Phone number of the user (optional).
     */
    @JsonProperty("telephone")
    private String telephone;
    /**
     * Preferred language of the user (optional, default 'fr').
     */
    @JsonProperty("langue")
    private String langue;
    /**
     * Role to assign to the user (optional, e.g., PASSAGER, AGENT, ADMIN). Only used by admin.
     */
    @JsonProperty("role")
    private String role;
} 