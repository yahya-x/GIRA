package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO for updating complaints.
 * Used for both user and agent updates with careful validation logic in service layer.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReclamationUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The title of the complaint.
     */
    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    @JsonProperty("titre")
    private String titre;

    /**
     * The description of the complaint.
     */
    @JsonProperty("description")
    private String description;

    /**
     * The priority level of the complaint.
     */
    @JsonProperty("priorite")
    private String priorite;

    /**
     * The status of the complaint (for agent updates).
     */
    @JsonProperty("statut")
    private String statut;

    /**
     * ID of the assigned agent (for agent updates).
     */
    @JsonProperty("agentAssigneId")
    private String agentAssigneId;

    /**
     * Location information for the complaint.
     */
    @JsonProperty("localisation")
    private ReclamationCreateRequest.LocalisationRequest localisation;

    /**
     * Description of the location.
     */
    @Size(max = 255, message = "La description du lieu ne peut pas dépasser 255 caractères")
    @JsonProperty("lieuDescription")
    private String lieuDescription;

    /**
     * Specific fields required for the complaint category.
     */
    @JsonProperty("champsSpecifiques")
    private Map<String, Object> champsSpecifiques;

    /**
     * List of file IDs to add to the complaint.
     */
    @JsonProperty("fichiersToAdd")
    private List<String> fichiersToAdd;

    /**
     * List of file IDs to remove from the complaint.
     */
    @JsonProperty("fichiersToRemove")
    private List<String> fichiersToRemove;

    /**
     * Satisfaction rating (for user after resolution).
     */
    @JsonProperty("satisfaction")
    private Integer satisfaction;

    /**
     * Satisfaction comment (for user after resolution).
     */
    @JsonProperty("commentaireSatisfaction")
    private String commentaireSatisfaction;
} 