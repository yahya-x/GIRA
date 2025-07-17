package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.request.ReclamationCreateRequest;
import com.GIRA.Backend.DTO.request.ReclamationUpdateRequest;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;
import com.GIRA.Backend.DTO.response.ReclamationResponse;
import com.GIRA.Backend.DTO.common.ApiResponse;
import com.GIRA.Backend.DTO.request.ReclamationFilterRequest;
import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.service.interfaces.ReclamationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing complaints (reclamations).
 * Provides endpoints for CRUD operations, advanced filtering, pagination, and statistics.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    private final ReclamationService reclamationService;

    @Autowired
    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    /**
     * Creates a new complaint (reclamation).
     *
     * @param request the complaint creation request
     * @return the created complaint response
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<ReclamationResponse>> createReclamation(@Valid @RequestBody ReclamationCreateRequest request) {
        ReclamationResponse response = reclamationService.createReclamation(request);
        return ResponseEntity.ok(ApiResponse.success("Reclamation créée avec succès", response));
    }

    /**
     * Recherche avancée paginée et filtrée des réclamations via un DTO de filtre.
     * Permet de rechercher selon plusieurs critères métier et de contrôler la pagination/tri.
     *
     * @param filterRequest DTO contenant les critères de filtrage et de pagination
     * @return page de réponses liste réclamation
     */
    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<ReclamationListResponse>>> searchReclamations(
            @Valid @RequestBody ReclamationFilterRequest filterRequest) {
        Page<ReclamationListResponse> responses = reclamationService.findWithFiltersDto(filterRequest);
        return ResponseEntity.ok(ApiResponse.success("Liste des réclamations filtrée récupérée", responses));
    }

    /**
     * Lists complaints with advanced filtering, pagination, and sorting.
     *
     * @param statut         (optional) complaint status
     * @param priorite       (optional) complaint priority
     * @param categorieId    (optional) category UUID
     * @param sousCategorieId(optional) subcategory UUID
     * @param agentId        (optional) assigned agent UUID
     * @param utilisateurId  (optional) user UUID
     * @param pageable       pagination and sorting parameters
     * @return paginated list of complaints matching the filters
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<ReclamationListResponse>>> getReclamations(
            @RequestParam(value = "statut", required = false) Reclamation.Statut statut,
            @RequestParam(value = "priorite", required = false) Reclamation.Priorite priorite,
            @RequestParam(value = "categorieId", required = false) UUID categorieId,
            @RequestParam(value = "sousCategorieId", required = false) UUID sousCategorieId,
            @RequestParam(value = "agentId", required = false) UUID agentId,
            @RequestParam(value = "utilisateurId", required = false) UUID utilisateurId,
            @PageableDefault(size = 10, sort = "dateCreation", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReclamationListResponse> responses = reclamationService.findWithFiltersDto(statut, priorite, categorieId, sousCategorieId, agentId, utilisateurId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Liste des réclamations récupérée", responses));
    }

    /**
     * Gets a complaint by its unique identifier.
     *
     * @param id the complaint UUID
     * @return the complaint response
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<ReclamationResponse>> getReclamationById(@PathVariable UUID id) {
        ReclamationResponse response = reclamationService.getReclamationByIdForCurrentUser(id);
        return ResponseEntity.ok(ApiResponse.success("Réclamation récupérée", response));
    }

    /**
     * Updates a complaint (status, assignment, etc.).
     *
     * @param id      the complaint UUID
     * @param request the update request
     * @return the updated complaint response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<ReclamationResponse>> updateReclamation(@PathVariable UUID id, @Valid @RequestBody ReclamationUpdateRequest request) {
        ReclamationResponse response = reclamationService.updateReclamation(id, request);
        return ResponseEntity.ok(ApiResponse.success("Réclamation mise à jour", response));
    }

    /**
     * Deletes a complaint (admin only).
     *
     * @param id the complaint UUID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteReclamation(@PathVariable UUID id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.ok(ApiResponse.success("Réclamation supprimée", null));
    }
} 