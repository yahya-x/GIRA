package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Historique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Historique entities.
 * Provides data access methods for complaint history and audit trails.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface HistoriqueRepository extends JpaRepository<Historique, UUID> {
    /**
     * Finds history records by complaint ID.
     * @param reclamationId the complaint UUID
     * @return list of history records for the complaint
     */
    List<Historique> findByReclamation_Id(UUID reclamationId);
    /**
     * Finds history records by user ID.
     * @param utilisateurId the user UUID
     * @return list of history records by the user
     */
    List<Historique> findByUtilisateurId(UUID utilisateurId);
    /**
     * Finds history records by action type.
     * @param action the action name
     * @return list of history records for the specified action
     */
    List<Historique> findByAction(String action);
    /**
     * Finds history records by action date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return list of history records in the date range
     */
    List<Historique> findByDateActionBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    /**
     * Counts history records by action type.
     * @return list of action and count pairs
     */
    @Query("SELECT h.action, COUNT(h.id) FROM Historique h GROUP BY h.action")
    List<Object[]> countByActionType();
    /**
     * Finds history records by user ID (alternative method).
     * @param utilisateurId the user UUID
     * @return list of history records by the user
     */
    List<Historique> findByUtilisateur_Id(UUID utilisateurId);
} 