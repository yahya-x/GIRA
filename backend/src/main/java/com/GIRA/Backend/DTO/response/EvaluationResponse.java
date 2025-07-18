package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for evaluation responses.
 * Contains information about a user's evaluation of a complaint's resolution.
 *
 * @author Mohamed Yahya Jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
public class EvaluationResponse {

    /**
     * Unique identifier of the evaluation.
     */
    @JsonProperty("id")
    private UUID id;

    /**
     * Related complaint (reclamation) ID.
     */
    @JsonProperty("reclamationId")
    private UUID reclamationId;

    /**
     * User who performed the evaluation.
     */
    @JsonProperty("evaluateurId")
    private UUID evaluateurId;

    /**
     * Overall rating (1-5).
     */
    @JsonProperty("noteGlobale")
    private Integer noteGlobale;

    /**
     * Speed rating (1-5).
     */
    @JsonProperty("noteRapidite")
    private Integer noteRapidite;

    /**
     * Quality rating (1-5).
     */
    @JsonProperty("noteQualite")
    private Integer noteQualite;

    /**
     * Communication rating (1-5).
     */
    @JsonProperty("noteCommunication")
    private Integer noteCommunication;

    /**
     * Resolution rating (1-5).
     */
    @JsonProperty("noteResolution")
    private Integer noteResolution;

    /**
     * Indicates whether the evaluation is active.
     */
    @JsonProperty("actif")
    private Boolean actif;

    /**
     * Date and time when the evaluation was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * Date and time when the evaluation was last modified.
     */
    @JsonProperty("dateModification")
    private LocalDateTime dateModification;

    /**
     * Optional comment about the evaluation.
     */
    @JsonProperty("commentaire")
    private String commentaire;

    /**
     * Date and time when the evaluation was made.
     */
    @JsonProperty("dateEvaluation")
    private LocalDateTime dateEvaluation;

    /**
     * Whether the user recommends the service.
     */
    @JsonProperty("recommande")
    private Boolean recommande;
} 