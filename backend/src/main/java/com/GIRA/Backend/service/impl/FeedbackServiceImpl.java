package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.FeedbackServiceInterface;
import com.GIRA.Backend.Respository.FeedbackServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of FeedbackService.
 * Provides add, retrieval, advanced queries, and statistics for feedback on airport services.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class FeedbackServiceImpl implements FeedbackServiceInterface {
    private final FeedbackServiceRepository feedbackServiceRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackServiceRepository feedbackServiceRepository) {
        this.feedbackServiceRepository = feedbackServiceRepository;
    }

    /**
     * Adds new feedback for an airport service.
     * @param feedback The feedback entity to add
     * @return The added feedback entity
     */
    @Override
    public com.GIRA.Backend.Entities.FeedbackService addFeedback(com.GIRA.Backend.Entities.FeedbackService feedback) {
        return feedbackServiceRepository.save(feedback);
    }

    /**
     * Retrieves feedback by its ID.
     * @param id The feedback UUID
     * @return The feedback entity
     */
    @Override
    public com.GIRA.Backend.Entities.FeedbackService getFeedbackById(UUID id) {
        return feedbackServiceRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all feedback submitted by a specific user.
     * @param utilisateurId The user UUID
     * @return List of feedback entities
     */
    @Override
    public List<com.GIRA.Backend.Entities.FeedbackService> getFeedbackByUtilisateurId(UUID utilisateurId) {
        return feedbackServiceRepository.findByUtilisateurId(utilisateurId);
    }

    /**
     * Retrieves all feedback for a specific type of service.
     * @param typeService The type of service
     * @return List of feedback entities
     */
    @Override
    public List<com.GIRA.Backend.Entities.FeedbackService> getFeedbackByTypeService(String typeService) {
        return feedbackServiceRepository.findByTypeService(typeService);
    }

    /**
     * Retrieves all feedback by approval status.
     * @param approuve Approval status
     * @return List of feedback entities
     */
    @Override
    public List<com.GIRA.Backend.Entities.FeedbackService> getFeedbackByApprouve(Boolean approuve) {
        return feedbackServiceRepository.findByApprouve(approuve);
    }

    /**
     * Retrieves all feedback by moderation status.
     * @param modere Moderation status
     * @return List of feedback entities
     */
    @Override
    public List<com.GIRA.Backend.Entities.FeedbackService> getFeedbackByModere(Boolean modere) {
        return feedbackServiceRepository.findByModere(modere);
    }

    /**
     * Retrieves all feedback created within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of feedback entities
     */
    @Override
    public List<com.GIRA.Backend.Entities.FeedbackService> getFeedbackByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return feedbackServiceRepository.findByDateCreationBetween(dateDebut, dateFin);
    }

    /**
     * Calculates the average rating for a type of service.
     * @param typeService The type of service
     * @return The average rating
     */
    @Override
    public Double averageNoteByTypeService(String typeService) {
        return feedbackServiceRepository.averageNoteByTypeService(typeService);
    }

    /**
     * Deletes feedback by its ID.
     * @param id The feedback UUID
     */
    @Override
    public void deleteFeedback(UUID id) {
        feedbackServiceRepository.deleteById(id);
    }
} 