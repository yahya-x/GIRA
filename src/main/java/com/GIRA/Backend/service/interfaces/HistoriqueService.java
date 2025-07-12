package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.Historique;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing complaint history (historique).
 * Provides add, retrieval, advanced queries, and statistics.
 * @author Mohamed Yahya Jabrane
 */
public interface HistoriqueService {
    /**
     * Adds a new history record for a complaint.
     * @param historique The history entity to add
     * @return The added history entity
     */
    Historique addHistorique(Historique historique);

    /**
     * Retrieves a history record by its ID.
     * @param id The history UUID
     * @return The history entity
     */
    Historique getHistoriqueById(UUID id);

    /**
     * Retrieves all history records for a specific complaint.
     * @param reclamationId The complaint UUID
     * @return List of history entities
     */
    List<Historique> getHistoriqueByReclamationId(UUID reclamationId);

    /**
     * Retrieves all history records by a specific user.
     * @param utilisateurId The user UUID
     * @return List of history entities
     */
    List<Historique> getHistoriqueByUtilisateurId(UUID utilisateurId);

    /**
     * Retrieves all history records for a specific action.
     * @param action The action name
     * @return List of history entities
     */
    List<Historique> getHistoriqueByAction(String action);

    /**
     * Retrieves all history records created within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of history entities
     */
    List<Historique> getHistoriqueByDateActionBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Counts history records by action type.
     * @return List of action and count pairs
     */
    List<Object[]> countByActionType();

    /**
     * Deletes a history record by its ID.
     * @param id The history UUID
     */
    void deleteHistorique(UUID id);
} 