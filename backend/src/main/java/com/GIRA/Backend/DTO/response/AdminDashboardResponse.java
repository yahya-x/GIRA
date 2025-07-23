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
 * DTO pour le tableau de bord administrateur.
 * Contient les statistiques globales du système, les tendances, et les performances des agents.
 *
 * @author GIRA
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Statistiques globales des réclamations.
     */
    @JsonProperty("statistiquesGlobales")
    private StatistiquesGlobales statistiquesGlobales;

    /**
     * Répartition des réclamations par statut.
     */
    @JsonProperty("repartitionParStatut")
    private Map<String, Long> repartitionParStatut;

    /**
     * Répartition des réclamations par catégorie.
     */
    @JsonProperty("repartitionParCategorie")
    private Map<String, Long> repartitionParCategorie;

    /**
     * Répartition des réclamations par priorité.
     */
    @JsonProperty("repartitionParPriorite")
    private Map<String, Long> repartitionParPriorite;

    /**
     * Performance des agents (réclamations assignées et résolues).
     */
    @JsonProperty("performanceAgents")
    private List<PerformanceAgent> performanceAgents;

    /**
     * Tendances des 30 derniers jours.
     */
    @JsonProperty("tendances30Jours")
    private List<TendanceJournaliere> tendances30Jours;

    /**
     * Statistiques de satisfaction client.
     */
    @JsonProperty("satisfactionClient")
    private StatistiquesSatisfaction satisfactionClient;

    /**
     * Temps de résolution moyen par catégorie.
     */
    @JsonProperty("tempsResolutionMoyen")
    private Map<String, Double> tempsResolutionMoyen;

    /**
     * Date de génération du rapport.
     */
    @JsonProperty("dateGeneration")
    private LocalDateTime dateGeneration;

    /**
     * Statistiques globales des réclamations.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatistiquesGlobales {
        @JsonProperty("totalReclamations")
        private Long totalReclamations;

        @JsonProperty("reclamationsEnCours")
        private Long reclamationsEnCours;

        @JsonProperty("reclamationsResolues")
        private Long reclamationsResolues;

        @JsonProperty("reclamationsUrgentes")
        private Long reclamationsUrgentes;

        @JsonProperty("tauxResolution")
        private Double tauxResolution;

        @JsonProperty("tempsResolutionMoyen")
        private Double tempsResolutionMoyen;
    }

    /**
     * Performance d'un agent.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceAgent {
        @JsonProperty("agentId")
        private String agentId;

        @JsonProperty("nomAgent")
        private String nomAgent;

        @JsonProperty("reclamationsAssignees")
        private Long reclamationsAssignees;

        @JsonProperty("reclamationsResolues")
        private Long reclamationsResolues;

        @JsonProperty("tauxResolution")
        private Double tauxResolution;

        @JsonProperty("tempsResolutionMoyen")
        private Double tempsResolutionMoyen;

        @JsonProperty("satisfactionMoyenne")
        private Double satisfactionMoyenne;
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

        @JsonProperty("nouvellesReclamations")
        private Long nouvellesReclamations;

        @JsonProperty("reclamationsResolues")
        private Long reclamationsResolues;

        @JsonProperty("reclamationsEnCours")
        private Long reclamationsEnCours;
    }

    /**
     * Statistiques de satisfaction client.
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
    }
} 