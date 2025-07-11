package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Concise DTO for displaying a list of complaints.
 * Used for user dashboards and agent queues with minimal data transfer.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReclamationListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the complaint.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Complaint number for easy reference.
     */
    @JsonProperty("numero")
    private String numero;

    /**
     * Title of the complaint.
     */
    @JsonProperty("titre")
    private String titre;

    /**
     * Current status of the complaint.
     */
    @JsonProperty("statut")
    private String statut;

    /**
     * Priority level of the complaint.
     */
    @JsonProperty("priorite")
    private String priorite;

    /**
     * Name of the category this complaint belongs to.
     */
    @JsonProperty("categorieNom")
    private String categorieNom;

    /**
     * When the complaint was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * When the complaint was last modified.
     */
    @JsonProperty("dateModification")
    private LocalDateTime dateModification;

    /**
     * Full name of the assigned agent.
     */
    @JsonProperty("assignedAgentNomComplet")
    private String assignedAgentNomComplet;
} 