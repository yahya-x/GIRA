package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for updating existing roles.
 * Used by administrators to modify role properties and permissions.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The name of the role.
     * Maximum 50 characters.
     */
    @Size(max = 50, message = "Le nom du rôle ne peut pas dépasser 50 caractères")
    @JsonProperty("nom")
    private String nom;

    /**
     * The description of the role.
     * Maximum 255 characters.
     */
    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    @JsonProperty("description")
    private String description;

    /**
     * List of permissions granted to this role.
     * Can be null or empty.
     */
    @JsonProperty("permissions")
    private List<String> permissions;
} 