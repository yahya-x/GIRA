package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.EvaluationService;
import com.GIRA.Backend.Respository.EvaluationRepository;
import com.GIRA.Backend.Entities.Evaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of EvaluationService.
 * Provides add, retrieval, advanced queries, and statistics for evaluations.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class EvaluationServiceImpl implements EvaluationService {
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public EvaluationServiceImpl(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    /**
     * Adds a new evaluation.
     * @param evaluation The evaluation entity to add
     * @return The added evaluation entity
     */
    @Override
    public Evaluation addEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    /**
     * Retrieves an evaluation by its ID.
     * @param id The evaluation UUID
     * @return The evaluation entity
     */
    @Override
    public Evaluation getEvaluationById(UUID id) {
        return evaluationRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all evaluations for a specific complaint.
     * @param reclamationId The complaint UUID
     * @return List of evaluation entities
     */
    @Override
    public List<Evaluation> getEvaluationsByReclamationId(UUID reclamationId) {
        return evaluationRepository.findByReclamation_Id(reclamationId);
    }

    /**
     * Retrieves all evaluations by a specific user.
     * @param evaluateurId The user UUID
     * @return List of evaluation entities
     */
    @Override
    public List<Evaluation> getEvaluationsByEvaluateurId(UUID evaluateurId) {
        return evaluationRepository.findByEvaluateur_Id(evaluateurId);
    }

    /**
     * Retrieves all evaluations with a specific global rating.
     * @param noteGlobale The global rating
     * @return List of evaluation entities
     */
    @Override
    public List<Evaluation> getEvaluationsByNoteGlobale(Integer noteGlobale) {
        return evaluationRepository.findByNoteGlobale(noteGlobale);
    }

    /**
     * Retrieves all evaluations made within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of evaluation entities
     */
    @Override
    public List<Evaluation> getEvaluationsByDateEvaluationBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return evaluationRepository.findByDateEvaluationBetween(dateDebut, dateFin);
    }

    /**
     * Calculates the average global rating for a complaint.
     * @param reclamationId The complaint UUID
     * @return The average global rating
     */
    @Override
    public Double averageNoteGlobaleByReclamation(UUID reclamationId) {
        return evaluationRepository.averageNoteGlobaleByReclamation(reclamationId);
    }

    /**
     * Deletes an evaluation by its ID.
     * @param id The evaluation UUID
     */
    @Override
    public void deleteEvaluation(UUID id) {
        evaluationRepository.deleteById(id);
    }
} 