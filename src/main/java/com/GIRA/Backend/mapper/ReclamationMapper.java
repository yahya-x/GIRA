package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.Categorie;
import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.Entities.SousCategorie;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.DTO.request.ReclamationCreateRequest;
import com.GIRA.Backend.DTO.response.ReclamationResponse;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;
import com.GIRA.Backend.DTO.response.FichierResponse;
import com.GIRA.Backend.DTO.response.CommentaireResponse;
import com.GIRA.Backend.DTO.response.NotificationResponse;
import com.GIRA.Backend.Entities.Fichier;
import com.GIRA.Backend.Entities.Commentaire;
import com.GIRA.Backend.Entities.Notification;
import com.GIRA.Backend.service.interfaces.FichierService;
import com.GIRA.Backend.service.interfaces.CommentaireService;
import com.GIRA.Backend.service.interfaces.NotificationService;
import java.util.List;
import java.util.stream.Collectors;

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
     * Converts a Reclamation entity to a ReclamationResponse DTO, including files, comments, and notifications.
     *
     * @param r the Reclamation entity
     * @param fichierService the file service
     * @param commentaireService the comment service
     * @param notificationService the notification service
     * @return the mapped ReclamationResponse DTO
     */
    public static ReclamationResponse toResponse(Reclamation r, FichierService fichierService, CommentaireService commentaireService, NotificationService notificationService) {
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
        // === Files ===
        List<FichierResponse> fichiers = fichierService.getFilesByReclamationId(r.getId())
            .stream().map(f -> FichierResponse.builder()
                .id(f.getId())
                .fileName(f.getNomOriginal())
                .url(f.getUrl())
                .description(f.getDescription())
                .typeMime(f.getTypeMime())
                .reclamationId(f.getReclamation() != null ? f.getReclamation().getId() : null)
                .uploadedBy(f.getUploadePar() != null ? f.getUploadePar().getId() : null)
                .dateUpload(f.getDateUpload())
                .build())
            .collect(java.util.stream.Collectors.toList());
        // Add to response if field exists
        try { java.lang.reflect.Field field = resp.getClass().getDeclaredField("fichiers"); field.setAccessible(true); field.set(resp, fichiers); } catch (Exception ignored) {}
        // === Comments ===
        List<CommentaireResponse> commentaires = commentaireService.getCommentsByReclamationId(r.getId())
            .stream().map(c -> CommentaireResponse.builder()
                .id(c.getId() != null ? c.getId().toString() : null)
                .reclamationId(r.getId() != null ? r.getId().toString() : null)
                .contenu(c.getContenu())
                .type(c.getType())
                .dateCreation(c.getDateCreation())
                .dateModification(c.getDateModification())
                .lu(c.getLu())
                .dateMarkageLu(c.getDateMarkageLu())
                .auteurId(c.getAuteur() != null ? c.getAuteur().getId().toString() : null)
                .modifiePar(c.getModifiePar() != null ? c.getModifiePar().toString() : null)
                .build())
            .collect(java.util.stream.Collectors.toList());
        try { java.lang.reflect.Field field = resp.getClass().getDeclaredField("commentaires"); field.setAccessible(true); field.set(resp, commentaires); } catch (Exception ignored) {}
        // === Notifications ===
        List<NotificationResponse> notifications = notificationService.findByReclamationId(r.getId())
            .stream().map(n -> {
                NotificationResponse nr = new NotificationResponse();
                nr.setId(n.getId());
                nr.setDestinataireId(n.getDestinataire() != null ? n.getDestinataire().getId() : null);
                nr.setType(n.getType() != null ? n.getType().name() : null);
                nr.setSujet(n.getSujet());
                nr.setContenu(n.getContenu());
                nr.setDateCreation(n.getDateCreation());
                nr.setDateEnvoi(n.getDateEnvoi());
                nr.setDateLecture(n.getDateLecture());
                nr.setStatut(n.getStatut() != null ? n.getStatut().name() : null);
                nr.setReclamationId(r.getId());
                nr.setMetadonnees(n.getMetadonnees());
                return nr;
            })
            .collect(Collectors.toList());
        try { java.lang.reflect.Field field = resp.getClass().getDeclaredField("notifications"); field.setAccessible(true); field.set(resp, notifications); } catch (Exception ignored) {}
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