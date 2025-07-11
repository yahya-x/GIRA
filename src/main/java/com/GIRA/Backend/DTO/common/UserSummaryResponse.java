package com.GIRA.Backend.DTO.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Lightweight DTO to represent a user in other DTOs.
 * Contains only essential user information without exposing sensitive data.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the user.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Full name of the user (combination of nom and prenom).
     */
    @JsonProperty("nomComplet")
    private String nomComplet;

    /**
     * Email address of the user.
     */
    @JsonProperty("email")
    private String email;
} 