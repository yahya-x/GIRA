package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.FeedbackService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing FeedbackService entities.
 * Provides data access methods for airport service feedback and ratings.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface FeedbackServiceRepository extends JpaRepository<FeedbackService, UUID> {
    /**
     * Finds feedback by user ID.
     * @param utilisateurId the user UUID
     * @return list of feedback submitted by the user
     */
    List<FeedbackService> findByUtilisateurId(UUID utilisateurId);
    /**
     * Finds feedback by type of service.
     * @param typeService the type of service
     * @return list of feedback for the specified service type
     */
    List<FeedbackService> findByTypeService(String typeService);
    /**
     * Finds feedback by approval status.
     * @param approuve the approval status
     * @return list of feedback with the specified approval status
     */
    List<FeedbackService> findByApprouve(Boolean approuve);
    /**
     * Finds feedback by moderation status.
     * @param modere the moderation status
     * @return list of feedback with the specified moderation status
     */
    List<FeedbackService> findByModere(Boolean modere);
    /**
     * Finds feedback by creation date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return list of feedback created in the date range
     */
    List<FeedbackService> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    /**
     * Calculates the average rating for a service type.
     * @param typeService the type of service
     * @return average rating for the service type
     */
    @Query("SELECT AVG(f.note) FROM FeedbackService f WHERE f.typeService = :typeService")
    Double averageNoteByTypeService(@Param("typeService") String typeService);
} 