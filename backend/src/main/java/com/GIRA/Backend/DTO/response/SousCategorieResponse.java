package com.GIRA.Backend.DTO.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de réponse pour une sous-catégorie.
 * Fournit les informations détaillées d'une sous-catégorie pour les réponses API.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
@Builder
public class SousCategorieResponse {
    private UUID id;
    private String nom;
    private String description;
    private UUID categorieId;
    private String champsRequis;
    private Boolean actif;
    private Integer ordreAffichage;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
} 