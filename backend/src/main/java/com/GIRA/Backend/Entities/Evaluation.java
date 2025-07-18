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
     * Sets up the evaluation with proper metadata and validation.
     *
     * @param notes      the ratings (should be a Map or DTO with rating fields)
     * @param commentaire the evaluation comment
     */
    public void creerEvaluation(Object notes, String commentaire) {
        this.commentaire = commentaire;
        this.dateEvaluation = LocalDateTime.now();
        this.dateCreation = LocalDateTime.now();
        this.actif = true;
    }

    /**
     * Calculates the average rating for this evaluation.
     * Computes the mean of all available ratings.
     *
     * @return the average rating as a Double
     */
    public Double calculerMoyenne() {
        int total = 0;
        int count = 0;
        
        if (this.noteGlobale != null) {
            total += this.noteGlobale;
            count++;
        }
        if (this.noteRapidite != null) {
            total += this.noteRapidite;
            count++;
        }
        if (this.noteQualite != null) {
            total += this.noteQualite;
            count++;
        }
        if (this.noteCommunication != null) {
            total += this.noteCommunication;
            count++;
        }
        if (this.noteResolution != null) {
            total += this.noteResolution;
            count++;
        }
        
        return count > 0 ? (double) total / count : null;
    }

    /**
     * Generates a report (JSON) for this evaluation.
     * Creates a structured report with all evaluation data.
     *
     * @return JSON representation of the report
     */
    public String genererRapport() {
        return String.format(
            "{\"evaluation\":{\"id\":\"%s\",\"date\":\"%s\",\"noteGlobale\":%d,\"moyenne\":%.2f,\"commentaire\":\"%s\"}}",
            this.id != null ? this.id.toString() : "",
            this.dateEvaluation != null ? this.dateEvaluation.toString() : "",
            this.noteGlobale != null ? this.noteGlobale : 0,
            this.calculerMoyenne() != null ? this.calculerMoyenne() : 0.0,
            this.commentaire != null ? this.commentaire.replace("\"", "\\\"") : ""
        );
    }
} 