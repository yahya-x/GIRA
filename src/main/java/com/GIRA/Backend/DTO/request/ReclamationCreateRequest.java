package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for creating a new complaint (reclamation) in the airport complaint system.
 * <p>
 * Contains all necessary information to submit a complaint, including category, title, description,
 * priority, location, custom fields, attached files, and notification preferences.
 * Used for transferring complaint creation data from clients to the backend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReclamationCreateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Unique identifier of the complaint category. Required.
     */
    @NotNull(message = "La catégorie est obligatoire")
    @JsonProperty("categorieId")
    private UUID categorieId;

    /**
     * Unique identifier of the subcategory (optional).
     */
    @JsonProperty("sousCategorieId")
    private UUID sousCategorieId;

    /**
     * Title of the complaint. Required. Must be 5-200 characters.
     */
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 5, max = 200, message = "Le titre doit contenir entre 5 et 200 caractères")
    @JsonProperty("titre")
    private String titre;

    /**
     * Detailed description of the complaint. Required. Must be 10-2000 characters.
     */
    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 2000, message = "La description doit contenir entre 10 et 2000 caractères")
    @JsonProperty("description")
    private String description;

    /**
     * to check After
     * Priority of the complaint. Required. Must be one of: BASSE, NORMALE, HAUTE, URGENTE.
     */
    @NotNull(message = "La priorité est obligatoire")
    @Pattern(regexp = "^(BASSE|NORMALE|HAUTE|URGENTE)$", message = "La priorité doit être BASSE, NORMALE, HAUTE ou URGENTE")
    @JsonProperty("priorite")
    private String priorite;

    

    /**
     * Custom fields for the complaint as key-value pairs (optional).
     */
    @JsonProperty("champsSpecifiques")
    private Map<String, Object> champsSpecifiques;

    /**
     * List of file IDs attached to the complaint (optional).
     */
    @JsonProperty("fichiers")
    private List<UUID> fichiers;

    /**
     * Whether to notify the user by email. Default: true.
     */
    @JsonProperty("notifierParEmail")
    private Boolean notifierParEmail = true;

    /**
     * Whether to notify the user by SMS. Default: false.
     */
    @JsonProperty("notifierParSMS")
    private Boolean notifierParSMS = false;

   
} 