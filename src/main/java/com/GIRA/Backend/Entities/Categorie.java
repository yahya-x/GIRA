package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * <p>
 * Entity representing a complaint category.
 * Supports hierarchical structure via parent-child relationship.
 * </p>
 *
 * Examples: Bagages, Retards/Annulations, Personnel, Installations, Sécurité, Restauration, Commerces
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "categories")
public class Categorie extends BaseEntity {

    /**
     * Name of the category (not null).
     */
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    /**
     * Description of the category.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Icon representing the category.
     */
    @Column(name = "icone", length = 100)
    private String icone;

    /**
     * Color code for the category (e.g., #FF0000).
     */
    @Column(name = "couleur", length = 20)
    private String couleur;

    /**
     * Estimated resolution time in hours.
     */
    @Column(name = "temps_resolution_estime")
    private Integer tempsResolutionEstime;

    /**
     * Indicates whether the category is active (default true).
     */
    @Column(name = "actif", nullable = false)
    private boolean actif = true;

    /**
     * Display order for the category (ordre_affichage in DB).
     */
    @Column(name = "ordre_affichage")
    private Integer ordreAffichage;

    /**
     * Parent category for hierarchy (nullable).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Categorie parent;

    /**
     * List of subcategories (children) for this category.
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Categorie> sousCategories;

    /**
     * Default constructor.
     */
    public Categorie() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Adds a subcategory to this category.
     *
     * @param sousCategorie the subcategory to add
     */
    public void ajouterSousCategorie(Categorie sousCategorie) {
        // TODO: Implement add subcategory logic
    }

    /**
     * Retrieves the list of subcategories for this category.
     *
     * @return list of subcategories
     */
    public List<Categorie> obtenirSousCategories() {
        // TODO: Implement retrieval logic
        return sousCategories;
    }

    /**
     * Calculates the average resolution time for this category.
     *
     * @return average resolution time in hours
     */
    public Integer calculerTempsMoyenResolution() {
        // TODO: Implement calculation logic
        return null;
    }
} 