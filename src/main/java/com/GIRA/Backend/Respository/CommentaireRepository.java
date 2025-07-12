package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, UUID> {

    // Find comments by complaint
    List<Commentaire> findByReclamation_Id(UUID reclamationId);

    // Find comments by author
    List<Commentaire> findByAuteurId(UUID auteurId);

    // Find comments by type
    List<Commentaire> findByType(String type);

    // Find comments by creation date range
    List<Commentaire> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Count comments by complaint
    long countByReclamation_Id(UUID reclamationId);

    // Advanced search by content (case-insensitive)
    @Query("SELECT c FROM Commentaire c WHERE LOWER(c.contenu) LIKE LOWER(CONCAT('%', :contenu, '%'))")
    List<Commentaire> findByContenuContainingIgnoreCase(@Param("contenu") String contenu);
} 