package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.FeedbackService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing feedback on airport services.
 * Provides add, retrieval, advanced queries, and statistics.
 * @author Mohamed Yahya Jabrane
 */
public interface FeedbackServiceInterface {
    /**
     * Adds new feedback for an airport service.
     * @param feedback The feedback entity to add
     * @return The added feedback entity
     */
    FeedbackService addFeedback(FeedbackService feedback);

    /**
     * Retrieves feedback by its ID.
     * @param id The feedback UUID
     * @return The feedback entity
     */
    FeedbackService getFeedbackById(UUID id);

    /**
     * Retrieves all feedback submitted by a specific user.
     * @param utilisateurId The user UUID
     * @return List of feedback entities
     */
    List<FeedbackService> getFeedbackByUtilisateurId(UUID utilisateurId);

    /**
     * Retrieves all feedback for a specific type of service.
     * @param typeService The type of service
     * @return List of feedback entities
     */
    List<FeedbackService> getFeedbackByTypeService(String typeService);

    /**
     * Retrieves all feedback by approval status.
     * @param approuve Approval status
     * @return List of feedback entities
     */
    List<FeedbackService> getFeedbackByApprouve(Boolean approuve);

    /**
     * Retrieves all feedback by moderation status.
     * @param modere Moderation status
     * @return List of feedback entities
     */
    List<FeedbackService> getFeedbackByModere(Boolean modere);

    /**
     * Retrieves all feedback created within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of feedback entities
     */
    List<FeedbackService> getFeedbackByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Calculates the average rating for a type of service.
     * @param typeService The type of service
     * @return The average rating
     */
    Double averageNoteByTypeService(String typeService);

    /**
     * Deletes feedback by its ID.
     * @param id The feedback UUID
     */
    void deleteFeedback(UUID id);
} 