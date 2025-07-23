package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO representing the user data returned in API responses.
 * <p>
 * Contains user profile information, account status, preferences, and statistics.
 * Used for transferring user data from the backend to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Unique identifier of the user.
     */
    @JsonProperty("id")
    private UUID id;

    /**
     * Email address of the user.
     */
    @JsonProperty("email")
    private String email;

    /**
     * Last name of the user.
     */
    @JsonProperty("nom")
    private String nom;

    /**
     * First name of the user.
     */
    @JsonProperty("prenom")
    private String prenom;

    /**
     * User's phone number.
     */
    @JsonProperty("telephone")
    private String telephone;

    /**
     * Preferred language of the user (e.g., 'fr', 'en').
     */
    @JsonProperty("langue")
    private String langue;

    /**
     * Date and time when the user account was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * Whether the user account is active.
     */
    @JsonProperty("actif")
    private Boolean actif;

    /**
     * Whether the user's email address has been verified.
     */
    @JsonProperty("emailVerifie")
    private Boolean emailVerifie;

    /**
     * Role information for the user.
     */
    @JsonProperty("role")
    private RoleResponse role;

    /**
     * Token used for email verification.
     */
    @JsonProperty("tokenVerification")
    private String tokenVerification;

    /**
     * Token used for password reset.
     */
    @JsonProperty("tokenResetPassword")
    private String tokenResetPassword;

    /**
     * Date and time of the user's last login.
     */
    @JsonProperty("derniereConnexion")
    private LocalDateTime derniereConnexion;

    /**
     * User preferences as key-value pairs (JSONB).
     */
    @JsonProperty("preferences")
    private Map<String, Object> preferences;

    /**
     * Number of complaints (reclamations) submitted by the user.
     */
    @JsonProperty("nombreReclamations")
    private Integer nombreReclamations;
} 