package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for feedback on airport services.
 * Contains information about a user's feedback for a specific airport service (e.g., restaurant, wifi).
 *
 * @author Mohamed Yahya Jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
public class FeedbackServiceResponse {

    /**
     * Unique identifier of the feedback.
     */
    @JsonProperty("id")
    private UUID id;

    /**
     * User who gave the feedback.
     */
    @JsonProperty("utilisateurId")
    private UUID utilisateurId;

    /**
     * Type of service (RESTAURANT, BOUTIQUE, TOILETTES, etc.).
     */
    @JsonProperty("typeService")
    private String typeService;

    /**
     * Name of the service.
     */
    @JsonProperty("nomService")
    private String nomService;

    /**
     * Geographical location (as WKT or GeoJSON string).
     */
    @JsonProperty("localisation")
    private String localisation;

    /**
     * Free-text description of the location.
     */
    @JsonProperty("localisationDescription")
    private String localisationDescription;

    /**
     * Indicates whether the feedback is visible.
     */
    @JsonProperty("visible")
    private Boolean visible;

    /**
     * Rating (1-5).
     */
    @JsonProperty("note")
    private Integer note;

    /**
     * Optional comment about the service.
     */
    @JsonProperty("commentaire")
    private String commentaire;

    /**
     * Date and time when the feedback was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * Whether the feedback was moderated.
     */
    @JsonProperty("modere")
    private Boolean modere;

    /**
     * Whether the feedback was approved.
     */
    @JsonProperty("approuve")
    private Boolean approuve;

    /**
     * Moderator user ID, if applicable.
     */
    @JsonProperty("moderePar")
    private UUID moderePar;

    /**
     * Date and time when moderation occurred.
     */
    @JsonProperty("dateModeration")
    private LocalDateTime dateModeration;

    /**
     * Date and time when the feedback was last modified.
     */
    @JsonProperty("dateModification")
    private LocalDateTime dateModification;
} 