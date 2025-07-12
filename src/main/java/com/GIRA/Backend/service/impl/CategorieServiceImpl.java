package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.CategorieService;
import com.GIRA.Backend.Respository.CategorieRepository;
import com.GIRA.Backend.Entities.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of CategorieService.
 * Provides add, retrieval, advanced queries, and statistics for categories.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class CategorieServiceImpl implements CategorieService {
    private final CategorieRepository categorieRepository;

    @Autowired
    public CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    /**
     * Adds a new category.
     * @param categorie The category entity to add
     * @return The added category entity
     */
    @Override
    public Categorie addCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    /**
     * Retrieves a category by its ID.
     * @param id The category UUID
     * @return Optional containing the category entity if found
     */
    @Override
    public Optional<Categorie> getCategorieById(UUID id) {
        return categorieRepository.findById(id);
    }

    /**
     * Retrieves all categories.
     * @return List of category entities
     */
    @Override
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    /**
     * Retrieves all active categories.
     * @return List of active category entities
     */
    @Override
    public List<Categorie> getActiveCategories() {
        return categorieRepository.findByActifTrue();
    }

    /**
     * Retrieves all categories by parent ID.
     * @param parentId The parent category UUID
     * @return List of category entities
     */
    @Override
    public List<Categorie> getCategoriesByParentId(UUID parentId) {
        return categorieRepository.findByParent_Id(parentId);
    }

    /**
     * Retrieves a category by its name (case-insensitive).
     * @param nom The category name
     * @return Optional containing the category entity if found
     */
    @Override
    public Optional<Categorie> getCategorieByNomIgnoreCase(String nom) {
        return categorieRepository.findByNomIgnoreCase(nom);
    }

    /**
     * Counts categories by parent ID.
     * @param parentId The parent category UUID
     * @return Number of categories
     */
    @Override
    public long countByParentId(UUID parentId) {
        return categorieRepository.countByParent_Id(parentId);
    }

    /**
     * Advanced search with filters and pagination.
     * @param nom The category name (optional)
     * @param actif Active status (optional)
     * @param parentId Parent category UUID (optional)
     * @param pageable Pagination parameters
     * @return Page of category entities
     */
    @Override
    public Page<Categorie> findWithFilters(String nom, Boolean actif, UUID parentId, Pageable pageable) {
        return categorieRepository.findWithFilters(nom, actif, parentId, pageable);
    }

    /**
     * Deletes a category by its ID.
     * @param id The category UUID
     */
    @Override
    public void deleteCategorie(UUID id) {
        categorieRepository.deleteById(id);
    }
} 