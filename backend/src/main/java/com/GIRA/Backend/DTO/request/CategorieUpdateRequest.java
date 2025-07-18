package com.GIRA.Backend.DTO.request;

import lombok.Data;
import java.util.UUID;

/**
 * DTO pour la mise à jour d'une catégorie.
 * Fournit les champs pouvant être modifiés pour une catégorie existante.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
public class CategorieUpdateRequest {
    /** Nom de la catégorie (optionnel). */
    private String nom;

    /** Description de la catégorie (optionnelle). */
    private String description;

    /** Icône de la catégorie (optionnelle). */
    private String icone;

    /** Couleur de la catégorie (optionnelle). */
    private String couleur;

    /** Identifiant du parent (optionnel, pour la hiérarchie). */
    private UUID parentId;

    /** Statut actif (optionnel). */
    private Boolean actif;

    /** Ordre d'affichage (optionnel). */
    private Integer ordreAffichage;
} 