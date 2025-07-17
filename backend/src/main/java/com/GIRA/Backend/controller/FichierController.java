package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.response.FichierResponse;
import com.GIRA.Backend.Entities.Fichier;
import com.GIRA.Backend.service.interfaces.FichierService;
import com.GIRA.Backend.service.interfaces.ReclamationService;
import com.GIRA.Backend.service.interfaces.UserService;
import com.GIRA.Backend.DTO.common.ApiResponse;
import com.GIRA.Backend.security.UserPrincipal;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.Duration;
import java.time.Instant;
import java.net.URI;

/**
 * REST controller for managing file uploads, downloads, and metadata.
 * Provides endpoints for uploading, downloading, listing, and deleting files associated with complaints.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@RestController
@RequestMapping("/api/fichiers")
public class FichierController {
    private final FichierService fichierService;
    private final ReclamationService reclamationService;
    private final UserService userService;

    @Autowired
    public FichierController(FichierService fichierService, ReclamationService reclamationService, UserService userService) {
        this.fichierService = fichierService;
        this.reclamationService = reclamationService;
        this.userService = userService;
    }

    /**
     * Uploads a file and associates it with a complaint.
     *
     * @param file         the file to upload
     * @param reclamationId the complaint UUID
     * @return the uploaded file's metadata
     */
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<FichierResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("reclamationId") @NotNull UUID reclamationId
    ) throws IOException {
        // Get current user ID from security context
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = userPrincipal.getId();
        Fichier fichier = fichierService.uploadFile(file, reclamationId, userId);
        FichierResponse response = FichierResponse.builder()
                .id(fichier.getId())
                .fileName(fichier.getNomOriginal())
                .url(fichier.getUrl())
                .description(fichier.getDescription())
                .typeMime(fichier.getTypeMime())
                .reclamationId(fichier.getReclamation() != null ? fichier.getReclamation().getId() : null)
                .uploadedBy(fichier.getUploadePar() != null ? fichier.getUploadePar().getId() : null)
                .dateUpload(fichier.getDateUpload())
                .build();
        return ResponseEntity.ok(ApiResponse.success("Fichier uploadé avec succès", response));
    }

    /**
     * Generates a signed URL for downloading a file (valid for a limited time).
     *
     * @param id the file UUID
     * @param durationSeconds validity duration in seconds
     * @return signed URL as a string
     */
    @GetMapping("/signed-url/{id}")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> getSignedUrl(@PathVariable UUID id, @RequestParam(defaultValue = "300") long durationSeconds) {
        Fichier fichier = fichierService.getFileById(id);
        if (fichier == null) {
            return ResponseEntity.notFound().build();
        }
        // Access control: Only complaint owner, assigned agent, or admin
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = userPrincipal.getId();
        boolean isAdmin = userPrincipal.getRole().equals("ADMIN");
        boolean isOwner = fichier.getUploadePar() != null && fichier.getUploadePar().getId().equals(userId);
        boolean isComplaintOwner = fichier.getReclamation() != null && fichier.getReclamation().getUtilisateur() != null && fichier.getReclamation().getUtilisateur().getId().equals(userId);
        boolean isAssignedAgent = fichier.getReclamation() != null && fichier.getReclamation().getAgentAssigne() != null && fichier.getReclamation().getAgentAssigne().getId().equals(userId);
        if (!(isAdmin || isOwner || isComplaintOwner || isAssignedAgent)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Vous n'êtes pas autorisé à accéder à ce fichier."));
        }
        // Generate a pseudo signed URL (for demo; in production, use a real signing mechanism)
        long expiresAt = Instant.now().plus(Duration.ofSeconds(durationSeconds)).getEpochSecond();
        String token = java.util.Base64.getEncoder().encodeToString((id.toString() + ":" + expiresAt).getBytes());
        String signedUrl = String.format("/api/fichiers/download/%s?token=%s&expires=%d", id, token, expiresAt);
        return ResponseEntity.ok(ApiResponse.success("URL signée générée", signedUrl));
    }

    /**
     * Downloads a file by its ID (secured).
     *
     * @param id the file UUID
     * @return the file as a resource
     */
    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID id, @RequestParam(required = false) String token, @RequestParam(required = false) Long expires) throws IOException {
        Fichier fichier = fichierService.getFileById(id);
        if (fichier == null) {
            return ResponseEntity.notFound().build();
        }
        // Access control: Only complaint owner, assigned agent, or admin
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = userPrincipal.getId();
        boolean isAdmin = userPrincipal.getRole().equals("ADMIN");
        boolean isOwner = fichier.getUploadePar() != null && fichier.getUploadePar().getId().equals(userId);
        boolean isComplaintOwner = fichier.getReclamation() != null && fichier.getReclamation().getUtilisateur() != null && fichier.getReclamation().getUtilisateur().getId().equals(userId);
        boolean isAssignedAgent = fichier.getReclamation() != null && fichier.getReclamation().getAgentAssigne() != null && fichier.getReclamation().getAgentAssigne().getId().equals(userId);
        if (!(isAdmin || isOwner || isComplaintOwner || isAssignedAgent)) {
            return ResponseEntity.status(403).build();
        }
        // If token/expires are present, validate pseudo signed URL
        if (token != null && expires != null) {
            long now = Instant.now().getEpochSecond();
            if (now > expires) {
                return ResponseEntity.status(403).build();
            }
            String expectedToken = java.util.Base64.getEncoder().encodeToString((id.toString() + ":" + expires).getBytes());
            if (!expectedToken.equals(token)) {
                return ResponseEntity.status(403).build();
            }
        }
        Path filePath = Paths.get(fichier.getCheminComplet());
        Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
        String contentType = fichier.getTypeMime();
        if (contentType == null) {
            contentType = Files.probeContentType(filePath);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fichier.getNomOriginal() + "\"")
                .body(resource);
    }

    /**
     * Lists all files for a given complaint.
     *
     * @param reclamationId the complaint UUID
     * @return list of file metadata
     */
    @GetMapping("/by-reclamation/{reclamationId}")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<FichierResponse>>> listFilesByReclamation(@PathVariable UUID reclamationId) {
        // Access control: Only complaint owner, assigned agent, or admin
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = userPrincipal.getId();
        boolean isAdmin = userPrincipal.getRole().equals("ADMIN");
        // Get complaint owner and assigned agent
        List<Fichier> fichiers = fichierService.getFilesByReclamationId(reclamationId);
        boolean isComplaintOwner = !fichiers.isEmpty() && fichiers.get(0).getReclamation() != null && fichiers.get(0).getReclamation().getUtilisateur() != null && fichiers.get(0).getReclamation().getUtilisateur().getId().equals(userId);
        boolean isAssignedAgent = !fichiers.isEmpty() && fichiers.get(0).getReclamation() != null && fichiers.get(0).getReclamation().getAgentAssigne() != null && fichiers.get(0).getReclamation().getAgentAssigne().getId().equals(userId);
        if (!(isAdmin || isComplaintOwner || isAssignedAgent)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Vous n'êtes pas autorisé à accéder à ces fichiers."));
        }
        List<FichierResponse> files = fichiers
                .stream().map(f -> FichierResponse.builder()
                        .id(f.getId())
                        .fileName(f.getNomOriginal())
                        .url(f.getUrl())
                        .description(f.getDescription())
                        .typeMime(f.getTypeMime())
                        .reclamationId(f.getReclamation() != null ? f.getReclamation().getId() : null)
                        .uploadedBy(f.getUploadePar() != null ? f.getUploadePar().getId() : null)
                        .dateUpload(f.getDateUpload())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Liste des fichiers récupérée", files));
    }

    /**
     * Deletes a file by its ID (admin or file owner only).
     *
     * @param id the file UUID
     * @return success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSAGER', 'AGENT')")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable UUID id) {
        // Get current user ID from security context
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = userPrincipal.getId();
        Fichier fichier = fichierService.getFileById(id);
        boolean isOwner = fichier != null && fichier.getUploadePar() != null && fichier.getUploadePar().getId().equals(userId);
        boolean isAdmin = userPrincipal.getRole().equals("ADMIN");
        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(403).body(ApiResponse.error("Vous n'êtes pas autorisé à supprimer ce fichier."));
        }
        fichierService.deleteFile(id);
        return ResponseEntity.ok(ApiResponse.success("Fichier supprimé", null));
    }
} 