package com.GIRA.Backend.DTO.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * Detailed DTO for returning complaint (reclamation) information in API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamationResponse {
    private String id;
    private String numero;
    private String titre;
    private String description;
    private String statut;
    private String priorite;
    private String categorieNom;
    private String sousCategorieNom;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private LocalDateTime dateResolution;
    private LocalDateTime dateEcheance;
    private String localisation;
    private String lieuDescription;
    private String assignedAgentNomComplet;
    private Integer satisfaction;
    private String commentaireSatisfaction;
    private String metadonnees;
    // Add more fields as needed (e.g., files, comments)
} 