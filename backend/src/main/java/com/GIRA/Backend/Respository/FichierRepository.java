package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Fichier entities.
 * Provides data access methods for file management in the complaint system.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface FichierRepository extends JpaRepository<Fichier, UUID> {
    /**
     * Finds files by complaint ID.
     * @param reclamationId the complaint UUID
     * @return list of files for the complaint
     */
    List<Fichier> findByReclamation_Id(UUID reclamationId);
    /**
     * Finds files uploaded by a user.
     * @param userId the user UUID
     * @return list of files uploaded by the user
     */
    List<Fichier> findByUploadePar_Id(UUID userId);
    /**
     * Finds files by MIME type.
     * @param typeMime the MIME type
     * @return list of files with the specified MIME type
     */
    List<Fichier> findByTypeMime(String typeMime);
    /**
     * Finds files uploaded within a date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return list of files uploaded in the date range
     */
    List<Fichier> findByDateUploadBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    /**
     * Counts files by complaint ID.
     * @param reclamationId the complaint UUID
     * @return number of files for the complaint
     */
    long countByReclamation_Id(UUID reclamationId);
    /**
     * Finds files by original name (case-insensitive).
     * @param nomOriginal the original file name
     * @return list of files with the specified name
     */
    List<Fichier> findByNomOriginalIgnoreCase(String nomOriginal);
} 