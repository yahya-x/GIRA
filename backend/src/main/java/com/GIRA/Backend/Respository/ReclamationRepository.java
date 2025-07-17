package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
public interface ReclamationRepository extends JpaRepository<Reclamation, UUID>, JpaSpecificationExecutor<Reclamation> {
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
    
    /**
     * Counts complaints by creation date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return number of complaints created in the date range
     */
    long countByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    
    /**
     * Counts complaints by status and creation date range.
     * @param statut the complaint status
     * @param dateDebut start date
     * @param dateFin end date
     * @return number of complaints with the specified status in the date range
     */
    long countByStatutAndDateCreationBetween(Reclamation.Statut statut, LocalDateTime dateDebut, LocalDateTime dateFin);
    
    /**
     * Finds average resolution time in hours.
     * @return average resolution time or null if no resolved complaints
     */
    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (r.date_resolution - r.date_creation))/3600.0) FROM reclamations r WHERE r.date_resolution IS NOT NULL", nativeQuery = true)
    Double findAverageResolutionTime();
    
    /**
     * Finds average resolution time in hours for a specific agent.
     * @param agentId the agent UUID
     * @return average resolution time or null if no resolved complaints
     */
    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (r.date_resolution - r.date_creation))/3600.0) FROM reclamations r WHERE r.agent_assigne_id = :agentId AND r.date_resolution IS NOT NULL", nativeQuery = true)
    Double findAverageResolutionTimeByAgent(@Param("agentId") UUID agentId);
    
    /**
     * Finds average resolution time in hours for a date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return average resolution time or null if no resolved complaints
     */
    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (r.date_resolution - r.date_creation))/3600.0) FROM reclamations r WHERE r.date_creation BETWEEN :dateDebut AND :dateFin AND r.date_resolution IS NOT NULL", nativeQuery = true)
    Double findAverageResolutionTimeBetween(@Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);
    
    /**
     * Counts urgent complaints (priority URGENTE).
     * @return number of urgent complaints
     */
    long countByPriorite(Reclamation.Priorite priorite);
    
    /**
     * Counts urgent complaints for a specific agent.
     * @param agentId the agent UUID
     * @return number of urgent complaints assigned to the agent
     */
    @Query("SELECT COUNT(r) FROM Reclamation r WHERE r.agentAssigne.id = :agentId AND r.priorite = 'URGENTE'")
    long countUrgentReclamationsByAgent(@Param("agentId") UUID agentId);
    
    /**
     * Counts overdue complaints (past due date).
     * @return number of overdue complaints
     */
    @Query("SELECT COUNT(r) FROM Reclamation r WHERE r.dateEcheance < :now AND r.statut NOT IN ('RESOLUE', 'FERMEE')")
    long countOverdueReclamations(@Param("now") LocalDateTime now);
    
    /**
     * Counts overdue complaints for a specific agent.
     * @param agentId the agent UUID
     * @param now current date time
     * @return number of overdue complaints assigned to the agent
     */
    @Query("SELECT COUNT(r) FROM Reclamation r WHERE r.agentAssigne.id = :agentId AND r.dateEcheance < :now AND r.statut NOT IN ('RESOLUE', 'FERMEE')")
    long countOverdueReclamationsByAgent(@Param("agentId") UUID agentId, @Param("now") LocalDateTime now);
    
    /**
     * Gets complaints by priority distribution.
     * @return list of priority and complaint count pairs
     */
    @Query("SELECT r.priorite, COUNT(r.id) FROM Reclamation r GROUP BY r.priorite")
    List<Object[]> countByPrioriteGroup();
    
    /**
     * Gets complaints by priority distribution for a specific agent.
     * @param agentId the agent UUID
     * @return list of priority and complaint count pairs
     */
    @Query("SELECT r.priorite, COUNT(r.id) FROM Reclamation r WHERE r.agentAssigne.id = :agentId GROUP BY r.priorite")
    List<Object[]> countByPrioriteGroupForAgent(@Param("agentId") UUID agentId);
    
    /**
     * Gets complaints by category distribution for a specific agent.
     * @param agentId the agent UUID
     * @return list of category name and complaint count pairs
     */
    @Query("SELECT r.categorie.nom, COUNT(r.id) FROM Reclamation r WHERE r.agentAssigne.id = :agentId GROUP BY r.categorie.nom")
    List<Object[]> countByCategorieForAgent(@Param("agentId") UUID agentId);
    
    /**
     * Gets average resolution time by category.
     * @return list of category name and average resolution time pairs
     */
    @Query(value = "SELECT r.categorie.nom, AVG(EXTRACT(EPOCH FROM (r.date_resolution - r.date_creation))/3600.0) " +
           "FROM reclamations r WHERE r.date_resolution IS NOT NULL GROUP BY r.categorie.nom", nativeQuery = true)
    List<Object[]> findAverageResolutionTimeByCategorie();
    
    /**
     * Gets complaints with satisfaction ratings.
     * @return list of complaints with satisfaction data
     */
    @Query("SELECT r FROM Reclamation r WHERE r.satisfaction IS NOT NULL")
    List<Reclamation> findReclamationsWithSatisfaction();
    
    /**
     * Gets complaints with satisfaction ratings for a specific agent.
     * @param agentId the agent UUID
     * @return list of complaints with satisfaction data
     */
    @Query("SELECT r FROM Reclamation r WHERE r.agentAssigne.id = :agentId AND r.satisfaction IS NOT NULL")
    List<Reclamation> findReclamationsWithSatisfactionByAgent(@Param("agentId") UUID agentId);
    
    /**
     * Gets daily complaint trends for the last 30 days.
     * @return list of date and complaint count pairs
     */
    @Query(value = "SELECT DATE(r.date_creation) as date, COUNT(r.id) as count " +
           "FROM reclamations r " +
           "WHERE r.date_creation >= :startDate " +
           "GROUP BY DATE(r.date_creation) " +
           "ORDER BY date", nativeQuery = true)
    List<Object[]> findDailyTrends(@Param("startDate") LocalDateTime startDate);
    
    /**
     * Gets daily complaint trends for a specific agent for the last 7 days.
     * @param agentId the agent UUID
     * @param startDate start date for trends
     * @return list of date and complaint count pairs
     */
    @Query(value = "SELECT DATE(r.date_creation) as date, COUNT(r.id) as count " +
           "FROM reclamations r " +
           "WHERE r.agent_assigne_id = :agentId AND r.date_creation >= :startDate " +
           "GROUP BY DATE(r.date_creation) " +
           "ORDER BY date", nativeQuery = true)
    List<Object[]> findDailyTrendsByAgent(@Param("agentId") UUID agentId, @Param("startDate") LocalDateTime startDate);
    
    /**
     * Gets urgent complaints list.
     * @return list of urgent complaints
     */
    @Query("SELECT r FROM Reclamation r WHERE r.priorite = 'URGENTE' AND r.statut NOT IN ('RESOLUE', 'FERMEE') ORDER BY r.dateCreation DESC")
    List<Reclamation> findUrgentReclamations();
    
    /**
     * Gets urgent complaints list for a specific agent.
     * @param agentId the agent UUID
     * @return list of urgent complaints assigned to the agent
     */
    @Query("SELECT r FROM Reclamation r WHERE r.agentAssigne.id = :agentId AND r.priorite = 'URGENTE' AND r.statut NOT IN ('RESOLUE', 'FERMEE') ORDER BY r.dateCreation DESC")
    List<Reclamation> findUrgentReclamationsByAgent(@Param("agentId") UUID agentId);
    
    /**
     * Gets overdue complaints list.
     * @param now current date time
     * @return list of overdue complaints
     */
    @Query("SELECT r FROM Reclamation r WHERE r.dateEcheance < :now AND r.statut NOT IN ('RESOLUE', 'FERMEE') ORDER BY r.dateEcheance ASC")
    List<Reclamation> findOverdueReclamations(@Param("now") LocalDateTime now);
    
    /**
     * Gets overdue complaints list for a specific agent.
     * @param agentId the agent UUID
     * @param now current date time
     * @return list of overdue complaints assigned to the agent
     */
    @Query("SELECT r FROM Reclamation r WHERE r.agentAssigne.id = :agentId AND r.dateEcheance < :now AND r.statut NOT IN ('RESOLUE', 'FERMEE') ORDER BY r.dateEcheance ASC")
    List<Reclamation> findOverdueReclamationsByAgent(@Param("agentId") UUID agentId, @Param("now") LocalDateTime now);
} 