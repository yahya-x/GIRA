package com.GIRA.Backend.DTO.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de réponse pour un fichier.
 * Fournit les informations détaillées d'un fichier pour les réponses API.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Data
@Builder
public class FichierResponse {
    private UUID id;
    private String fileName;
    private String url;
    private String description;
    private String typeMime;
    private UUID reclamationId;
    private UUID uploadedBy;
    private LocalDateTime dateUpload;
} 