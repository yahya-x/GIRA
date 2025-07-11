package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing complaints (reclamations).
 * Provides creation, update, assignment, status changes, advanced queries, and statistics.
 * @author Mohamed Yahya Jabrane
 */
public interface ReclamationService {
    /**
     * Creates a new complaint.
     * @param reclamation The complaint entity to create
     * @return The created complaint entity
     */
    Reclamation createReclamation(Reclamation reclamation);

    /**
     * Retrieves a complaint by ID.
     * @param id The complaint UUID
     * @return Optional containing the complaint entity if found
     */
    Optional<Reclamation> getReclamationById(UUID id);

    /**
     * Retrieves all complaints.
     * @return List of complaint entities
     */
    List<Reclamation> getAllReclamations();

    /**
     * Updates a complaint.
     * @param id The complaint UUID
     * @param reclamation The updated complaint data
     * @return The updated complaint entity
     */
    Reclamation updateReclamation(UUID id, Reclamation reclamation);

    /**
     * Deletes a complaint by ID.
     * @param id The complaint UUID
     */
    void deleteReclamation(UUID id);

    /**
     * Finds complaints by user ID.
     * @param utilisateurId The user UUID
     * @return List of complaint entities
     */
    List<Reclamation> findByUtilisateurId(UUID utilisateurId);

    /**
     * Finds complaints by assigned agent ID.
     * @param agentId The agent UUID
     * @return List of complaint entities
     */
    List<Reclamation> findByAgentAssigneId(UUID agentId);

    /**
     * Finds complaints by status.
     * @param statut The complaint status
     * @return List of complaint entities
     */
    List<Reclamation> findByStatut(Reclamation.Statut statut);

    /**
     * Finds complaints by category ID.
     * @param categorieId The category UUID
     * @return List of complaint entities
     */
    List<Reclamation> findByCategorieId(UUID categorieId);

    /**
     * Finds complaints by subcategory ID.
     * @param sousCategorieId The subcategory UUID
     * @return List of complaint entities
     */
    List<Reclamation> findBySousCategorieId(UUID sousCategorieId);

    /**
     * Finds a complaint by its unique number.
     * @param numero The unique complaint number
     * @return Optional containing the complaint entity if found
     */
    Optional<Reclamation> findByNumero(String numero);

    /**
     * Finds complaints by creation date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of complaint entities
     */
    List<Reclamation> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Finds complaints by priority.
     * @param priorite The complaint priority
     * @return List of complaint entities
     */
    List<Reclamation> findByPriorite(Reclamation.Priorite priorite);

    /**
     * Advanced search with filters and pagination.
     * @param statut The complaint status
     * @param priorite The complaint priority
     * @param categorieId The category UUID
     * @param sousCategorieId The subcategory UUID
     * @param agentId The agent UUID
     * @param utilisateurId The user UUID
     * @param pageable Pagination parameters
     * @return Page of complaint entities
     */
    Page<Reclamation> findWithFilters(Reclamation.Statut statut, Reclamation.Priorite priorite, UUID categorieId, UUID sousCategorieId, UUID agentId, UUID utilisateurId, Pageable pageable);

    /**
     * Counts complaints by status.
     * @param statut The complaint status
     * @return Number of complaints
     */
    long countByStatut(Reclamation.Statut statut);

    /**
     * Counts complaints by agent.
     * @param agentId The agent UUID
     * @return Number of complaints
     */
    long countByAgentAssigneId(UUID agentId);

    /**
     * Counts complaints by user.
     * @param utilisateurId The user UUID
     * @return Number of complaints
     */
    long countByUtilisateurId(UUID utilisateurId);

    /**
     * Gets statistics: number of complaints per category.
     * @return List of category name and complaint count pairs
     */
    List<Object[]> countByCategorie();

    /**
     * Gets statistics: number of complaints per status.
     * @return List of status and complaint count pairs
     */
    List<Object[]> countByStatutGroup();
} 