package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Historique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface    HistoriqueRepository extends JpaRepository<Historique, UUID> {

    // Find history by complaint
    List<Historique> findByReclamation_Id(UUID reclamationId);

    // Find history by user
    List<Historique> findByUtilisateurId(UUID utilisateurId);

    // Find history by action
    List<Historique> findByAction(String action);

    // Find history by date range
    List<Historique> findByDateActionBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Count actions by type
    @Query("SELECT h.action, COUNT(h.id) FROM Historique h GROUP BY h.action")
    List<Object[]> countByActionType();

    List<Historique> findByUtilisateur_Id(UUID utilisateurId);
} 