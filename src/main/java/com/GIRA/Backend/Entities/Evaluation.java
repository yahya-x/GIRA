package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * <p>
 * Entity representing an evaluation (feedback) of a complaint's resolution.
 * Stores ratings, comments, and recommendation status.
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "evaluations")
public class Evaluation extends BaseEntity {

    /**
     * The complaint (reclamation) being evaluated.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamation_id", nullable = false)
    private Reclamation reclamation;

    /**
     * The user who performed the evaluation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluateur_id", nullable = false)
    private User evaluateur;

    /**
     * Overall rating (1-5).
     */
    @Column(name = "note_globale", nullable = false)
    private Integer noteGlobale;

    /**
     * Speed rating (1-5).
     */
    @Column(name = "note_rapidite")
    private Integer noteRapidite;

    /**
     * Quality rating (1-5).
     */
    @Column(name = "note_qualite")
    private Integer noteQualite;

    /**
     * Communication rating (1-5).
     */
    @Column(name = "note_communication")
    private Integer noteCommunication;

    /**
     * Resolution rating (1-5).
     */
    @Column(name = "note_resolution")
    private Integer noteResolution;

    /**
     * Indicates whether the evaluation is active (matches 'actif' in DB).
     */
    @Column(name = "actif", nullable = false)
    private boolean actif = true;

    /**
     * Evaluation comment.
     */
    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    /**
     * Date and time when the evaluation was made.
     */
    @Column(name = "date_evaluation", nullable = false)
    private LocalDateTime dateEvaluation;

    /**
     * Date and time when the evaluation was created.
     */
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    /**
     * Date and time when the evaluation was last modified.
     */
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    /**
     * Indicates whether the user recommends the service.
     */
    @Column(name = "recommande")
    private Boolean recommande;

    /**
     * Default constructor.
     */
    public Evaluation() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Creates a new evaluation with the given ratings and comment.
     *
     * @param notes      the ratings
     * @param commentaire the evaluation comment
     */
    public void creerEvaluation(Object notes, String commentaire) {
        // TODO: Implement evaluation creation logic
    }

    /**
     * Calculates the average rating for this evaluation.
     *
     * @return the average rating as a Double
     */
    public Double calculerMoyenne() {
        // TODO: Implement average calculation
        return null;
    }

    /**
     * Generates a report (JSON) for this evaluation.
     *
     * @return JSON representation of the report
     */
    public String genererRapport() {
        // TODO: Implement report generation
        return null;
    }
} 