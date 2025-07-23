package com.GIRA.Backend.DTO.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de réponse pour une catégorie.
 * Fournit les informations détaillées d'une catégorie pour les réponses API.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
@Builder
public class CategorieResponse {
    private UUID id;
    private String nom;
    private String description;
    private String icone;
    private String couleur;
    private UUID parentId;
    private Boolean actif;
    private Integer ordreAffichage;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
} 