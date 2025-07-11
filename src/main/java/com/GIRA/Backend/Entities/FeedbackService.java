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
     *
     * @param service     the service name
     * @param note        the rating
     * @param commentaire the feedback comment
     */
    public void creerFeedback(String service, Integer note, String commentaire) {
        // TODO: Implement feedback creation logic
    }

    /**
     * Moderates the feedback (approve or reject).
     *
     * @param decision   the moderation decision
     * @param moderateur the user who moderates
     */
    public void moderer(Boolean decision, User moderateur) {
        // TODO: Implement moderation logic
    }

    /**
     * Calculates the average rating for a given service type.
     *
     * @param typeService the type of service
     * @return the average rating as a Double
     */
    public static Double calculerNoteMoyenne(TypeService typeService) {
        // TODO: Implement average rating calculation
        return null;
    }

    /**
     * Enum representing the type of airport service for feedback.
     */
    public enum TypeService {
        WIFI, TOILETTES, PARKING, SECURITE, INFORMATION, PORTES_EMBARQUEMENT, AUTRE
    }
} 