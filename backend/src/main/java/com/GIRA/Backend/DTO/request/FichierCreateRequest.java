package com.GIRA.Backend.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

/**
 * DTO pour la création d'un fichier (upload).
 * Fournit les champs nécessaires à l'ajout d'un fichier à une réclamation.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
public class FichierCreateRequest {
    /** Identifiant de la réclamation associée (obligatoire). */
    @NotNull(message = "La réclamation associée est obligatoire.")
    private UUID reclamationId;

    /** Description du fichier (optionnelle). */
    private String description;

    /** Type MIME du fichier (optionnel). */
    private String typeMime;
} 