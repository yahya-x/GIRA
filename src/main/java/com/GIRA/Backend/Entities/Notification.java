package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * <p>
 * Entity representing a notification sent to a user regarding a complaint or system event.
 * Supports multiple notification types and statuses.
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "notifications")
public class Notification extends BaseEntity {

    /**
     * The user who is the recipient of the notification.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private User destinataire;

    /**
     * Type of the notification (EMAIL, PUSH, SMS).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private Type type;

    /**
     * Subject of the notification.
     */
    @Column(name = "sujet", length = 255)
    private String sujet;

    /**
     * Content of the notification.
     */
    @Column(name = "contenu", columnDefinition = "TEXT")
    private String contenu;

    /**
     * Date and time when the notification was sent.
     */
    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    /**
     * Date and time when the notification was read.
     */
    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    /**
     * Status of the notification (EN_ATTENTE, ENVOYE, ECHEC, LU).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private Statut statut;

    /**
     * The complaint (reclamation) related to this notification, if any.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamation_id")
    private Reclamation reclamation;

    /**
     * Date and time when the notification was created.
     */
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    /**
     * Additional context data, stored as JSONB in the database.
     */
    @Column(name = "donnees_contexte", columnDefinition = "jsonb")
    private String donneesContexte;

    /**
     * Additional metadata, stored as JSONB in the database.
     */
    @Column(name = "metadonnees", columnDefinition = "jsonb")
    private String metadonnees;

    /**
     * Default constructor.
     */
    public Notification() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Creates a new notification for a recipient.
     *
     * @param destinataire the recipient user
     * @param type         the notification type
     * @param contenu      the content of the notification
     */
    public void creerNotification(User destinataire, Type type, String contenu) {
        // TODO: Implement notification creation logic
    }

    /**
     * Sends the notification to the recipient.
     */
    public void envoyer() {
        // TODO: Implement send logic
    }

    /**
     * Marks the notification as read.
     */
    public void marquerCommeLue() {
        // TODO: Implement mark as read logic
    }

    /**
     * Schedules the notification to be sent at a specific date and time.
     *
     * @param dateEnvoi the scheduled send date and time
     */
    public void programmer(LocalDateTime dateEnvoi) {
        // TODO: Implement scheduling logic
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
    public void setDateLecture(LocalDateTime dateLecture) {
        this.dateLecture = dateLecture;
    }

    /**
     * Enum representing the type of notification.
     */
    public enum Type {
        EMAIL, PUSH, SMS
    }

    /**
     * Enum representing the status of the notification.
     */
    public enum Statut {
        EN_ATTENTE, ENVOYE, ECHEC, LU
    }
} 