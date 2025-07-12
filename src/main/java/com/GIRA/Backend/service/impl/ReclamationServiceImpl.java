package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.ReclamationService;
import com.GIRA.Backend.Respository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.GIRA.Backend.Entities.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Implementation of ReclamationService.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class ReclamationServiceImpl implements ReclamationService {
    private final ReclamationRepository reclamationRepository;

    @Autowired
    public ReclamationServiceImpl(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;
    }

    /**
     * Creates a new complaint.
     * @param reclamation The complaint entity to create
     * @return The created complaint entity
     */
    @Override
    public Reclamation createReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    /**
     * Retrieves a complaint by ID.
     * @param id The complaint UUID
     * @return Optional containing the complaint entity if found
     */
    @Override
    public Optional<Reclamation> getReclamationById(UUID id) {
        return reclamationRepository.findById(id);
    }

    /**
     * Retrieves all complaints.
     * @return List of complaint entities
     */
    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    /**
     * Updates a complaint.
     * @param id The complaint UUID
     * @param reclamation The updated complaint data
     * @return The updated complaint entity
     */
    @Override
    public Reclamation updateReclamation(UUID id, Reclamation reclamation) {
        Optional<Reclamation> existingOpt = reclamationRepository.findById(id);
        if (existingOpt.isEmpty()) return null;
        Reclamation existing = existingOpt.get();
        // TODO: Update only allowed fields
        existing.setStatut(reclamation.getStatut());
        existing.setPriorite(reclamation.getPriorite());
        existing.setCategorie(reclamation.getCategorie());
        existing.setSousCategorie(reclamation.getSousCategorie());
        existing.setAgentAssigne(reclamation.getAgentAssigne());
        existing.setDescription(reclamation.getDescription());
        // ... add more fields as needed
        return reclamationRepository.save(existing);
    }

    /**
     * Deletes a complaint by ID.
     * @param id The complaint UUID
     */
    @Override
    public void deleteReclamation(UUID id) {
        reclamationRepository.deleteById(id);
    }

    /**
     * Finds complaints by user ID.
     * @param utilisateurId The user UUID
     * @return List of complaint entities
     */
    @Override
    public List<Reclamation> findByUtilisateurId(UUID utilisateurId) {
        return reclamationRepository.findByUtilisateur_Id(utilisateurId);
    }

    /**
     * Finds complaints by assigned agent ID.
     * @param agentId The agent UUID
     * @return List of complaint entities
     */
    @Override
    public List<Reclamation> findByAgentAssigneId(UUID agentId) {
        return reclamationRepository.findByAgentAssigne_Id(agentId);
    }

    /**
     * Finds complaints by status.
     * @param statut The complaint status
     * @return List of complaint entities
     */
    @Override
    public List<Reclamation> findByStatut(Reclamation.Statut statut) {
        return reclamationRepository.findByStatut(statut);
    }

    /**
     * Finds complaints by category ID.
     * @param categorieId The category UUID
     * @return List of complaint entities
     */
    @Override
    public List<Reclamation> findByCategorieId(UUID categorieId) {
        return reclamationRepository.findByCategorie_Id(categorieId);
    }

    /**
     * Finds complaints by subcategory ID.
     * @param sousCategorieId The subcategory UUID
     * @return List of complaint entities
     */
    @Override
    public List<Reclamation> findBySousCategorieId(UUID sousCategorieId) {
        return reclamationRepository.findBySousCategorie_Id(sousCategorieId);
    }

    /**
     * Finds a complaint by its unique number.
     * @param numero The unique complaint number
     * @return Optional containing the complaint entity if found
     */
    @Override
    public Optional<Reclamation> findByNumero(String numero) {
        return reclamationRepository.findByNumero(numero);
    }

    /**
     * Finds complaints by creation date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of complaint entities
     */
    @Override
    public List<Reclamation> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return reclamationRepository.findByDateCreationBetween(dateDebut, dateFin);
    }

    /**
     * Finds complaints by priority.
     * @param priorite The complaint priority
     * @return List of complaint entities
     */
    @Override
    public List<Reclamation> findByPriorite(Reclamation.Priorite priorite) {
        return reclamationRepository.findByPriorite(priorite);
    }

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
    @Override
    public Page<Reclamation> findWithFilters(Reclamation.Statut statut, Reclamation.Priorite priorite, UUID categorieId, UUID sousCategorieId, UUID agentId, UUID utilisateurId, Pageable pageable) {
        return reclamationRepository.findWithFilters(statut, priorite, categorieId, sousCategorieId, agentId, utilisateurId, pageable);
    }

    /**
     * Counts complaints by status.
     * @param statut The complaint status
     * @return Number of complaints
     */
    @Override
    public long countByStatut(Reclamation.Statut statut) {
        return reclamationRepository.countByStatut(statut);
    }

    /**
     * Counts complaints by agent.
     * @param agentId The agent UUID
     * @return Number of complaints
     */
    @Override
    public long countByAgentAssigneId(UUID agentId) {
        return reclamationRepository.countByAgentAssigne_Id(agentId);
    }

    /**
     * Counts complaints by user.
     * @param utilisateurId The user UUID
     * @return Number of complaints
     */
    @Override
    public long countByUtilisateurId(UUID utilisateurId) {
        return reclamationRepository.countByUtilisateur_Id(utilisateurId);
    }

    /**
     * Gets statistics: number of complaints per category.
     * @return List of category name and complaint count pairs
     */
    @Override
    public List<Object[]> countByCategorie() {
        return reclamationRepository.countByCategorie();
    }

    /**
     * Gets statistics: number of complaints per status.
     * @return List of status and complaint count pairs
     */
    @Override
    public List<Object[]> countByStatutGroup() {
        return reclamationRepository.countByStatutGroup();
    }
} 