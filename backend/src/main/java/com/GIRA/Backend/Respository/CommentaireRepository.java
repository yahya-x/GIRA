package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Commentaire entities.
 * Provides data access methods for complaint comments and discussions.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, UUID> {
    /**
     * Finds comments by complaint ID.
     * @param reclamationId the complaint UUID
     * @return list of comments for the complaint
     */
    List<Commentaire> findByReclamation_Id(UUID reclamationId);
    /**
     * Finds comments by author ID.
     * @param auteurId the author UUID
     * @return list of comments by the author
     */
    List<Commentaire> findByAuteurId(UUID auteurId);
    /**
     * Finds comments by type.
     * @param type the comment type
     * @return list of comments with the specified type
     */
    List<Commentaire> findByType(String type);
    /**
     * Finds comments by creation date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return list of comments created in the date range
     */
    List<Commentaire> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    /**
     * Counts comments by complaint ID.
     * @param reclamationId the complaint UUID
     * @return number of comments for the complaint
     */
    long countByReclamation_Id(UUID reclamationId);
    /**
     * Finds comments by content (case-insensitive search).
     * @param contenu the content to search for
     * @return list of comments containing the specified content
     */
    @Query("SELECT c FROM Commentaire c WHERE LOWER(c.contenu) LIKE LOWER(CONCAT('%', :contenu, '%'))")
    List<Commentaire> findByContenuContainingIgnoreCase(@Param("contenu") String contenu);
    /**
     * Finds comments by author ID (alternative method).
     * @param auteurId the author UUID
     * @return list of comments by the author
     */
    List<Commentaire> findByAuteur_Id(UUID auteurId);
} 