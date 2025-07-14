package com.GIRA.Backend.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;

/**
 * DTO pour la création d'une catégorie.
 * Fournit les champs nécessaires à la création d'une nouvelle catégorie.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
public class CategorieCreateRequest {
    /** Nom de la catégorie (obligatoire). */
    @NotBlank(message = "Le nom de la catégorie est obligatoire.")
    private String nom;

    /** Description de la catégorie (optionnelle). */
    private String description;

    /** Icône de la catégorie (optionnelle). */
    private String icone;

    /** Couleur de la catégorie (optionnelle). */
    private String couleur;

    /** Identifiant du parent (optionnel, pour la hiérarchie). */
    private UUID parentId;

    /** Statut actif (optionnel, par défaut true). */
    private Boolean actif = true;

    /** Ordre d'affichage (optionnel). */
    private Integer ordreAffichage;
} 