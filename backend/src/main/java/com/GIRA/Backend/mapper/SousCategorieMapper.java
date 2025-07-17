package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.SousCategorie;
import com.GIRA.Backend.Entities.Categorie;
import com.GIRA.Backend.DTO.request.SousCategorieCreateRequest;
import com.GIRA.Backend.DTO.request.SousCategorieUpdateRequest;
import com.GIRA.Backend.DTO.response.SousCategorieResponse;
import java.time.LocalDateTime;

/**
 * Classe utilitaire pour le mapping entre les entités SousCategorie et les DTOs SousCategorie.
 * Fournit des méthodes statiques pour convertir entre SousCategorie, SousCategorieCreateRequest, SousCategorieUpdateRequest et SousCategorieResponse.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
public class SousCategorieMapper {

    /**
     * Convertit une entité SousCategorie en SousCategorieResponse DTO.
     *
     * @param sousCategorie l'entité sous-catégorie
     * @return le DTO de réponse sous-catégorie
     */
    public static SousCategorieResponse toResponse(SousCategorie sousCategorie) {
        if (sousCategorie == null) return null;
        return SousCategorieResponse.builder()
                .id(sousCategorie.getId())
                .nom(sousCategorie.getNom())
                .description(sousCategorie.getDescription())
                .categorieId(sousCategorie.getCategorie() != null ? sousCategorie.getCategorie().getId() : null)
                .champsRequis(sousCategorie.getChampsRequis())
                .actif(sousCategorie.isActif())
                .ordreAffichage(sousCategorie.getOrdreAffichage())
                .dateCreation(sousCategorie.getDateCreation())
                .dateModification(sousCategorie.getDateModification())
                .build();
    }

    /**
     * Convertit un SousCategorieCreateRequest en entité SousCategorie.
     *
     * @param request la requête de création de sous-catégorie
     * @param categorie la catégorie parente (obligatoire)
     * @return l'entité sous-catégorie
     */
    public static SousCategorie fromCreateRequest(SousCategorieCreateRequest request, Categorie categorie) {
        if (request == null) return null;
        SousCategorie sousCategorie = new SousCategorie();
        sousCategorie.setNom(request.getNom());
        sousCategorie.setDescription(request.getDescription());
        sousCategorie.setCategorie(categorie);
        sousCategorie.setChampsRequis(request.getChampsRequis());
        sousCategorie.setActif(request.getActif() != null ? request.getActif() : true);
        sousCategorie.setOrdreAffichage(request.getOrdreAffichage());
        sousCategorie.setDateCreation(LocalDateTime.now());
        return sousCategorie;
    }

    /**
     * Met à jour une entité SousCategorie à partir d'un SousCategorieUpdateRequest.
     *
     * @param sousCategorie l'entité sous-catégorie à mettre à jour
     * @param request la requête de mise à jour
     * @param categorie la catégorie parente (optionnelle)
     */
    public static void updateSousCategorieFromRequest(SousCategorie sousCategorie, SousCategorieUpdateRequest request, Categorie categorie) {
        if (sousCategorie == null || request == null) return;
        if (request.getNom() != null) sousCategorie.setNom(request.getNom());
        if (request.getDescription() != null) sousCategorie.setDescription(request.getDescription());
        if (categorie != null) sousCategorie.setCategorie(categorie);
        if (request.getChampsRequis() != null) sousCategorie.setChampsRequis(request.getChampsRequis());
        if (request.getActif() != null) sousCategorie.setActif(request.getActif());
        if (request.getOrdreAffichage() != null) sousCategorie.setOrdreAffichage(request.getOrdreAffichage());
        sousCategorie.setDateModification(LocalDateTime.now());
    }
} 