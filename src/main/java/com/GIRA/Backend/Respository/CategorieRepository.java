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

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, UUID> {

    // Find all active categories
    List<Categorie> findByActifTrue();

    // Find by parent category
    List<Categorie> findByParent_Id(UUID parentId);

    // Find by name (case-insensitive)
    Optional<Categorie> findByNomIgnoreCase(String nom);

    // Count by parent
    long countByParent_Id(UUID parentId);

    // Advanced search with pagination
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