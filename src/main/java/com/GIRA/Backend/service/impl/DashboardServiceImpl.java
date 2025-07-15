package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.DTO.response.AdminDashboardResponse;
import com.GIRA.Backend.DTO.response.AgentDashboardResponse;
import com.GIRA.Backend.service.interfaces.DashboardService;
import com.GIRA.Backend.Respository.ReclamationRepository;
import com.GIRA.Backend.Respository.UserRepository;
import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of DashboardService.
 * Provides dashboard data generation for administrators and agents.
 * 
 * @author Mohamed yahya jabrane
 * @since 1.0
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;

    @Autowired
    public DashboardServiceImpl(ReclamationRepository reclamationRepository, UserRepository userRepository) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the administrator dashboard with global statistics, agent performance, and trends.
     *
     * @return AdminDashboardResponse containing all admin dashboard data
     */
    @Override
    public AdminDashboardResponse getAdminDashboard() {
        // Get current date for calculations
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // Calculate various statistics
        long totalReclamations = reclamationRepository.count();
        long todayReclamations;
        long pendingReclamations;
        long inProgressReclamations;
        long resolvedReclamations;
        long closedReclamations;
        
        try {
            todayReclamations = reclamationRepository.countByDateCreationBetween(startOfDay, endOfDay);
            pendingReclamations = reclamationRepository.countByStatut(Reclamation.Statut.SOUMISE);
            inProgressReclamations = reclamationRepository.countByStatut(Reclamation.Statut.EN_COURS);
            resolvedReclamations = reclamationRepository.countByStatut(Reclamation.Statut.RESOLUE);
            closedReclamations = reclamationRepository.countByStatut(Reclamation.Statut.FERMEE);
        } catch (Exception e) {
            // Fallback if queries fail
            todayReclamations = 0;
            pendingReclamations = 0;
            inProgressReclamations = 0;
            resolvedReclamations = 0;
            closedReclamations = 0;
        }

        // Calculate average resolution time (in hours)
        Double avgResolutionTime;
        try {
            avgResolutionTime = reclamationRepository.findAverageResolutionTime();
        } catch (Exception e) {
            // Fallback if query fails
            avgResolutionTime = 0.0;
        }

        // Get top performing agents
        List<User> topAgents;
        try {
            topAgents = userRepository.findTopAgentsByResolutionCount(5);
            if (topAgents == null) {
                topAgents = List.of();
            }
        } catch (Exception e) {
            // Fallback if query fails
            topAgents = List.of();
        }

        // Create statistics globales
        AdminDashboardResponse.StatistiquesGlobales statistiquesGlobales = AdminDashboardResponse.StatistiquesGlobales.builder()
                .totalReclamations(totalReclamations)
                .reclamationsEnCours(inProgressReclamations)
                .reclamationsResolues(resolvedReclamations)
                .reclamationsUrgentes(0L) // TODO: Calculate urgent reclamations
                .tauxResolution(totalReclamations > 0 ? (double) resolvedReclamations / totalReclamations * 100 : 0.0)
                .tempsResolutionMoyen(avgResolutionTime != null ? avgResolutionTime : 0.0)
                .build();

        // Create performance agents list
        List<AdminDashboardResponse.PerformanceAgent> performanceAgents = topAgents.stream()
                .map(user -> AdminDashboardResponse.PerformanceAgent.builder()
                        .agentId(user.getId().toString())
                        .nomAgent(user.getNom() + " " + user.getPrenom())
                        .reclamationsAssignees(0L) // TODO: Calculate assigned reclamations
                        .reclamationsResolues(0L) // TODO: Calculate resolved reclamations
                        .tauxResolution(0.0) // TODO: Calculate resolution rate
                        .tempsResolutionMoyen(0.0) // TODO: Calculate average resolution time
                        .satisfactionMoyenne(0.0) // TODO: Calculate satisfaction
                        .build())
                .toList();

        return AdminDashboardResponse.builder()
                .statistiquesGlobales(statistiquesGlobales)
                .repartitionParStatut(Map.of(
                        "SOUMISE", pendingReclamations,
                        "EN_COURS", inProgressReclamations,
                        "RESOLUE", resolvedReclamations,
                        "FERMEE", closedReclamations
                ))
                .repartitionParCategorie(Map.of()) // TODO: Calculate by category
                .repartitionParPriorite(Map.of()) // TODO: Calculate by priority
                .performanceAgents(performanceAgents)
                .tendances30Jours(List.of()) // TODO: Calculate trends
                .satisfactionClient(AdminDashboardResponse.StatistiquesSatisfaction.builder()
                        .noteMoyenne(0.0)
                        .totalEvaluations(0L)
                        .repartitionNotes(Map.of())
                        .tauxSatisfaction(0.0)
                        .build())
                .tempsResolutionMoyen(Map.of()) // TODO: Calculate by category
                .dateGeneration(LocalDateTime.now())
                .build();
    }

    /**
     * Retrieves the agent dashboard with personal statistics, workload, and performance.
     *
     * @param agentId the agent's UUID as a string
     * @return AgentDashboardResponse containing all agent dashboard data
     */
    @Override
    public AgentDashboardResponse getAgentDashboard(String agentId) {
        try {
            UUID agentUuid = UUID.fromString(agentId);
            
            // Get agent's assigned reclamations
            List<Reclamation> assignedReclamations;
            try {
                assignedReclamations = reclamationRepository.findByAgentAssigne_Id(agentUuid);
            } catch (Exception e) {
                // Fallback if query fails
                assignedReclamations = List.of();
            }
            
            // Calculate agent-specific statistics
            long totalAssigned = assignedReclamations.size();
            long pendingAssigned = assignedReclamations.stream()
                    .filter(r -> r.getStatut() == Reclamation.Statut.SOUMISE)
                    .count();
            long inProgressAssigned = assignedReclamations.stream()
                    .filter(r -> r.getStatut() == Reclamation.Statut.EN_COURS)
                    .count();
            long resolvedAssigned = assignedReclamations.stream()
                    .filter(r -> r.getStatut() == Reclamation.Statut.RESOLUE)
                    .count();

            // Calculate agent's average resolution time
            Double agentAvgResolutionTime;
            try {
                agentAvgResolutionTime = reclamationRepository.findAverageResolutionTimeByAgent(agentUuid);
            } catch (Exception e) {
                // Fallback if query fails
                agentAvgResolutionTime = 0.0;
            }

            // Create statistiques personnelles
            AgentDashboardResponse.StatistiquesPersonnelles statistiquesPersonnelles = AgentDashboardResponse.StatistiquesPersonnelles.builder()
                    .totalAssignees(totalAssigned)
                    .reclamationsEnCours(inProgressAssigned)
                    .reclamationsResolues(resolvedAssigned)
                    .reclamationsUrgentes(0L) // TODO: Calculate urgent reclamations
                    .reclamationsEnRetard(0L) // TODO: Calculate overdue reclamations
                    .tauxResolution(totalAssigned > 0 ? (double) resolvedAssigned / totalAssigned * 100 : 0.0)
                    .tempsResolutionMoyen(agentAvgResolutionTime != null ? agentAvgResolutionTime : 0.0)
                    .chargeTravail(totalAssigned < 5 ? "FAIBLE" : totalAssigned < 15 ? "NORMALE" : "ELEVEE")
                    .build();

            return AgentDashboardResponse.builder()
                    .statistiquesPersonnelles(statistiquesPersonnelles)
                    .repartitionParStatut(Map.of(
                            "SOUMISE", pendingAssigned,
                            "EN_COURS", inProgressAssigned,
                            "RESOLUE", resolvedAssigned
                    ))
                    .repartitionParPriorite(Map.of()) // TODO: Calculate by priority
                    .repartitionParCategorie(Map.of()) // TODO: Calculate by category
                    .reclamationsUrgentes(List.of()) // TODO: Get urgent reclamations
                    .reclamationsEnRetard(List.of()) // TODO: Get overdue reclamations
                    .performanceMois(AgentDashboardResponse.PerformanceMois.builder()
                            .reclamationsTraitees(resolvedAssigned)
                            .tempsResolutionMoyen(agentAvgResolutionTime != null ? agentAvgResolutionTime : 0.0)
                            .satisfactionMoyenne(0.0) // TODO: Calculate satisfaction
                            .objectifAtteint(true) // TODO: Check if objective met
                            .pourcentageObjectif(100.0) // TODO: Calculate percentage
                            .build())
                    .tendances7Jours(List.of()) // TODO: Calculate trends
                    .satisfactionClients(AgentDashboardResponse.StatistiquesSatisfaction.builder()
                            .noteMoyenne(0.0)
                            .totalEvaluations(0L)
                            .repartitionNotes(Map.of())
                            .tauxSatisfaction(0.0)
                            .commentairesPositifs(0L)
                            .commentairesNegatifs(0L)
                            .build())
                    .dateGeneration(LocalDateTime.now())
                    .build();
        } catch (IllegalArgumentException e) {
            // Return empty dashboard if agent ID is invalid
            AgentDashboardResponse.StatistiquesPersonnelles emptyStats = AgentDashboardResponse.StatistiquesPersonnelles.builder()
                    .totalAssignees(0L)
                    .reclamationsEnCours(0L)
                    .reclamationsResolues(0L)
                    .reclamationsUrgentes(0L)
                    .reclamationsEnRetard(0L)
                    .tauxResolution(0.0)
                    .tempsResolutionMoyen(0.0)
                    .chargeTravail("FAIBLE")
                    .build();

            return AgentDashboardResponse.builder()
                    .statistiquesPersonnelles(emptyStats)
                    .repartitionParStatut(Map.of())
                    .repartitionParPriorite(Map.of())
                    .repartitionParCategorie(Map.of())
                    .reclamationsUrgentes(List.of())
                    .reclamationsEnRetard(List.of())
                    .performanceMois(AgentDashboardResponse.PerformanceMois.builder()
                            .reclamationsTraitees(0L)
                            .tempsResolutionMoyen(0.0)
                            .satisfactionMoyenne(0.0)
                            .objectifAtteint(false)
                            .pourcentageObjectif(0.0)
                            .build())
                    .tendances7Jours(List.of())
                    .satisfactionClients(AgentDashboardResponse.StatistiquesSatisfaction.builder()
                            .noteMoyenne(0.0)
                            .totalEvaluations(0L)
                            .repartitionNotes(Map.of())
                            .tauxSatisfaction(0.0)
                            .commentairesPositifs(0L)
                            .commentairesNegatifs(0L)
                            .build())
                    .dateGeneration(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * Retrieves complaint statistics for a given period.
     *
     * @param dateDebut start date of the period
     * @param dateFin end date of the period
     * @return AdminDashboardResponse containing statistics for the period
     */
    @Override
    public AdminDashboardResponse getReclamationStatistics(LocalDate dateDebut, LocalDate dateFin) {
        LocalDateTime startDateTime = dateDebut.atStartOfDay();
        LocalDateTime endDateTime = dateFin.atTime(23, 59, 59);

        // Get reclamations for the specified period
        List<Reclamation> periodReclamations;
        try {
            periodReclamations = reclamationRepository.findByDateCreationBetween(startDateTime, endDateTime);
        } catch (Exception e) {
            // Fallback if query fails
            periodReclamations = List.of();
        }

        // Calculate statistics for the period
        long totalReclamations = periodReclamations.size();
        long pendingReclamations = periodReclamations.stream()
                .filter(r -> r.getStatut() == Reclamation.Statut.SOUMISE)
                .count();
        long inProgressReclamations = periodReclamations.stream()
                .filter(r -> r.getStatut() == Reclamation.Statut.EN_COURS)
                .count();
        long resolvedReclamations = periodReclamations.stream()
                .filter(r -> r.getStatut() == Reclamation.Statut.RESOLUE)
                .count();
        long closedReclamations = periodReclamations.stream()
                .filter(r -> r.getStatut() == Reclamation.Statut.FERMEE)
                .count();

        // Calculate average resolution time for the period
        Double avgResolutionTime;
        try {
            avgResolutionTime = reclamationRepository.findAverageResolutionTimeBetween(startDateTime, endDateTime);
        } catch (Exception e) {
            // Fallback if query fails
            avgResolutionTime = 0.0;
        }

        // Create statistics globales for period
        AdminDashboardResponse.StatistiquesGlobales statistiquesGlobales = AdminDashboardResponse.StatistiquesGlobales.builder()
                .totalReclamations(totalReclamations)
                .reclamationsEnCours(inProgressReclamations)
                .reclamationsResolues(resolvedReclamations)
                .reclamationsUrgentes(0L) // TODO: Calculate urgent reclamations
                .tauxResolution(totalReclamations > 0 ? (double) resolvedReclamations / totalReclamations * 100 : 0.0)
                .tempsResolutionMoyen(avgResolutionTime != null ? avgResolutionTime : 0.0)
                .build();

        return AdminDashboardResponse.builder()
                .statistiquesGlobales(statistiquesGlobales)
                .repartitionParStatut(Map.of(
                        "SOUMISE", pendingReclamations,
                        "EN_COURS", inProgressReclamations,
                        "RESOLUE", resolvedReclamations,
                        "FERMEE", closedReclamations
                ))
                .repartitionParCategorie(Map.of()) // TODO: Calculate by category
                .repartitionParPriorite(Map.of()) // TODO: Calculate by priority
                .performanceAgents(List.of()) // Not calculated for period statistics
                .tendances30Jours(List.of()) // TODO: Calculate trends
                .satisfactionClient(AdminDashboardResponse.StatistiquesSatisfaction.builder()
                        .noteMoyenne(0.0)
                        .totalEvaluations(0L)
                        .repartitionNotes(Map.of())
                        .tauxSatisfaction(0.0)
                        .build())
                .tempsResolutionMoyen(Map.of()) // TODO: Calculate by category
                .dateGeneration(LocalDateTime.now())
                .build();
    }

    /**
     * Retrieves real-time statistics for the current day.
     *
     * @return AdminDashboardResponse containing today's statistics
     */
    @Override
    public AdminDashboardResponse getRealTimeStatistics() {
        // Get today's statistics (same as getAdminDashboard but focused on today)
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        long todayReclamations;
        long todayPending;
        long todayInProgress;
        long todayResolved;
        long todayClosed;
        
        try {
            todayReclamations = reclamationRepository.countByDateCreationBetween(startOfDay, endOfDay);
            todayPending = reclamationRepository.countByStatutAndDateCreationBetween(
                    Reclamation.Statut.SOUMISE, startOfDay, endOfDay);
            todayInProgress = reclamationRepository.countByStatutAndDateCreationBetween(
                    Reclamation.Statut.EN_COURS, startOfDay, endOfDay);
            todayResolved = reclamationRepository.countByStatutAndDateCreationBetween(
                    Reclamation.Statut.RESOLUE, startOfDay, endOfDay);
            todayClosed = reclamationRepository.countByStatutAndDateCreationBetween(
                    Reclamation.Statut.FERMEE, startOfDay, endOfDay);
        } catch (Exception e) {
            // Fallback if queries fail
            todayReclamations = 0;
            todayPending = 0;
            todayInProgress = 0;
            todayResolved = 0;
            todayClosed = 0;
        }

        // Create statistics globales for real-time
        AdminDashboardResponse.StatistiquesGlobales statistiquesGlobales = AdminDashboardResponse.StatistiquesGlobales.builder()
                .totalReclamations(todayReclamations)
                .reclamationsEnCours(todayInProgress)
                .reclamationsResolues(todayResolved)
                .reclamationsUrgentes(0L) // TODO: Calculate urgent reclamations
                .tauxResolution(todayReclamations > 0 ? (double) todayResolved / todayReclamations * 100 : 0.0)
                .tempsResolutionMoyen(0.0) // Not calculated for real-time
                .build();

        return AdminDashboardResponse.builder()
                .statistiquesGlobales(statistiquesGlobales)
                .repartitionParStatut(Map.of(
                        "SOUMISE", todayPending,
                        "EN_COURS", todayInProgress,
                        "RESOLUE", todayResolved,
                        "FERMEE", todayClosed
                ))
                .repartitionParCategorie(Map.of()) // TODO: Calculate by category
                .repartitionParPriorite(Map.of()) // TODO: Calculate by priority
                .performanceAgents(List.of()) // Not calculated for real-time
                .tendances30Jours(List.of()) // TODO: Calculate trends
                .satisfactionClient(AdminDashboardResponse.StatistiquesSatisfaction.builder()
                        .noteMoyenne(0.0)
                        .totalEvaluations(0L)
                        .repartitionNotes(Map.of())
                        .tauxSatisfaction(0.0)
                        .build())
                .tempsResolutionMoyen(Map.of()) // TODO: Calculate by category
                .dateGeneration(LocalDateTime.now())
                .build();
    }
} 