package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.Categorie;
import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.Entities.SousCategorie;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.DTO.request.ReclamationCreateRequest;
import com.GIRA.Backend.DTO.response.ReclamationResponse;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;

public class ReclamationMapper {
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
        
        // Handle champsSpecifiques, fichiers, notifications
        // In real app, these would be processed separately:
        // - champsSpecifiques: validate against sousCategorie requirements
        // - fichiers: create Fichier entities and associate
        // - notifications: create initial notification for submission
        
        return r;
    }

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
        
        // Map files, comments, etc.
        // In real app, these would be fetched and mapped:
        // - files: List<FichierResponse> from fichierRepository.findByReclamation(r)
        // - comments: List<CommentaireResponse> from commentaireRepository.findByReclamation(r)
        // - notifications: List<NotificationResponse> from notificationRepository.findByReclamation(r)
        
        return resp;
    }

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