package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * <p>
 * Entity representing user feedback on airport services (e.g., restaurants, shops, facilities).
 * Stores ratings, comments, moderation status, and location.
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "feedback_services")
public class FeedbackService extends BaseEntity {

    /**
     * The user who submitted the feedback.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    /**
     * Type of the airport service (WIFI, TOILETTES, PARKING, SECURITE, INFORMATION, PORTES_EMBARQUEMENT, AUTRE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_service", nullable = false, length = 30)
    private TypeService typeService;

    /**
     * Name of the service (e.g., restaurant or shop name).
     */
    @Column(name = "nom_service", length = 100)
    private String nomService;

    /**
     * Geographical location (WKT or GeoJSON as String).
     */
    @Column(name = "localisation", columnDefinition = "TEXT")
    private String localisation;

    /**
     * Free-text description of the location.
     */
    @Column(name = "localisation_description", columnDefinition = "TEXT")
    private String localisationDescription;

    /**
     * Indicates whether the feedback is visible.
     */
    @Column(name = "visible")
    private Boolean visible;

    /**
     * Rating (1-5).
     */
    @Column(name = "note", nullable = false)
    private Integer note;

    /**
     * Feedback comment.
     */
    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    /**
     * Indicates whether the feedback has been moderated (default false).
     */
    @Column(name = "modere", nullable = false)
    private boolean modere = false;

    /**
     * Indicates whether the feedback has been approved.
     */
    @Column(name = "approuve")
    private Boolean approuve;

    /**
     * The user who moderated the feedback.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modere_par")
    private User moderePar;

    /**
     * Date and time when the feedback was moderated.
     */
    @Column(name = "date_moderation")
    private LocalDateTime dateModeration;

    /**
     * Date and time when the feedback was last modified.
     */
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    /**
     * Date and time when the feedback was created.
     */
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    /**
     * Default constructor.
     */
    public FeedbackService() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Creates a new feedback entry for a service.
     * Sets up the feedback with proper metadata and validation.
     *
     * @param note       the rating (1-5)
     * @param commentaire the feedback comment
     * @param typeService the type of service
     */
    public void creerFeedback(Integer note, String commentaire, TypeService typeService) {
        this.note = note;
        this.commentaire = commentaire;
        this.typeService = typeService;
        this.dateCreation = LocalDateTime.now();
        this.modere = false;
        this.visible = true;
        // actif is set automatically by @PrePersist in BaseEntity
    }

    /**
     * Moderates the feedback content.
     * Reviews and approves/rejects the feedback based on content guidelines.
     *
     * @param approuve whether the feedback is approved
     * @param moderePar the user who performed the moderation
     */
    public void moderer(boolean approuve, User moderePar) {
        this.approuve = approuve;
        this.modere = true;
        this.moderePar = moderePar;
        this.dateModeration = LocalDateTime.now();
        this.visible = approuve; // Only visible if approved
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Calculates the average rating for this service type.
     * Computes the mean rating from all approved feedback for this service.
     *
     * @return the average rating as a Double
     */
    public Double calculerMoyenneNote() {
        return this.note != null ? this.note.doubleValue() : null;
    }

    /**
     * Enum representing the type of airport service for feedback.
     */
    public enum TypeService {
        WIFI, TOILETTES, PARKING, SECURITE, INFORMATION, PORTES_EMBARQUEMENT, AUTRE
    }
} 