package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.Commentaire;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing comments (commentaires).
 * Provides add, retrieval, advanced queries, and statistics.
 * @author Mohamed Yahya Jabrane
 */
public interface CommentaireService {
    /**
     * Adds a new comment to a complaint.
     * @param commentaire The comment entity to add
     * @return The added comment entity
     */
    Commentaire addComment(Commentaire commentaire);

    /**
     * Retrieves a comment by its ID.
     * @param id The comment UUID
     * @return The comment entity
     */
    Commentaire getCommentById(UUID id);

    /**
     * Retrieves all comments for a specific complaint.
     * @param reclamationId The complaint UUID
     * @return List of comment entities
     */
    List<Commentaire> getCommentsByReclamationId(UUID reclamationId);

    /**
     * Retrieves all comments by a specific author.
     * @param auteurId The author UUID
     * @return List of comment entities
     */
    List<Commentaire> getCommentsByAuteurId(UUID auteurId);

    /**
     * Retrieves all comments of a specific type.
     * @param type The comment type
     * @return List of comment entities
     */
    List<Commentaire> getCommentsByType(String type);

    /**
     * Retrieves all comments created within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of comment entities
     */
    List<Commentaire> getCommentsByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Counts the number of comments for a specific complaint.
     * @param reclamationId The complaint UUID
     * @return Number of comments
     */
    long countByReclamationId(UUID reclamationId);

    /**
     * Searches comments by content (case-insensitive).
     * @param contenu The content to search for
     * @return List of comment entities
     */
    List<Commentaire> findByContenuContainingIgnoreCase(String contenu);

    /**
     * Deletes a comment by its ID.
     * @param id The comment UUID
     */
    void deleteComment(UUID id);
} 