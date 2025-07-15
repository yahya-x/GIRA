package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.Categorie;
import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.Entities.SousCategorie;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.DTO.request.ReclamationCreateRequest;
import com.GIRA.Backend.DTO.response.ReclamationResponse;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;

/**
 * Utility class for mapping between Reclamation entities and DTOs.
 * Provides static methods for converting between entity and DTO representations.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
public class ReclamationMapper {
    /**
     * Converts a ReclamationCreateRequest DTO to a Reclamation entity.
     *
     * @param req the DTO containing creation data
     * @param user the user submitting the complaint
     * @param categorie the complaint category
     * @param sousCategorie the complaint sub-category
     * @return the mapped Reclamation entity
     */
    public static Reclamation fromCreateRequest(ReclamationCreateRequest req, User user, Categorie categorie, SousCategorie sousCategorie) {
        Reclamation r = new Reclamation();
        r.setCategorie(categorie);
        r.setSousCategorie(sousCategorie);
        r.setTitre(req.getTitre());
        r.setDescription(req.getDescription());
        r.setLocalisation(req.getLocalisation());
        r.setLieuDescription(req.getLieuDescription());
        r.setUtilisateur(user);
        r.setPriorite(r.determinerPrioriteAutomatique());
        r.setStatut(Reclamation.Statut.SOUMISE);
        
        // TODO: Handle champsSpecifiques, files, and notifications
        // - champsSpecifiques: validate against sousCategorie requirements
        // - files: create Fichier entities and associate
        // - notifications: create initial notification for submission
        
        return r;
    }

    /**
     * Converts a Reclamation entity to a ReclamationResponse DTO.
     *
     * @param r the Reclamation entity
     * @return the mapped ReclamationResponse DTO
     */
    public static ReclamationResponse toResponse(Reclamation r) {
        if (r == null) return null;
        ReclamationResponse resp = new ReclamationResponse();
        resp.setId(r.getId() != null ? r.getId().toString() : null);
        resp.setNumero(r.getNumero());
        resp.setTitre(r.getTitre());
        resp.setDescription(r.getDescription());
        resp.setStatut(r.getStatut() != null ? r.getStatut().name() : null);
        resp.setPriorite(r.getPriorite() != null ? r.getPriorite().name() : null);
        resp.setCategorieNom(r.getCategorie() != null ? r.getCategorie().getNom() : null);
        resp.setSousCategorieNom(r.getSousCategorie() != null ? r.getSousCategorie().getNom() : null);
        resp.setDateCreation(r.getDateCreation());
        resp.setDateModification(r.getDateModification());
        resp.setDateResolution(r.getDateResolution());
        resp.setDateEcheance(r.getDateEcheance());
        resp.setLocalisation(r.getLocalisation());
        resp.setLieuDescription(r.getLieuDescription());
        resp.setAssignedAgentNomComplet(r.getAgentAssigne() != null ? r.getAgentAssigne().getNom() + " " + r.getAgentAssigne().getPrenom() : null);
        resp.setSatisfaction(r.getSatisfaction());
        resp.setCommentaireSatisfaction(r.getCommentaireSatisfaction());
        resp.setMetadonnees(r.getMetadonnees());
        
        // TODO: Map files, comments, and notifications if needed
        // - files: List<FichierResponse> from fichierRepository.findByReclamation(r)
        // - comments: List<CommentaireResponse> from commentaireRepository.findByReclamation(r)
        // - notifications: List<NotificationResponse> from notificationRepository.findByReclamation(r)
        
        return resp;
    }

    /**
     * Converts a Reclamation entity to a ReclamationListResponse DTO for list views.
     *
     * @param r the Reclamation entity
     * @return the mapped ReclamationListResponse DTO
     */
    public static ReclamationListResponse toListResponse(Reclamation r) {
        if (r == null) return null;
        return ReclamationListResponse.builder()
                .id(r.getId() != null ? r.getId().toString() : null)
                .numero(r.getNumero())
                .titre(r.getTitre())
                .statut(r.getStatut() != null ? r.getStatut().name() : null)
                .priorite(r.getPriorite() != null ? r.getPriorite().name() : null)
                .categorieNom(r.getCategorie() != null ? r.getCategorie().getNom() : null)
                .dateCreation(r.getDateCreation())
                .dateModification(r.getDateModification())
                .assignedAgentNomComplet(r.getAgentAssigne() != null ? r.getAgentAssigne().getNom() + " " + r.getAgentAssigne().getPrenom() : null)
                .build();
    }
} 