package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Categorie entities.
 * Provides data access methods for complaint categories and hierarchy.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, UUID> {
    /**
     * Finds all active categories.
     * @return list of active categories
     */
    List<Categorie> findByActifTrue();
    /**
     * Finds categories by parent category ID.
     * @param parentId the parent category UUID
     * @return list of categories with the specified parent
     */
    List<Categorie> findByParent_Id(UUID parentId);
    /**
     * Finds a category by name (case-insensitive).
     * @param nom the category name
     * @return optional containing the category if found
     */
    Optional<Categorie> findByNomIgnoreCase(String nom);
    /**
     * Counts categories by parent category ID.
     * @param parentId the parent category UUID
     * @return number of categories with the specified parent
     */
    long countByParent_Id(UUID parentId);
    /**
     * Advanced search with filters and pagination.
     * @param nom the category name (optional)
     * @param actif active status (optional)
     * @param parentId parent category UUID (optional)
     * @param pageable pagination parameters
     * @return page of categories matching the filters
     */
    @Query("SELECT c FROM Categorie c WHERE " +
           "(:nom IS NULL OR LOWER(c.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:actif IS NULL OR c.actif = :actif) AND " +
           "(:parentId IS NULL OR c.parent.id = :parentId)")
    Page<Categorie> findWithFilters(
        @Param("nom") String nom,
        @Param("actif") Boolean actif,
        @Param("parentId") UUID parentId,
        Pageable pageable
    );
} 