package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.Categorie;
import com.GIRA.Backend.DTO.request.CategorieCreateRequest;
import com.GIRA.Backend.DTO.request.CategorieUpdateRequest;
import com.GIRA.Backend.DTO.response.CategorieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing categories (categories).
 * Provides add, retrieval, advanced queries, and statistics.
 * @author Mohamed Yahya Jabrane
 */
public interface CategorieService {
    /**
     * Adds a new category.
     * @param categorie The category entity to add
     * @return The added category entity
     */
    Categorie addCategorie(Categorie categorie);

    /**
     * Retrieves a category by its ID.
     * @param id The category UUID
     * @return Optional containing the category entity if found
     */
    Optional<Categorie> getCategorieById(UUID id);

    /**
     * Retrieves all categories.
     * @return List of category entities
     */
    List<Categorie> getAllCategories();

    /**
     * Retrieves all active categories.
     * @return List of active category entities
     */
    List<Categorie> getActiveCategories();

    /**
     * Retrieves all categories by parent ID.
     * @param parentId The parent category UUID
     * @return List of category entities
     */
    List<Categorie> getCategoriesByParentId(UUID parentId);

    /**
     * Retrieves a category by its name (case-insensitive).
     * @param nom The category name
     * @return Optional containing the category entity if found
     */
    Optional<Categorie> getCategorieByNomIgnoreCase(String nom);

    /**
     * Counts categories by parent ID.
     * @param parentId The parent category UUID
     * @return Number of categories
     */
    long countByParentId(UUID parentId);

    /**
     * Advanced search with filters and pagination.
     * @param nom The category name (optional)
     * @param actif Active status (optional)
     * @param parentId Parent category UUID (optional)
     * @param pageable Pagination parameters
     * @return Page of category entities
     */
    Page<Categorie> findWithFilters(String nom, Boolean actif, UUID parentId, Pageable pageable);

    /**
     * Crée une nouvelle catégorie à partir d'un DTO de requête.
     * @param request la requête de création de catégorie
     * @return la réponse catégorie créée
     */
    CategorieResponse createCategorie(CategorieCreateRequest request);

    /**
     * Met à jour une catégorie à partir d'un DTO de requête.
     * @param id l'identifiant de la catégorie
     * @param request la requête de mise à jour
     * @return la réponse catégorie mise à jour
     */
    CategorieResponse updateCategorie(UUID id, CategorieUpdateRequest request);

    /**
     * Récupère une catégorie par son identifiant et la convertit en DTO de réponse.
     * @param id l'identifiant de la catégorie
     * @return la réponse catégorie
     */
    CategorieResponse getCategorieByIdDto(UUID id);

    /**
     * Recherche avancée paginée et filtrée des catégories, retourne des DTOs de réponse.
     * @param nom filtre nom (optionnel)
     * @param actif filtre actif (optionnel)
     * @param parentId filtre parent (optionnel)
     * @param pageable pagination et tri
     * @return page de réponses catégorie
     */
    Page<CategorieResponse> findWithFiltersDto(String nom, Boolean actif, UUID parentId, Pageable pageable);

    /**
     * Deletes a category by its ID.
     * @param id The category UUID
     */
    void deleteCategorie(UUID id);
} 