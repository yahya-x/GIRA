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

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, UUID> {

    // Find by user
    List<Reclamation> findByUtilisateur_Id(UUID utilisateurId);

    // Find by assigned agent
    List<Reclamation> findByAgentAssigne_Id(UUID agentId);

    // Find by status
    List<Reclamation> findByStatut(Reclamation.Statut statut);

    // Find by category
    List<Reclamation> findByCategorie_Id(UUID categorieId);

    // Find by subcategory
    List<Reclamation> findBySousCategorie_Id(UUID sousCategorieId);

    // Find by unique complaint number
    Optional<Reclamation> findByNumero(String numero);

    // Find by creation date range
    List<Reclamation> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Find by priority
    List<Reclamation> findByPriorite(Reclamation.Priorite priorite);

    // Advanced search with filters and pagination
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

    // Count by status
    long countByStatut(Reclamation.Statut statut);

    // Count by agent
    long countByAgentAssigne_Id(UUID agentId);

    // Count by user
    long countByUtilisateur_Id(UUID utilisateurId);

    // Statistics: number of complaints per category
    @Query("SELECT r.categorie.nom, COUNT(r.id) FROM Reclamation r GROUP BY r.categorie.nom")
    List<Object[]> countByCategorie();

    // Statistics: number of complaints per status
    @Query("SELECT r.statut, COUNT(r.id) FROM Reclamation r GROUP BY r.statut")
    List<Object[]> countByStatutGroup();
} 