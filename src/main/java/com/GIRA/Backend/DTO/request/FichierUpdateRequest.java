package com.GIRA.Backend.DTO.request;

import lombok.Data;

/**
 * DTO pour la mise à jour des métadonnées d'un fichier.
 * Fournit les champs pouvant être modifiés pour un fichier existant.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
public class FichierUpdateRequest {
    /** Description du fichier (optionnelle). */
    private String description;

    /** Type MIME du fichier (optionnel). */
    private String typeMime;
} 