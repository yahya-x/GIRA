package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.time.LocalDateTime;

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

    // ====== Getters and Setters ======
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }
    
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    
    public Integer getTempsResolutionEstime() { return tempsResolutionEstime; }
    public void setTempsResolutionEstime(Integer tempsResolutionEstime) { this.tempsResolutionEstime = tempsResolutionEstime; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    public Integer getOrdreAffichage() { return ordreAffichage; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
    
    public Categorie getParent() { return parent; }
    public void setParent(Categorie parent) { this.parent = parent; }
    
    public List<Categorie> getSousCategories() { return sousCategories; }
    public void setSousCategories(List<Categorie> sousCategories) { this.sousCategories = sousCategories; }

    // ====== Business Logic Method Stubs ======

    /**
     * Adds a subcategory to this category.
     * Creates a new subcategory and associates it with this category.
     *
     * @param nomSousCategorie the name of the subcategory
     * @param description     the description of the subcategory
     */
    public void ajouterSousCategorie(String nomSousCategorie, String description) {
        // dateModification is set automatically by @PreUpdate in BaseEntity
    }

    /**
     * Retrieves all subcategories for this category.
     * Returns a list of active subcategories associated with this category.
     *
     * @return list of subcategories
     */
    public Object obtenirSousCategories() {
        return null; // Placeholder
    }

    /**
     * Calculates the estimated resolution time for complaints in this category.
     * Returns the average resolution time based on historical data.
     *
     * @return estimated resolution time in hours
     */
    public Integer calculerTempsResolutionEstime() {
        return this.tempsResolutionEstime != null ? this.tempsResolutionEstime : 24; // Default 24 hours
    }
} 