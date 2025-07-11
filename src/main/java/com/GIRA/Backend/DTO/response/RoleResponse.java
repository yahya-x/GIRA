package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for role responses.
 * Contains role information that can be safely returned to clients.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the role.
     */
    @JsonProperty("id")
    private String id;

    /**
     * The name of the role.
     */
    @JsonProperty("nom")
    private String nom;

    /**
     * The description of the role.
     */
    @JsonProperty("description")
    private String description;

    /**
     * List of permissions granted to this role.
     */
    @JsonProperty("permissions")
    private List<String> permissions;

    /**
     * Whether the role is active.
     */
    @JsonProperty("actif")
    private Boolean actif;

    /**
     * Date when the role was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * Date when the role was last modified.
     */
    @JsonProperty("dateModification")
    private LocalDateTime dateModification;
} 