package com.GIRA.Backend.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO pour le tableau de bord agent.
 * Contient les statistiques personnelles, la charge de travail, et les performances de l'agent.
 *
 * @author GIRA
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentDashboardResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Statistiques personnelles de l'agent.
     */
    @JsonProperty("statistiquesPersonnelles")
    private StatistiquesPersonnelles statistiquesPersonnelles;

    /**
     * Répartition des réclamations assignées par statut.
     */
    @JsonProperty("repartitionParStatut")
    private Map<String, Long> repartitionParStatut;

    /**
     * Répartition des réclamations assignées par priorité.
     */
    @JsonProperty("repartitionParPriorite")
    private Map<String, Long> repartitionParPriorite;

    /**
     * Répartition des réclamations assignées par catégorie.
     */
    @JsonProperty("repartitionParCategorie")
    private Map<String, Long> repartitionParCategorie;

    /**
     * Réclamations urgentes en attente.
     */
    @JsonProperty("reclamationsUrgentes")
    private List<ReclamationListResponse> reclamationsUrgentes;

    /**
     * Réclamations en retard (dépassant les délais).
     */
    @JsonProperty("reclamationsEnRetard")
    private List<ReclamationListResponse> reclamationsEnRetard;

    /**
     * Performance du mois en cours.
     */
    @JsonProperty("performanceMois")
    private PerformanceMois performanceMois;

    /**
     * Tendances des 7 derniers jours.
     */
    @JsonProperty("tendances7Jours")
    private List<TendanceJournaliere> tendances7Jours;

    /**
     * Statistiques de satisfaction des clients.
     */
    @JsonProperty("satisfactionClients")
    private StatistiquesSatisfaction satisfactionClients;

    /**
     * Date de génération du rapport.
     */
    @JsonProperty("dateGeneration")
    private LocalDateTime dateGeneration;

    /**
     * Statistiques personnelles de l'agent.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatistiquesPersonnelles {
        @JsonProperty("totalAssignees")
        private Long totalAssignees;

        @JsonProperty("reclamationsEnCours")
        private Long reclamationsEnCours;

        @JsonProperty("reclamationsResolues")
        private Long reclamationsResolues;

        @JsonProperty("reclamationsUrgentes")
        private Long reclamationsUrgentes;

        @JsonProperty("reclamationsEnRetard")
        private Long reclamationsEnRetard;

        @JsonProperty("tauxResolution")
        private Double tauxResolution;

        @JsonProperty("tempsResolutionMoyen")
        private Double tempsResolutionMoyen;

        @JsonProperty("chargeTravail")
        private String chargeTravail; // "FAIBLE", "NORMALE", "ELEVEE"
    }

    /**
     * Performance du mois en cours.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceMois {
        @JsonProperty("reclamationsTraitees")
        private Long reclamationsTraitees;

        @JsonProperty("tempsResolutionMoyen")
        private Double tempsResolutionMoyen;

        @JsonProperty("satisfactionMoyenne")
        private Double satisfactionMoyenne;

        @JsonProperty("objectifAtteint")
        private Boolean objectifAtteint;

        @JsonProperty("pourcentageObjectif")
        private Double pourcentageObjectif;
    }

    /**
     * Tendance journalière.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TendanceJournaliere {
        @JsonProperty("date")
        private String date;

        @JsonProperty("nouvellesAssignations")
        private Long nouvellesAssignations;

        @JsonProperty("reclamationsResolues")
        private Long reclamationsResolues;

        @JsonProperty("reclamationsEnCours")
        private Long reclamationsEnCours;
    }

    /**
     * Statistiques de satisfaction des clients.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatistiquesSatisfaction {
        @JsonProperty("noteMoyenne")
        private Double noteMoyenne;

        @JsonProperty("totalEvaluations")
        private Long totalEvaluations;

        @JsonProperty("repartitionNotes")
        private Map<Integer, Long> repartitionNotes;

        @JsonProperty("tauxSatisfaction")
        private Double tauxSatisfaction;

        @JsonProperty("commentairesPositifs")
        private Long commentairesPositifs;

        @JsonProperty("commentairesNegatifs")
        private Long commentairesNegatifs;
    }
} 