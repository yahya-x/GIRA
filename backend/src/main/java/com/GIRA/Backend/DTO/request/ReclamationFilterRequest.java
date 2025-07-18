package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO pour le filtrage avancé et la pagination des réclamations.
 * Permet de rechercher les réclamations selon plusieurs critères métier (statut, date, catégorie, etc.)
 * et de contrôler la pagination et le tri côté client.
 *
 * @author GIRA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReclamationFilterRequest implements Serializable {
    /** Statut de la réclamation (ex: SOUMISE, EN_COURS, RESOLUE, etc.) */
    @JsonProperty("statut")
    private String statut;

    /** Identifiant de la catégorie */
    @JsonProperty("categorieId")
    private UUID categorieId;

    /** Identifiant de la sous-catégorie */
    @JsonProperty("sousCategorieId")
    private UUID sousCategorieId;

    /** Priorité de la réclamation (ex: BASSE, NORMALE, HAUTE, URGENTE) */
    @JsonProperty("priorite")
    private String priorite;

    /** Recherche par mot-clé (titre, description, etc.) */
    @JsonProperty("motCle")
    private String motCle;

    /** Date de début de création (inclus) */
    @JsonProperty("dateDebut")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateDebut;

    /** Date de fin de création (inclus) */
    @JsonProperty("dateFin")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFin;

    /** Page demandée (0 = première page) */
    @JsonProperty("page")
    @Min(0)
    private Integer page = 0;

    /** Taille de la page (nombre d'éléments par page) */
    @JsonProperty("size")
    @Min(1)
    private Integer size = 20;

    /** Critère de tri (ex: "dateCreation,desc") */
    @JsonProperty("sort")
    private String sort = "dateCreation,desc";
} 