package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.Fichier;
import com.GIRA.Backend.DTO.request.FichierCreateRequest;
import com.GIRA.Backend.DTO.request.FichierUpdateRequest;
import com.GIRA.Backend.DTO.response.FichierResponse;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe utilitaire pour le mapping entre les entités Fichier et les DTOs Fichier.
 * Fournit des méthodes statiques pour convertir entre Fichier, FichierCreateRequest, FichierUpdateRequest et FichierResponse.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
public class FichierMapper {

    /**
     * Convertit une entité Fichier en FichierResponse DTO.
     *
     * @param fichier l'entité fichier
     * @return le DTO de réponse fichier
     */
    public static FichierResponse toResponse(Fichier fichier) {
        if (fichier == null) return null;
        return FichierResponse.builder()
                .id(fichier.getId())
                .fileName(fichier.getNomOriginal())
                .url(null) // Or fichier.genererUrlSignee(3600L) if you want a signed URL
                .typeMime(fichier.getTypeMime())
                .reclamationId(fichier.getReclamation() != null ? fichier.getReclamation().getId() : null)
                .uploadedBy(fichier.getUploadePar() != null ? fichier.getUploadePar().getId() : null)
                .dateUpload(fichier.getDateUpload())
                .build();
    }

    /**
     * Convertit un FichierCreateRequest en entité Fichier.
     *
     * @param request la requête de création de fichier
     * @param reclamationId l'identifiant de la réclamation associée
     * @param uploadedById l'identifiant de l'utilisateur qui upload
     * @return l'entité fichier
     */
    public static Fichier fromCreateRequest(FichierCreateRequest request, UUID reclamationId, UUID uploadedById) {
        if (request == null) return null;
        Fichier fichier = new Fichier();
        fichier.setTypeMime(request.getTypeMime());
        fichier.setDateUpload(LocalDateTime.now());
        // L'association à la réclamation et à l'uploadeur doit être faite dans le service
        return fichier;
    }

    /**
     * Met à jour une entité Fichier à partir d'un FichierUpdateRequest.
     *
     * @param fichier l'entité fichier à mettre à jour
     * @param request la requête de mise à jour
     */
    public static void updateFichierFromRequest(Fichier fichier, FichierUpdateRequest request) {
        if (fichier == null || request == null) return;
        if (request.getTypeMime() != null) fichier.setTypeMime(request.getTypeMime());
    }
} 