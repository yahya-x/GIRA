package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.Fichier;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing files (fichiers).
 * Provides upload, retrieval, advanced queries, and statistics.
 * @author Mohamed Yahya Jabrane
 */
public interface FichierService {
    /**
     * Uploads a file and associates it with a complaint.
     * @param fichier The file entity to upload
     * @return The uploaded file entity
     */
    Fichier uploadFile(Fichier fichier);

    /**
     * Uploads a file from MultipartFile and associates it with a complaint and user.
     * @param file the file to upload
     * @param reclamationId the complaint UUID
     * @param userId the user UUID
     * @return the uploaded file entity
     */
    Fichier uploadFile(org.springframework.web.multipart.MultipartFile file, java.util.UUID reclamationId, java.util.UUID userId) throws java.io.IOException;

    /**
     * Retrieves a file by its ID.
     * @param id The file UUID
     * @return The file entity
     */
    Fichier getFileById(UUID id);

    /**
     * Retrieves all files attached to a specific complaint.
     * @param reclamationId The complaint UUID
     * @return List of file entities
     */
    List<Fichier> getFilesByReclamationId(UUID reclamationId);

    /**
     * Retrieves all files uploaded by a specific user.
     * @param userId The user UUID
     * @return List of file entities
     */
    List<Fichier> getFilesByUploadeParId(UUID userId);

    /**
     * Retrieves all files of a specific MIME type.
     * @param typeMime The MIME type
     * @return List of file entities
     */
    List<Fichier> getFilesByTypeMime(String typeMime);

    /**
     * Retrieves all files uploaded within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of file entities
     */
    List<Fichier> getFilesByDateUploadBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Counts the number of files attached to a specific complaint.
     * @param reclamationId The complaint UUID
     * @return Number of files
     */
    long countByReclamationId(UUID reclamationId);

    /**
     * Searches files by original name (case-insensitive).
     * @param nomOriginal The original file name
     * @return List of file entities
     */
    List<Fichier> findByNomOriginalIgnoreCase(String nomOriginal);

    /**
     * Deletes a file by its ID.
     * @param id The file UUID
     */
    void deleteFile(UUID id);
} 