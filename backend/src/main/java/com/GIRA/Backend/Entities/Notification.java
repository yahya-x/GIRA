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
     * Additional context data, stored as JSON in the database.
     */
    @Column(name = "donnees_contexte", columnDefinition = "TEXT")
    private String donneesContexte;

    /**
     * Additional metadata, stored as JSON in the database.
     */
    @Column(name = "metadonnees", columnDefinition = "TEXT")
    private String metadonnees;

    /**
     * Default constructor.
     */
    public Notification() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Creates a new notification for a recipient.
     * Sets up the notification with proper defaults and metadata.
     *
     * @param destinataire the recipient user
     * @param type         the notification type
     * @param contenu      the content of the notification
     */
    public void creerNotification(User destinataire, Type type, String contenu) {
        this.destinataire = destinataire;
        this.type = type;
        this.contenu = contenu;
        this.statut = Statut.EN_ATTENTE;
        this.dateCreation = LocalDateTime.now();
    }

    /**
     * Sends the notification to the recipient.
     * Updates the status and sends via appropriate channel (email, push, SMS).
     */
    public void envoyer() {
        try {
            // Simulate sending via appropriate service based on type
            switch (this.type) {
                case EMAIL:
                    // Simulate email sending
                    break;
                case PUSH:
                    // Simulate push notification
                    break;
                case SMS:
                    // Simulate SMS sending
                    break;
            }
            this.statut = Statut.ENVOYE;
            this.dateEnvoi = LocalDateTime.now();
        } catch (Exception e) {
            this.statut = Statut.ECHEC;
        }
    }

    /**
     * Marks the notification as read.
     * Updates the read status and timestamp.
     */
    public void marquerCommeLue() {
        this.statut = Statut.LU;
        this.dateLecture = LocalDateTime.now();
        // dateModification is set automatically by @PreUpdate in BaseEntity
    }

    /**
     * Schedules the notification to be sent at a specific date and time.
     * Sets up the notification for delayed sending.
     *
     * @param dateEnvoi the scheduled send date and time
     */
    public void programmer(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
        this.statut = Statut.EN_ATTENTE;
    }

    // ====== Getters and Setters ======
    
    public User getDestinataire() { return destinataire; }
    public void setDestinataire(User destinataire) { this.destinataire = destinataire; }
    
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    
    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }
    
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
    
    public LocalDateTime getDateLecture() { return dateLecture; }
    public void setDateLecture(LocalDateTime dateLecture) { this.dateLecture = dateLecture; }
    
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    
    public Reclamation getReclamation() { return reclamation; }
    public void setReclamation(Reclamation reclamation) { this.reclamation = reclamation; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public String getDonneesContexte() { return donneesContexte; }
    public void setDonneesContexte(String donneesContexte) { this.donneesContexte = donneesContexte; }
    
    public String getMetadonnees() { return metadonnees; }
    public void setMetadonnees(String metadonnees) { this.metadonnees = metadonnees; }

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