package com.GIRA.Backend.DTO.request;

import lombok.Data;
import java.util.UUID;

/**
 * DTO pour la mise à jour d'une sous-catégorie.
 * Fournit les champs pouvant être modifiés pour une sous-catégorie existante.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
public class SousCategorieUpdateRequest {
    /** Nom de la sous-catégorie (optionnel). */
    private String nom;

    /** Description de la sous-catégorie (optionnelle). */
    private String description;

    /** Identifiant de la catégorie parente (optionnel). */
    private UUID categorieId;

    /** Champs requis (optionnel, format JSON). */
    private String champsRequis;

    /** Statut actif (optionnel). */
    private Boolean actif;

    /** Ordre d'affichage (optionnel). */
    private Integer ordreAffichage;
} 