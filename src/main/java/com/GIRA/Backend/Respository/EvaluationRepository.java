package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {

    // Find evaluations by complaint
    List<Evaluation> findByReclamation_Id(UUID reclamationId);

    // Find evaluations by user
    List<Evaluation> findByEvaluateurId(UUID evaluateurId);

    // Find evaluations by rating
    List<Evaluation> findByNoteGlobale(Integer noteGlobale);

    // Find evaluations by date range
    List<Evaluation> findByDateEvaluationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Average rating for a complaint
    @Query("SELECT AVG(e.noteGlobale) FROM Evaluation e WHERE e.reclamation.id = :reclamationId")
    Double averageNoteGlobaleByReclamation(@Param("reclamationId") UUID reclamationId);
} 