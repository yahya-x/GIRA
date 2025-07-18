package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.CategorieService;
import com.GIRA.Backend.Respository.CategorieRepository;
import com.GIRA.Backend.Entities.Categorie;
import com.GIRA.Backend.DTO.request.CategorieCreateRequest;
import com.GIRA.Backend.DTO.request.CategorieUpdateRequest;
import com.GIRA.Backend.DTO.response.CategorieResponse;
import com.GIRA.Backend.mapper.CategorieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageImpl;

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
     * Crée une nouvelle catégorie à partir d'un DTO de requête.
     * @param request la requête de création de catégorie
     * @return la réponse catégorie créée
     */
    @Override
    public CategorieResponse createCategorie(CategorieCreateRequest request) {
        Categorie parent = null;
        if (request.getParentId() != null) {
            parent = categorieRepository.findById(request.getParentId()).orElse(null);
        }
        Categorie categorie = CategorieMapper.fromCreateRequest(request, parent);
        Categorie saved = categorieRepository.save(categorie);
        return CategorieMapper.toResponse(saved);
    }

    /**
     * Met à jour une catégorie à partir d'un DTO de requête.
     * @param id l'identifiant de la catégorie
     * @param request la requête de mise à jour
     * @return la réponse catégorie mise à jour
     */
    @Override
    public CategorieResponse updateCategorie(UUID id, CategorieUpdateRequest request) {
        Categorie categorie = categorieRepository.findById(id).orElse(null);
        if (categorie == null) return null;
        Categorie parent = null;
        if (request.getParentId() != null) {
            parent = categorieRepository.findById(request.getParentId()).orElse(null);
        }
        CategorieMapper.updateCategorieFromRequest(categorie, request, parent);
        Categorie saved = categorieRepository.save(categorie);
        return CategorieMapper.toResponse(saved);
    }

    /**
     * Récupère une catégorie par son identifiant et la convertit en DTO de réponse.
     * @param id l'identifiant de la catégorie
     * @return la réponse catégorie
     */
    @Override
    public CategorieResponse getCategorieByIdDto(UUID id) {
        Categorie categorie = categorieRepository.findById(id).orElse(null);
        return CategorieMapper.toResponse(categorie);
    }

    /**
     * Recherche avancée paginée et filtrée des catégories, retourne des DTOs de réponse.
     * @param nom filtre nom (optionnel)
     * @param actif filtre actif (optionnel)
     * @param parentId filtre parent (optionnel)
     * @param pageable pagination et tri
     * @return page de réponses catégorie
     */
    @Override
    public Page<CategorieResponse> findWithFiltersDto(String nom, Boolean actif, UUID parentId, Pageable pageable) {
        Page<Categorie> page = categorieRepository.findWithFilters(nom, actif, parentId, pageable);
        return page.map(CategorieMapper::toResponse);
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

    @Override
    public Categorie addCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }
} 