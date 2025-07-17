package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for admin/agent updates to complaints.
 * Contains fields that only administrators and agents can modify.
 * This includes priority, status, agent assignment, and due dates.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReclamationAdminUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The priority level of the complaint.
     * Must be one of: BASSE, NORMALE, HAUTE, URGENTE
     */
    @Pattern(regexp = "^(BASSE|NORMALE|HAUTE|URGENTE)$", message = "La priorité doit être BASSE, NORMALE, HAUTE ou URGENTE")
    @JsonProperty("priorite")
    private String priorite;

    /**
     * The status of the complaint.
     * Must be one of: SOUMISE, EN_COURS, EN_ATTENTE_INFO, RESOLUE, FERMEE, ANNULEE
     */
    @Pattern(regexp = "^(SOUMISE|EN_COURS|EN_ATTENTE_INFO|RESOLUE|FERMEE|ANNULEE)$", 
            message = "Le statut doit être SOUMISE, EN_COURS, EN_ATTENTE_INFO, RESOLUE, FERMEE ou ANNULEE")
    @JsonProperty("statut")
    private String statut;

    /**
     * ID of the assigned agent.
     */
    @JsonProperty("agentAssigneId")
    private UUID agentAssigneId;

    /**
     * Due date for resolution (optional).
     */
    @JsonProperty("dateEcheance")
    private String dateEcheance;

    /**
     * Internal comment for admin/agent use only.
     */
    @Size(max = 1000, message = "Le commentaire interne ne peut pas dépasser 1000 caractères")
    @JsonProperty("commentaireInterne")
    private String commentaireInterne;

    /**
     * Specific fields required for the complaint category.
     */
    @JsonProperty("champsSpecifiques")
    private Map<String, Object> champsSpecifiques;

    /**
     * List of file IDs to add to the complaint.
     */
    @JsonProperty("fichiersToAdd")
    private List<UUID> fichiersToAdd;

    /**
     * List of file IDs to remove from the complaint.
     */
    @JsonProperty("fichiersToRemove")
    private List<UUID> fichiersToRemove;
} 