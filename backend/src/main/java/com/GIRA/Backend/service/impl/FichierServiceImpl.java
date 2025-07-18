package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.FichierService;
import com.GIRA.Backend.Respository.FichierRepository;
import com.GIRA.Backend.Entities.Fichier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of FichierService.
 * Provides upload, retrieval, advanced queries, and statistics for files.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class FichierServiceImpl implements FichierService {
    private final FichierRepository fichierRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.file.max-size:10485760}") // 10MB default
    private long maxFileSize;

    @Autowired
    public FichierServiceImpl(FichierRepository fichierRepository) {
        this.fichierRepository = fichierRepository;
    }

    @PostConstruct
    private void initialize() {
        createUploadDirectory();
    }

    private void createUploadDirectory() {
        if (uploadDir == null || uploadDir.trim().isEmpty()) {
            throw new IllegalStateException("Upload directory property 'app.upload.dir' is not set or is empty");
        }
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Override
    public Fichier uploadFile(Fichier fichier) {
        return fichierRepository.save(fichier);
    }

    /**
     * Uploads a file from MultipartFile and creates a Fichier entity.
     */
    @Override
    public Fichier uploadFile(MultipartFile file, UUID reclamationId, UUID userId) throws IOException {
        // Validate file size
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size");
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Create file path
        Path filePath = Paths.get(uploadDir, uniqueFilename);

        // Save file to disk
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Create Fichier entity
        Fichier fichier = new Fichier();
        fichier.setNomOriginal(originalFilename);
        fichier.setCheminComplet(filePath.toString());
        fichier.setTypeMime(file.getContentType());
        fichier.setTaille(file.getSize());
        fichier.setDateUpload(LocalDateTime.now());
        fichier.setHashFichier(calculateFileHash(filePath));

        // Set relationships (these would need to be loaded from repositories)
        // fichier.setReclamation(reclamationRepository.findById(reclamationId).orElse(null));
        // fichier.setUploadePar(userRepository.findById(userId).orElse(null));

        return fichierRepository.save(fichier);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String calculateFileHash(Path filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Files.readAllBytes(filePath));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Error calculating file hash", e);
        }
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
        Fichier fichier = fichierRepository.findById(id).orElse(null);
        if (fichier != null) {
            // Delete file from storage
            try {
                Path filePath = Paths.get(fichier.getCheminComplet());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Log error but continue with database deletion
                System.err.println("Error deleting file from storage: " + e.getMessage());
            }
            
            // Delete from database
            fichierRepository.deleteById(id);
        }
    }
} 