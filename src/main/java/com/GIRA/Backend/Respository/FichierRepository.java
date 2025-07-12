package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FichierRepository extends JpaRepository<Fichier, UUID> {

    // Find files by complaint
    List<Fichier> findByReclamation_Id(UUID reclamationId);

    // Find files uploaded by a user
    List<Fichier> findByUploadePar_Id(UUID userId);

    // Find files by type
    List<Fichier> findByTypeMime(String typeMime);

    // Find files uploaded within a date range
    List<Fichier> findByDateUploadBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Count files by complaint
    long countByReclamation_Id(UUID reclamationId);

    // Advanced search by original name (case-insensitive)
    List<Fichier> findByNomOriginalIgnoreCase(String nomOriginal);
} 