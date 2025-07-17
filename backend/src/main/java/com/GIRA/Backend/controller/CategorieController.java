package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.common.ApiResponse;
import com.GIRA.Backend.DTO.request.CategorieCreateRequest;
import com.GIRA.Backend.DTO.request.CategorieUpdateRequest;
import com.GIRA.Backend.DTO.response.CategorieResponse;
import com.GIRA.Backend.service.interfaces.CategorieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des catégories.
 * Fournit des endpoints pour les opérations CRUD, la pagination, le filtrage et la hiérarchie des catégories.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final CategorieService categorieService;

    @Autowired
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    /**
     * Crée une nouvelle catégorie.
     *
     * @param request la requête de création de catégorie
     * @return la réponse catégorie créée
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategorieResponse>> createCategorie(@Valid @RequestBody CategorieCreateRequest request) {
        CategorieResponse response = categorieService.createCategorie(request);
        return ResponseEntity.ok(ApiResponse.success("Catégorie créée avec succès", response));
    }

    /**
     * Met à jour une catégorie existante.
     *
     * @param id l'identifiant de la catégorie
     * @param request la requête de mise à jour
     * @return la réponse catégorie mise à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategorieResponse>> updateCategorie(@PathVariable UUID id, @Valid @RequestBody CategorieUpdateRequest request) {
        CategorieResponse response = categorieService.updateCategorie(id, request);
        return ResponseEntity.ok(ApiResponse.success("Catégorie mise à jour", response));
    }

    /**
     * Supprime une catégorie (admin uniquement).
     *
     * @param id l'identifiant de la catégorie
     * @return confirmation de suppression
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategorie(@PathVariable UUID id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.ok(ApiResponse.success("Catégorie supprimée", null));
    }

    /**
     * Récupère une catégorie par son identifiant.
     *
     * @param id l'identifiant de la catégorie
     * @return la réponse catégorie
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ApiResponse<CategorieResponse>> getCategorieById(@PathVariable UUID id) {
        CategorieResponse response = categorieService.getCategorieByIdDto(id);
        return ResponseEntity.ok(ApiResponse.success("Catégorie récupérée", response));
    }

    /**
     * Liste paginée et filtrée des catégories.
     *
     * @param nom filtre nom (optionnel)
     * @param actif filtre actif (optionnel)
     * @param parentId filtre parent (optionnel)
     * @param pageable pagination et tri
     * @return page de réponses catégorie
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ApiResponse<Page<CategorieResponse>>> getCategories(
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "actif", required = false) Boolean actif,
            @RequestParam(value = "parentId", required = false) UUID parentId,
            @PageableDefault(size = 10, sort = "dateCreation", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CategorieResponse> page = categorieService.findWithFiltersDto(nom, actif, parentId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Liste des catégories récupérée", page));
    }
} 