package com.GIRA.Backend.DTO.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO summarizing user information for embedding in other responses.
 * <p>
 * Used for lightweight user references in API responses (e.g., file uploads, comments).
 * </p>
 *
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Unique identifier of the user.
     */
    @JsonProperty("id")
    private String id;
    /**
     * Email address of the user.
     */
    @JsonProperty("email")
    private String email;
    /**
     * Full name of the user.
     */
    @JsonProperty("nomComplet")
    private String nomComplet;
} 