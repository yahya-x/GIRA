package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for configuration responses.
 * Contains information about a system configuration entry.
 *
 * @author Mohamed Yahya Jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
public class ConfigurationResponse {

    /**
     * Unique identifier of the configuration entry.
     */
    @JsonProperty("id")
    private UUID id;

    /**
     * Configuration key (unique).
     */
    @JsonProperty("cle")
    private String cle;

    /**
     * Configuration value.
     */
    @JsonProperty("valeur")
    private String valeur;

    /**
     * Type of the configuration (STRING, INTEGER, BOOLEAN, JSON).
     */
    @JsonProperty("type")
    private String type;

    /**
     * Description of the configuration entry.
     */
    @JsonProperty("description")
    private String description;

    /**
     * Date and time when the configuration was last modified.
     */
    @JsonProperty("dateModification")
    private LocalDateTime dateModification;

    /**
     * Date and time when the configuration was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * User who last modified the configuration.
     */
    @JsonProperty("modifiePar")
    private UUID modifiePar;
} 