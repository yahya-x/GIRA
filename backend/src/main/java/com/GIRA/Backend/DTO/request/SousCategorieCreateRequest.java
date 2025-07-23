package com.GIRA.Backend.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

/**
 * DTO pour la création d'une sous-catégorie.
 * Fournit les champs nécessaires à la création d'une nouvelle sous-catégorie.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
public class SousCategorieCreateRequest {
    /** Nom de la sous-catégorie (obligatoire). */
    @NotBlank(message = "Le nom de la sous-catégorie est obligatoire.")
    private String nom;

    /** Description de la sous-catégorie (optionnelle). */
    private String description;

    /** Identifiant de la catégorie parente (obligatoire). */
    @NotNull(message = "La catégorie parente est obligatoire.")
    private UUID categorieId;

    /** Champs requis (optionnel, format JSON). */
    private String champsRequis;

    /** Statut actif (optionnel, par défaut true). */
    private Boolean actif = true;

    /** Ordre d'affichage (optionnel). */
    private Integer ordreAffichage;
} 