package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.FeedbackService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackServiceRepository extends JpaRepository<FeedbackService, UUID> {

    // Find feedback by user
    List<FeedbackService> findByUtilisateurId(UUID utilisateurId);

    // Find feedback by type of service
    List<FeedbackService> findByTypeService(String typeService);

    // Find feedback by approval status
    List<FeedbackService> findByApprouve(Boolean approuve);

    // Find feedback by moderation status
    List<FeedbackService> findByModere(Boolean modere);

    // Find feedback by date range
    List<FeedbackService> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Average rating for a service type
    @Query("SELECT AVG(f.note) FROM FeedbackService f WHERE f.typeService = :typeService")
    Double averageNoteByTypeService(@Param("typeService") String typeService);
} 