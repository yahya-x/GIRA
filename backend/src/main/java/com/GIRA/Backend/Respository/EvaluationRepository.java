package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Evaluation entities.
 * Provides data access methods for complaint evaluations and feedback.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {
    /**
     * Finds evaluations by complaint ID.
     * @param reclamationId the complaint UUID
     * @return list of evaluations for the complaint
     */
    List<Evaluation> findByReclamation_Id(UUID reclamationId);
    /**
     * Finds evaluations by user ID.
     * @param evaluateurId the user UUID
     * @return list of evaluations by the user
     */
    List<Evaluation> findByEvaluateurId(UUID evaluateurId);
    /**
     * Finds evaluations by overall rating.
     * @param noteGlobale the global rating
     * @return list of evaluations with the specified rating
     */
    List<Evaluation> findByNoteGlobale(Integer noteGlobale);
    /**
     * Finds evaluations by evaluation date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return list of evaluations in the date range
     */
    List<Evaluation> findByDateEvaluationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    /**
     * Calculates the average global rating for a complaint.
     * @param reclamationId the complaint UUID
     * @return average global rating
     */
    @Query("SELECT AVG(e.noteGlobale) FROM Evaluation e WHERE e.reclamation.id = :reclamationId")
    Double averageNoteGlobaleByReclamation(@Param("reclamationId") UUID reclamationId);
    /**
     * Finds evaluations by user ID (alternative method).
     * @param evaluateurId the user UUID
     * @return list of evaluations by the user
     */
    List<Evaluation> findByEvaluateur_Id(UUID evaluateurId);
} 