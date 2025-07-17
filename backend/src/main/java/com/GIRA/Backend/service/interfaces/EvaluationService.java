package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.Evaluation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing evaluations.
 * Provides add, retrieval, advanced queries, and statistics.
 * @author Mohamed Yahya Jabrane
 */
public interface EvaluationService {
    /**
     * Adds a new evaluation.
     * @param evaluation The evaluation entity to add
     * @return The added evaluation entity
     */
    Evaluation addEvaluation(Evaluation evaluation);

    /**
     * Retrieves an evaluation by its ID.
     * @param id The evaluation UUID
     * @return The evaluation entity
     */
    Evaluation getEvaluationById(UUID id);

    /**
     * Retrieves all evaluations for a specific complaint.
     * @param reclamationId The complaint UUID
     * @return List of evaluation entities
     */
    List<Evaluation> getEvaluationsByReclamationId(UUID reclamationId);

    /**
     * Retrieves all evaluations by a specific user.
     * @param evaluateurId The user UUID
     * @return List of evaluation entities
     */
    List<Evaluation> getEvaluationsByEvaluateurId(UUID evaluateurId);

    /**
     * Retrieves all evaluations with a specific global rating.
     * @param noteGlobale The global rating
     * @return List of evaluation entities
     */
    List<Evaluation> getEvaluationsByNoteGlobale(Integer noteGlobale);

    /**
     * Retrieves all evaluations made within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of evaluation entities
     */
    List<Evaluation> getEvaluationsByDateEvaluationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Calculates the average global rating for a complaint.
     * @param reclamationId The complaint UUID
     * @return The average global rating
     */
    Double averageNoteGlobaleByReclamation(UUID reclamationId);

    /**
     * Deletes an evaluation by its ID.
     * @param id The evaluation UUID
     */
    void deleteEvaluation(UUID id);
} 