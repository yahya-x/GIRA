package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO representing a user role in the system.
 * <p>
 * Contains role identifier and name.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Unique identifier of the role.
     */
    @JsonProperty("id")
    private UUID id;

    /**
     * Name of the role (e.g., 'ADMIN', 'USER').
     */
    @JsonProperty("name")
    private String name;
} 