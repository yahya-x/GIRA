package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Reclamation entities.
 * Provides data access methods for complaints including advanced queries, statistics, and filtering.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, UUID> {
    /**
     * Finds complaints by user ID.
     * @param utilisateurId the user UUID
     * @return list of complaints by the user
     */
    List<Reclamation> findByUtilisateur_Id(UUID utilisateurId);
    /**
     * Finds complaints by assigned agent ID.
     * @param agentId the agent UUID
     * @return list of complaints assigned to the agent
     */
    List<Reclamation> findByAgentAssigne_Id(UUID agentId);
    /**
     * Finds complaints by status.
     * @param statut the complaint status
     * @return list of complaints with the specified status
     */
    List<Reclamation> findByStatut(Reclamation.Statut statut);
    /**
     * Finds complaints by category ID.
     * @param categorieId the category UUID
     * @return list of complaints in the specified category
     */
    List<Reclamation> findByCategorie_Id(UUID categorieId);
    /**
     * Finds complaints by subcategory ID.
     * @param sousCategorieId the subcategory UUID
     * @return list of complaints in the specified subcategory
     */
    List<Reclamation> findBySousCategorie_Id(UUID sousCategorieId);
    /**
     * Finds a complaint by its unique number.
     * @param numero the unique complaint number
     * @return optional containing the complaint if found
     */
    Optional<Reclamation> findByNumero(String numero);
    /**
     * Finds complaints by creation date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return list of complaints created in the date range
     */
    List<Reclamation> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    /**
     * Finds complaints by priority.
     * @param priorite the complaint priority
     * @return list of complaints with the specified priority
     */
    List<Reclamation> findByPriorite(Reclamation.Priorite priorite);
    /**
     * Advanced search with filters and pagination.
     * @param statut the complaint status (optional)
     * @param priorite the complaint priority (optional)
     * @param categorieId the category UUID (optional)
     * @param sousCategorieId the subcategory UUID (optional)
     * @param agentId the agent UUID (optional)
     * @param utilisateurId the user UUID (optional)
     * @param pageable pagination parameters
     * @return page of complaints matching the filters
     */
    @Query("SELECT r FROM Reclamation r WHERE " +
           "(:statut IS NULL OR r.statut = :statut) AND " +
           "(:priorite IS NULL OR r.priorite = :priorite) AND " +
           "(:categorieId IS NULL OR r.categorie.id = :categorieId) AND " +
           "(:sousCategorieId IS NULL OR r.sousCategorie.id = :sousCategorieId) AND " +
           "(:agentId IS NULL OR r.agentAssigne.id = :agentId) AND " +
           "(:utilisateurId IS NULL OR r.utilisateur.id = :utilisateurId)")
    Page<Reclamation> findWithFilters(
        @Param("statut") Reclamation.Statut statut,
        @Param("priorite") Reclamation.Priorite priorite,
        @Param("categorieId") UUID categorieId,
        @Param("sousCategorieId") UUID sousCategorieId,
        @Param("agentId") UUID agentId,
        @Param("utilisateurId") UUID utilisateurId,
        Pageable pageable
    );
    /**
     * Counts complaints by status.
     * @param statut the complaint status
     * @return number of complaints with the specified status
     */
    long countByStatut(Reclamation.Statut statut);
    /**
     * Counts complaints by assigned agent.
     * @param agentId the agent UUID
     * @return number of complaints assigned to the agent
     */
    long countByAgentAssigne_Id(UUID agentId);
    /**
     * Counts complaints by user.
     * @param utilisateurId the user UUID
     * @return number of complaints by the user
     */
    long countByUtilisateur_Id(UUID utilisateurId);
    /**
     * Gets statistics: number of complaints per category.
     * @return list of category name and complaint count pairs
     */
    @Query("SELECT r.categorie.nom, COUNT(r.id) FROM Reclamation r GROUP BY r.categorie.nom")
    List<Object[]> countByCategorie();
    /**
     * Gets statistics: number of complaints per status.
     * @return list of status and complaint count pairs
     */
    @Query("SELECT r.statut, COUNT(r.id) FROM Reclamation r GROUP BY r.statut")
    List<Object[]> countByStatutGroup();
} 