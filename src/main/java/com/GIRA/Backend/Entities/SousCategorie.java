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
    @Column(name = "champs_requis", columnDefinition = "jsonb")
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
     * Retrieves the specific fields required for this sub-category.
     *
     * @return JSON string representing required fields
     */
    public String obtenirChampsSpecifiques() {
        // TODO: Implement logic to return required fields
        return champsRequis;
    }

    /**
     * Validates the provided data against the required fields configuration.
     *
     * @param donnees the data to validate
     * @return true if valid, false otherwise
     */
    public boolean validerDonnees(Object donnees) {
        // TODO: Implement validation logic
        return false;
    }
} 