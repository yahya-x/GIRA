package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for notification responses.
 * Contains notification information that can be safely returned to clients.
 *
 * @author Mohamed Yahya Jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
public class NotificationResponse {

    /**
     * Unique identifier of the notification.
     */
    @JsonProperty("id")
    private UUID id;

    /**
     * Recipient user ID.
     */
    @JsonProperty("destinataireId")
    private UUID destinataireId;

    /**
     * Notification type (EMAIL, PUSH, SMS, etc.).
     */
    @JsonProperty("type")
    private String type;

    /**
     * Subject of the notification.
     */
    @JsonProperty("sujet")
    private String sujet;

    /**
     * Content/body of the notification.
     */
    @JsonProperty("contenu")
    private String contenu;

    /**
     * Date and time when the notification was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * Date and time when the notification was sent.
     */
    @JsonProperty("dateEnvoi")
    private LocalDateTime dateEnvoi;

    /**
     * Date and time when the notification was read.
     */
    @JsonProperty("dateLecture")
    private LocalDateTime dateLecture;

    /**
     * Status of the notification (EN_ATTENTE, ENVOYE, ECHEC, LU).
     */
    @JsonProperty("statut")
    private String statut;

    /**
     * Related complaint (reclamation) ID, if applicable.
     */
    @JsonProperty("reclamationId")
    private UUID reclamationId;

    /**
     * Additional metadata as JSON string.
     */
    @JsonProperty("metadonnees")
    private String metadonnees;
} 