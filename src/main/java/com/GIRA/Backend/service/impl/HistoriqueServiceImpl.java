package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.HistoriqueService;
import com.GIRA.Backend.Respository.HistoriqueRepository;
import com.GIRA.Backend.Entities.Historique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of HistoriqueService.
 * Provides add, retrieval, advanced queries, and statistics for complaint history.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class HistoriqueServiceImpl implements HistoriqueService {
    private final HistoriqueRepository historiqueRepository;

    @Autowired
    public HistoriqueServiceImpl(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    /**
     * Adds a new history record for a complaint.
     * @param historique The history entity to add
     * @return The added history entity
     */
    @Override
    public Historique addHistorique(Historique historique) {
        return historiqueRepository.save(historique);
    }

    /**
     * Retrieves a history record by its ID.
     * @param id The history UUID
     * @return The history entity
     */
    @Override
    public Historique getHistoriqueById(UUID id) {
        return historiqueRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all history records for a specific complaint.
     * @param reclamationId The complaint UUID
     * @return List of history entities
     */
    @Override
    public List<Historique> getHistoriqueByReclamationId(UUID reclamationId) {
        return historiqueRepository.findByReclamation_Id(reclamationId);
    }

    /**
     * Retrieves all history records by a specific user.
     * @param utilisateurId The user UUID
     * @return List of history entities
     */
    @Override
    public List<Historique> getHistoriqueByUtilisateurId(UUID utilisateurId) {
        return historiqueRepository.findByUtilisateur_Id(utilisateurId);
    }

    /**
     * Retrieves all history records for a specific action.
     * @param action The action name
     * @return List of history entities
     */
    @Override
    public List<Historique> getHistoriqueByAction(String action) {
        return historiqueRepository.findByAction(action);
    }

    /**
     * Retrieves all history records created within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of history entities
     */
    @Override
    public List<Historique> getHistoriqueByDateActionBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return historiqueRepository.findByDateActionBetween(dateDebut, dateFin);
    }

    /**
     * Counts history records by action type.
     * @return List of action and count pairs
     */
    @Override
    public List<Object[]> countByActionType() {
        return historiqueRepository.countByActionType();
    }

    /**
     * Deletes a history record by its ID.
     * @param id The history UUID
     */
    @Override
    public void deleteHistorique(UUID id) {
        historiqueRepository.deleteById(id);
    }
} 