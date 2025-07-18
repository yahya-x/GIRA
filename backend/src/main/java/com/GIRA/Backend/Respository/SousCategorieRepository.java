package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.SousCategorie;
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
 * Repository interface for managing SousCategorie entities.
 * Provides data access methods for complaint subcategories and hierarchy.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface SousCategorieRepository extends JpaRepository<SousCategorie, UUID> {
    /**
     * Finds all active subcategories for a category.
     * @param categorieId the category UUID
     * @return list of active subcategories for the category
     */
    List<SousCategorie> findByCategorie_IdAndActifTrue(UUID categorieId);
    /**
     * Finds a subcategory by name (case-insensitive).
     * @param nom the subcategory name
     * @return optional containing the subcategory if found
     */
    Optional<SousCategorie> findByNomIgnoreCase(String nom);
    /**
     * Finds subcategories by parent category ID.
     * @param categorieId the category UUID
     * @return list of subcategories for the category
     */
    List<SousCategorie> findByCategorie_Id(UUID categorieId);
    /**
     * Advanced search with filters and pagination.
     * @param nom the subcategory name (optional)
     * @param actif active status (optional)
     * @param categorieId category UUID (optional)
     * @param pageable pagination parameters
     * @return page of subcategories matching the filters
     */
    @Query("SELECT s FROM SousCategorie s WHERE " +
           "(:nom IS NULL OR LOWER(s.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:actif IS NULL OR s.actif = :actif) AND " +
           "(:categorieId IS NULL OR s.categorie.id = :categorieId)")
    Page<SousCategorie> findWithFilters(
        @Param("nom") String nom,
        @Param("actif") Boolean actif,
        @Param("categorieId") UUID categorieId,
        Pageable pageable
    );
} 