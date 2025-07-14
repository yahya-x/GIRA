package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.Categorie;
import com.GIRA.Backend.DTO.request.CategorieCreateRequest;
import com.GIRA.Backend.DTO.request.CategorieUpdateRequest;
import com.GIRA.Backend.DTO.response.CategorieResponse;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe utilitaire pour le mapping entre les entités Categorie et les DTOs Categorie.
 * Fournit des méthodes statiques pour convertir entre Categorie, CategorieCreateRequest, CategorieUpdateRequest et CategorieResponse.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
public class CategorieMapper {

    /**
     * Convertit une entité Categorie en CategorieResponse DTO.
     *
     * @param categorie l'entité catégorie
     * @return le DTO de réponse catégorie
     */
    public static CategorieResponse toResponse(Categorie categorie) {
        if (categorie == null) return null;
        return CategorieResponse.builder()
                .id(categorie.getId())
                .nom(categorie.getNom())
                .description(categorie.getDescription())
                .icone(categorie.getIcone())
                .couleur(categorie.getCouleur())
                .parentId(categorie.getParent() != null ? categorie.getParent().getId() : null)
                .actif(categorie.isActif())
                .ordreAffichage(categorie.getOrdreAffichage())
                .dateCreation(categorie.getDateCreation())
                .dateModification(categorie.getDateModification())
                .build();
    }

    /**
     * Convertit un CategorieCreateRequest en entité Categorie.
     *
     * @param request la requête de création de catégorie
     * @param parent la catégorie parente (optionnelle)
     * @return l'entité catégorie
     */
    public static Categorie fromCreateRequest(CategorieCreateRequest request, Categorie parent) {
        if (request == null) return null;
        Categorie categorie = new Categorie();
        categorie.setNom(request.getNom());
        categorie.setDescription(request.getDescription());
        categorie.setIcone(request.getIcone());
        categorie.setCouleur(request.getCouleur());
        categorie.setParent(parent);
        categorie.setActif(request.getActif() != null ? request.getActif() : true);
        categorie.setOrdreAffichage(request.getOrdreAffichage());
        categorie.setDateCreation(LocalDateTime.now());
        return categorie;
    }

    /**
     * Met à jour une entité Categorie à partir d'un CategorieUpdateRequest.
     *
     * @param categorie l'entité catégorie à mettre à jour
     * @param request la requête de mise à jour
     * @param parent la catégorie parente (optionnelle)
     */
    public static void updateCategorieFromRequest(Categorie categorie, CategorieUpdateRequest request, Categorie parent) {
        if (categorie == null || request == null) return;
        if (request.getNom() != null) categorie.setNom(request.getNom());
        if (request.getDescription() != null) categorie.setDescription(request.getDescription());
        if (request.getIcone() != null) categorie.setIcone(request.getIcone());
        if (request.getCouleur() != null) categorie.setCouleur(request.getCouleur());
        if (parent != null) categorie.setParent(parent);
        if (request.getActif() != null) categorie.setActif(request.getActif());
        if (request.getOrdreAffichage() != null) categorie.setOrdreAffichage(request.getOrdreAffichage());
        categorie.setDateModification(LocalDateTime.now());
    }
} 