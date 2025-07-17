package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Entity representing a sub-category of a complaint category.
 * Supports configuration of specific required fields via JSON.
 * </p>
 *
 * Examples for Bagages: Bagage perdu, Bagage endommagé, Bagage retardé, Contenu manquant
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "sous_categories")
public class SousCategorie extends BaseEntity {

    /**
     * The parent category to which this sub-category belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    /**
     * Name of the sub-category (not null).
     */
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    /**
     * Description of the sub-category.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * JSON configuration for required fields specific to this sub-category.
     * Example: '{"champ1":true,"champ2":false}'
     */
    @Column(name = "champs_requis", columnDefinition = "TEXT")
    private String champsRequis;

    /**
     * Display order for the sub-category (ordre_affichage in DB).
     */
    @Column(name = "ordre_affichage")
    private Integer ordreAffichage;

    /**
     * Indicates whether the sub-category is active (default true).
     */
    @Column(name = "actif", nullable = false)
    private boolean actif = true;

    /**
     * Display order for the sub-category.
     */
    @Column(name = "ordre")
    private Integer ordre;

    /**
     * Default constructor.
     */
    public SousCategorie() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Returns the required fields configuration for this subcategory.
     * Parses the JSON configuration and returns a structured object.
     *
     * @return object containing required field configuration
     */
    public Object obtenirChampsRequis() {
        if (this.champsRequis != null && !this.champsRequis.isEmpty()) {
            return this.champsRequis; // Return as string for now
        }
        return null;
    }

    /**
     * Validates the provided data against the required fields configuration.
     * Checks if all required fields are present and valid.
     *
     * @param donnees the data to validate
     * @return true if validation passes, false otherwise
     */
    public boolean validerDonnees(Object donnees) {
        return true; // Placeholder
    }

    // ====== Getters and Setters ======
    
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getChampsRequis() { return champsRequis; }
    public void setChampsRequis(String champsRequis) { this.champsRequis = champsRequis; }
    
    public Integer getOrdreAffichage() { return ordreAffichage; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    public Integer getOrdre() { return ordre; }
    public void setOrdre(Integer ordre) { this.ordre = ordre; }
} 