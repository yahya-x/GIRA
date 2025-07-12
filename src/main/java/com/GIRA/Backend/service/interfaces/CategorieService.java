package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.Categorie;
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
     * Deletes a category by its ID.
     * @param id The category UUID
     */
    void deleteCategorie(UUID id);
} 