package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * <p>
 * Entity representing the history (audit trail) of actions performed on a complaint (reclamation).
 * Tracks changes, actions, and user activity for traceability.
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "historiques")
public class Historique extends BaseEntity {

    /**
     * The complaint (reclamation) to which this history entry belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamation_id", nullable = false)
    private Reclamation reclamation;

    /**
     * The user who performed the action.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    /**
     * Action performed (e.g., CREATION, CHANGEMENT_STATUT, ASSIGNATION, etc.).
     */
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    /**
     * Previous value (before the action), stored as JSON in the database.
     */
    @Column(name = "ancienne_valeur", columnDefinition = "TEXT")
    private String ancienneValeur;

    /**
     * New value (after the action), stored as JSON in the database.
     */
    @Column(name = "nouvelle_valeur", columnDefinition = "TEXT")
    private String nouvelleValeur;

    /**
     * Date and time when the action was performed.
     */
    @Column(name = "date_action", nullable = false)
    private LocalDateTime dateAction;

    /**
     * Optional comment about the action.
     */
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    /**
     * IP address from which the action was performed.
     */
    @Column(name = "ip_adresse", length = 45)
    private String ipAdresse;

    /**
     * Default constructor.
     */
    public Historique() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Records a new action in the history.
     * Creates a new history entry with proper metadata and context.
     *
     * @param action  the action performed
     * @param donnees the data related to the action
     */
    public void enregistrerAction(String action, Object donnees) {
        this.action = action;
        this.dateAction = LocalDateTime.now();
        // dateCreation is set automatically by @PrePersist in BaseEntity
        
        // Convert donnees to JSON string for storage
        if (donnees != null) {
            this.nouvelleValeur = donnees.toString();
        }
    }

    /**
     * Retrieves the history for a given complaint.
     * Static method to get all history entries for a specific complaint.
     *
     * @param reclamationId the complaint ID
     * @return list of Historique entries
     */
    public static Object obtenirHistorique(Long reclamationId) {
        // In real app, this would be handled by HistoriqueService
        return null; // Placeholder
    }

    /**
     * Generates a timeline (JSON) of the complaint's history.
     * Creates a structured timeline of all actions performed on the complaint.
     *
     * @return JSON representation of the timeline
     */
    public String genererTimeline() {
        return String.format(
            "{\"timeline\":[{\"date\":\"%s\",\"action\":\"%s\",\"user\":\"%s\",\"details\":\"%s\"}]}",
            this.dateAction != null ? this.dateAction.toString() : "",
            this.action != null ? this.action : "",
            this.utilisateur != null ? this.utilisateur.getNom() : "",
            this.commentaire != null ? this.commentaire : ""
        );
    }
} 