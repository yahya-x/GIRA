package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.FichierService;
import com.GIRA.Backend.Respository.FichierRepository;
import com.GIRA.Backend.Entities.Fichier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of FichierService.
 * Provides upload, retrieval, advanced queries, and statistics for files.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class FichierServiceImpl implements FichierService {
    private final FichierRepository fichierRepository;

    @Autowired
    public FichierServiceImpl(FichierRepository fichierRepository) {
        this.fichierRepository = fichierRepository;
    }

    @Override
    public Fichier uploadFile(Fichier fichier) {
        // TODO: Add file storage logic (local/cloud storage)
        return fichierRepository.save(fichier);
    }

    @Override
    public Fichier getFileById(UUID id) {
        return fichierRepository.findById(id).orElse(null);
    }

    @Override
    public List<Fichier> getFilesByReclamationId(UUID reclamationId) {
        return fichierRepository.findByReclamation_Id(reclamationId);
    }

    @Override
    public List<Fichier> getFilesByUploadeParId(UUID userId) {
        return fichierRepository.findByUploadePar_Id(userId);
    }

    @Override
    public List<Fichier> getFilesByTypeMime(String typeMime) {
        return fichierRepository.findByTypeMime(typeMime);
    }

    @Override
    public List<Fichier> getFilesByDateUploadBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return fichierRepository.findByDateUploadBetween(dateDebut, dateFin);
    }

    @Override
    public long countByReclamationId(UUID reclamationId) {
        return fichierRepository.countByReclamation_Id(reclamationId);
    }

    @Override
    public List<Fichier> findByNomOriginalIgnoreCase(String nomOriginal) {
        return fichierRepository.findByNomOriginalIgnoreCase(nomOriginal);
    }

    @Override
    public void deleteFile(UUID id) {
        // TODO: Add file deletion from storage logic
        fichierRepository.deleteById(id);
    }
} 