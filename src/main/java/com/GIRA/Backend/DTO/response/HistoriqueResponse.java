package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for complaint history (historique) responses.
 * Contains information about actions or events in the lifecycle of a complaint.
 *
 * @author Mohamed Yahya Jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
public class HistoriqueResponse {

    /**
     * Unique identifier of the history record.
     */
    @JsonProperty("id")
    private UUID id;

    /**
     * Related complaint (reclamation) ID.
     */
    @JsonProperty("reclamationId")
    private UUID reclamationId;

    /**
     * User who performed the action.
     */
    @JsonProperty("utilisateurId")
    private UUID utilisateurId;

    /**
     * Action performed (CREATION, CHANGEMENT_STATUT, etc.).
     */
    @JsonProperty("action")
    private String action;

    /**
     * Previous value (as JSONB string, if applicable).
     */
    @JsonProperty("ancienneValeur")
    private String ancienneValeur;

    /**
     * New value (as JSONB string, if applicable).
     */
    @JsonProperty("nouvelleValeur")
    private String nouvelleValeur;

    /**
     * Date and time when the action was performed.
     */
    @JsonProperty("dateAction")
    private LocalDateTime dateAction;

    /**
     * Optional comment about the action.
     */
    @JsonProperty("commentaire")
    private String commentaire;

    /**
     * IP address from which the action was performed.
     */
    @JsonProperty("ipAdresse")
    private String ipAdresse;
} 