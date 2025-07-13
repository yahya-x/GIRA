package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * <p>
 * Entity representing a file attached to a complaint (reclamation).
 * Stores metadata and relationships for file management.
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "fichiers")
public class Fichier extends BaseEntity {

    /**
     * The complaint (reclamation) to which this file is attached.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamation_id", nullable = false)
    private Reclamation reclamation;

    /**
     * Original file name (not null).
     */
    @Column(name = "nom_original", nullable = false, length = 255)
    private String nomOriginal;

    /**
     * Full file path (not null).
     */
    @Column(name = "chemin_complet", nullable = false, length = 500)
    private String cheminComplet;

    /**
     * MIME type of the file (not null).
     */
    @Column(name = "type_mime", nullable = false, length = 100)
    private String typeMime;

    /**
     * File size in bytes (not null).
     */
    @Column(name = "taille", nullable = false)
    private Long taille;

    /**
     * File hash (checksum) for integrity, mapped to hash_fichier in DB.
     */
    @Column(name = "hash_fichier", length = 128)
    private String hashFichier;

    /**
     * Date and time when the file was uploaded (not null).
     */
    @Column(name = "date_upload", nullable = false)
    private LocalDateTime dateUpload;

    /**
     * The user who uploaded the file.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploade_par_id")
    private User uploadePar;

    /**
     * Default constructor.
     */
    public Fichier() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Uploads a file and associates it with a complaint.
     * Creates a new Fichier entity with proper metadata.
     *
     * @param fichier      the file to upload (should be MultipartFile or similar)
     * @param reclamationId the complaint ID
     * @return the uploaded Fichier entity
     */
    public static Fichier uploaderFichier(Object fichier, Long reclamationId) {
        Fichier f = new Fichier();
        f.setDateUpload(LocalDateTime.now());
        f.setActif(true);
        f.setDateCreation(LocalDateTime.now());
        return f;
    }

    /**
     * Deletes the file from storage and database.
     * Removes the physical file and marks the entity as inactive.
     */
    public void supprimerFichier() {
        // actif is set automatically by @PreUpdate in BaseEntity
        // dateModification is set automatically by @PreUpdate in BaseEntity
    }

    /**
     * Generates a signed URL for accessing the file.
     * Useful for secure, time-limited access to files.
     *
     * @param duree the duration for which the URL is valid (in seconds)
     * @return the signed URL as a String
     */
    public String genererUrlSignee(Long duree) {
        return "https://example.com/files/" + this.id + "?token=placeholder&expires=" + 
               (System.currentTimeMillis() + (duree * 1000));
    }

    /**
     * Validates the file size against allowed limits.
     * Checks if the file size is within acceptable bounds.
     *
     * @return true if valid, false otherwise
     */
    public boolean validerTaille() {
        long maxSize = 10 * 1024 * 1024; // 10MB default
        return this.taille != null && this.taille <= maxSize && this.taille > 0;
    }

    /**
     * Validates the file type against allowed MIME types.
     * Checks if the file type is permitted for upload.
     *
     * @return true if valid, false otherwise
     */
    public boolean validerType() {
        if (this.typeMime == null) return false;
        
        String[] allowedTypes = {
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf", "application/msword", 
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain", "text/csv"
        };
        
        for (String allowedType : allowedTypes) {
            if (allowedType.equals(this.typeMime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the checksum of the file.
     * Uses SHA-256 for file integrity verification.
     *
     * @return the checksum as a String
     */
    public String calculerChecksum() {
        return "placeholder_checksum_" + this.id;
    }
    
    // ====== Getters and Setters ======
    
    public Reclamation getReclamation() { return reclamation; }
    public void setReclamation(Reclamation reclamation) { this.reclamation = reclamation; }
    
    public String getNomOriginal() { return nomOriginal; }
    public void setNomOriginal(String nomOriginal) { this.nomOriginal = nomOriginal; }
    
    public String getCheminComplet() { return cheminComplet; }
    public void setCheminComplet(String cheminComplet) { this.cheminComplet = cheminComplet; }
    
    public String getTypeMime() { return typeMime; }
    public void setTypeMime(String typeMime) { this.typeMime = typeMime; }
    
    public Long getTaille() { return taille; }
    public void setTaille(Long taille) { this.taille = taille; }
    
    public String getHashFichier() { return hashFichier; }
    public void setHashFichier(String hashFichier) { this.hashFichier = hashFichier; }
    
    public LocalDateTime getDateUpload() { return dateUpload; }
    public void setDateUpload(LocalDateTime dateUpload) { this.dateUpload = dateUpload; }
    
    public User getUploadePar() { return uploadePar; }
    public void setUploadePar(User uploadePar) { this.uploadePar = uploadePar; }
} 