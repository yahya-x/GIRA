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

@Repository
public interface SousCategorieRepository extends JpaRepository<SousCategorie, UUID> {

    // Find all active subcategories for a category
    List<SousCategorie> findByCategorie_IdAndActifTrue(UUID categorieId);

    // Find by name (case-insensitive)
    Optional<SousCategorie> findByNomIgnoreCase(String nom);

    // Find by parent category
    List<SousCategorie> findByCategorie_Id(UUID categorieId);

    // Advanced search with pagination
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