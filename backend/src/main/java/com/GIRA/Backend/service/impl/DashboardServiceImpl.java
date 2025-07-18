package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.DTO.response.AdminDashboardResponse;
import com.GIRA.Backend.DTO.response.AgentDashboardResponse;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;
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
import java.util.HashMap;
import java.util.stream.Collectors;

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
        LocalDateTime now = LocalDateTime.now();

        // Calculate various statistics
        long totalReclamations;
        long todayReclamations;
        long pendingReclamations;
        long inProgressReclamations;
        long resolvedReclamations;
        long closedReclamations;
        long urgentReclamations;
        
        try {
            totalReclamations = reclamationRepository.count();
            todayReclamations = reclamationRepository.countByDateCreationBetween(startOfDay, endOfDay);
            pendingReclamations = reclamationRepository.countByStatut(Reclamation.Statut.SOUMISE);
            inProgressReclamations = reclamationRepository.countByStatut(Reclamation.Statut.EN_COURS);
            resolvedReclamations = reclamationRepository.countByStatut(Reclamation.Statut.RESOLUE);
            closedReclamations = reclamationRepository.countByStatut(Reclamation.Statut.FERMEE);
            urgentReclamations = reclamationRepository.countByPriorite(Reclamation.Priorite.URGENTE);
        } catch (Exception e) {
            // Fallback if queries fail
            totalReclamations = 0;
            todayReclamations = 0;
            pendingReclamations = 0;
            inProgressReclamations = 0;
            resolvedReclamations = 0;
            closedReclamations = 0;
            urgentReclamations = 0;
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

        // Calculate category distribution
        Map<String, Long> repartitionParCategorie = new HashMap<>();
        try {
            List<Object[]> categoryStats = reclamationRepository.countByCategorie();
            for (Object[] stat : categoryStats) {
                String categoryName = (String) stat[0];
                Long count = ((Number) stat[1]).longValue();
                repartitionParCategorie.put(categoryName, count);
            }
        } catch (Exception e) {
            // Fallback if query fails
            repartitionParCategorie = Map.of();
        }

        // Calculate priority distribution
        Map<String, Long> repartitionParPriorite = new HashMap<>();
        try {
            List<Object[]> priorityStats = reclamationRepository.countByPrioriteGroup();
            for (Object[] stat : priorityStats) {
                String priority = stat[0].toString();
                Long count = ((Number) stat[1]).longValue();
                repartitionParPriorite.put(priority, count);
            }
        } catch (Exception e) {
            // Fallback if query fails
            repartitionParPriorite = Map.of();
        }

        // Calculate satisfaction statistics
        AdminDashboardResponse.StatistiquesSatisfaction satisfactionStats;
        try {
            List<Reclamation> reclamationsWithSatisfaction = reclamationRepository.findReclamationsWithSatisfaction();
            long totalEvaluations = reclamationsWithSatisfaction.size();
            double noteMoyenne = 0.0;
            Map<Integer, Long> repartitionNotes = new HashMap<>();
            
            if (totalEvaluations > 0) {
                double totalRating = reclamationsWithSatisfaction.stream()
                    .mapToInt(r -> r.getSatisfaction() != null ? r.getSatisfaction() : 0)
                    .sum();
                noteMoyenne = totalRating / totalEvaluations;
                
                // Calculate rating distribution
                repartitionNotes = reclamationsWithSatisfaction.stream()
                    .filter(r -> r.getSatisfaction() != null)
                    .collect(Collectors.groupingBy(
                        r -> r.getSatisfaction(),
                        Collectors.counting()
                    ));
            }
            
            double tauxSatisfaction = totalEvaluations > 0 ? 
                (reclamationsWithSatisfaction.stream()
                    .filter(r -> r.getSatisfaction() != null && r.getSatisfaction() >= 4)
                    .count() * 100.0 / totalEvaluations) : 0.0;
            
            satisfactionStats = AdminDashboardResponse.StatistiquesSatisfaction.builder()
                .noteMoyenne(noteMoyenne)
                .totalEvaluations(totalEvaluations)
                .repartitionNotes(repartitionNotes)
                .tauxSatisfaction(tauxSatisfaction)
                .build();
        } catch (Exception e) {
            satisfactionStats = AdminDashboardResponse.StatistiquesSatisfaction.builder()
                .noteMoyenne(0.0)
                .totalEvaluations(0L)
                .repartitionNotes(Map.of())
                .tauxSatisfaction(0.0)
                .build();
        }

        // Calculate resolution time by category
        Map<String, Double> tempsResolutionMoyen = new HashMap<>();
        try {
            List<Object[]> resolutionStats = reclamationRepository.findAverageResolutionTimeByCategorie();
            for (Object[] stat : resolutionStats) {
                String categoryName = (String) stat[0];
                Double avgTime = stat[1] != null ? ((Number) stat[1]).doubleValue() : 0.0;
                tempsResolutionMoyen.put(categoryName, avgTime);
            }
        } catch (Exception e) {
            tempsResolutionMoyen = Map.of();
        }

        // Admin trends
        List<AdminDashboardResponse.TendanceJournaliere> tendances30Jours;
        try {
            LocalDateTime thirtyDaysAgo = now.minusDays(30);
            List<Object[]> dailyTrends = reclamationRepository.findDailyTrends(thirtyDaysAgo);
            tendances30Jours = dailyTrends.stream()
                .map(trend -> AdminDashboardResponse.TendanceJournaliere.builder()
                    .date(trend[0].toString())
                    .nouvellesReclamations(((Number) trend[1]).longValue())
                    .reclamationsResolues(0L)
                    .reclamationsEnCours(0L)
                    .build())
                .collect(Collectors.toList());
        } catch (Exception e) {
            tendances30Jours = List.of();
        }

        // Create performance agents list with actual data
        List<AdminDashboardResponse.PerformanceAgent> performanceAgents = topAgents.stream()
            .map(user -> {
                try {
                    Object[] stats = userRepository.getAgentPerformanceStats(user.getId());
                    long assigned = stats[0] != null ? ((Number) stats[0]).longValue() : 0L;
                    long resolved = stats[1] != null ? ((Number) stats[1]).longValue() : 0L;
                    double satisfaction = stats[2] != null ? ((Number) stats[2]).doubleValue() : 0.0;
                    double resolutionRate = assigned > 0 ? (double) resolved / assigned * 100 : 0.0;
                    
                    return AdminDashboardResponse.PerformanceAgent.builder()
                        .agentId(user.getId().toString())
                        .nomAgent(user.getNom() + " " + user.getPrenom())
                        .reclamationsAssignees(assigned)
                        .reclamationsResolues(resolved)
                        .tauxResolution(resolutionRate)
                        .tempsResolutionMoyen(reclamationRepository.findAverageResolutionTimeByAgent(user.getId()))
                        .satisfactionMoyenne(satisfaction)
                        .build();
                } catch (Exception e) {
                    return AdminDashboardResponse.PerformanceAgent.builder()
                        .agentId(user.getId().toString())
                        .nomAgent(user.getNom() + " " + user.getPrenom())
                        .reclamationsAssignees(0L)
                        .reclamationsResolues(0L)
                        .tauxResolution(0.0)
                        .tempsResolutionMoyen(0.0)
                        .satisfactionMoyenne(0.0)
                        .build();
                }
            })
            .collect(Collectors.toList());

        // Create statistics globales
        AdminDashboardResponse.StatistiquesGlobales statistiquesGlobales = AdminDashboardResponse.StatistiquesGlobales.builder()
                .totalReclamations(totalReclamations)
                .reclamationsEnCours(inProgressReclamations)
                .reclamationsResolues(resolvedReclamations)
                .reclamationsUrgentes(urgentReclamations)
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
                .repartitionParCategorie(repartitionParCategorie)
                .repartitionParPriorite(repartitionParPriorite)
                .performanceAgents(performanceAgents)
                .tendances30Jours(tendances30Jours)
                .satisfactionClient(satisfactionStats)
                .tempsResolutionMoyen(tempsResolutionMoyen)
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
            LocalDateTime now = LocalDateTime.now();
            
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

            // Calculate urgent and overdue complaints
            long urgentReclamations;
            long overdueReclamations;
            try {
                urgentReclamations = reclamationRepository.countUrgentReclamationsByAgent(agentUuid);
                overdueReclamations = reclamationRepository.countOverdueReclamationsByAgent(agentUuid, now);
            } catch (Exception e) {
                urgentReclamations = 0;
                overdueReclamations = 0;
            }

            // Calculate agent's average resolution time
            Double agentAvgResolutionTime;
            try {
                agentAvgResolutionTime = reclamationRepository.findAverageResolutionTimeByAgent(agentUuid);
            } catch (Exception e) {
                // Fallback if query fails
                agentAvgResolutionTime = 0.0;
            }

            // Calculate priority distribution for agent
            Map<String, Long> repartitionParPriorite = new HashMap<>();
            try {
                List<Object[]> priorityStats = reclamationRepository.countByPrioriteGroupForAgent(agentUuid);
                for (Object[] stat : priorityStats) {
                    String priority = stat[0].toString();
                    Long count = ((Number) stat[1]).longValue();
                    repartitionParPriorite.put(priority, count);
                }
            } catch (Exception e) {
                repartitionParPriorite = Map.of();
            }

            // Calculate category distribution for agent
            Map<String, Long> repartitionParCategorie = new HashMap<>();
            try {
                List<Object[]> categoryStats = reclamationRepository.countByCategorieForAgent(agentUuid);
                for (Object[] stat : categoryStats) {
                    String categoryName = (String) stat[0];
                    Long count = ((Number) stat[1]).longValue();
                    repartitionParCategorie.put(categoryName, count);
                }
            } catch (Exception e) {
                repartitionParCategorie = Map.of();
            }

            // Get urgent and overdue complaints lists
            List<ReclamationListResponse> reclamationsUrgentes;
            List<ReclamationListResponse> reclamationsEnRetard;
            try {
                List<Reclamation> urgentList = reclamationRepository.findUrgentReclamationsByAgent(agentUuid);
                reclamationsUrgentes = urgentList.stream().map(ReclamationListResponse::fromEntity).collect(Collectors.toList());
                List<Reclamation> overdueList = reclamationRepository.findOverdueReclamationsByAgent(agentUuid, now);
                reclamationsEnRetard = overdueList.stream().map(ReclamationListResponse::fromEntity).collect(Collectors.toList());
            } catch (Exception e) {
                reclamationsUrgentes = List.of();
                reclamationsEnRetard = List.of();
            }

            // Calculate satisfaction statistics for agent
            AgentDashboardResponse.StatistiquesSatisfaction satisfactionStats;
            try {
                List<Reclamation> reclamationsWithSatisfaction = reclamationRepository.findReclamationsWithSatisfactionByAgent(agentUuid);
                long totalEvaluations = reclamationsWithSatisfaction.size();
                double noteMoyenne = 0.0;
                Map<Integer, Long> repartitionNotes = new HashMap<>();
                long commentairesPositifs = 0;
                long commentairesNegatifs = 0;
                
                if (totalEvaluations > 0) {
                    double totalRating = reclamationsWithSatisfaction.stream()
                        .mapToInt(r -> r.getSatisfaction() != null ? r.getSatisfaction() : 0)
                        .sum();
                    noteMoyenne = totalRating / totalEvaluations;
                    
                                    // Calculate rating distribution
                repartitionNotes = reclamationsWithSatisfaction.stream()
                    .filter(r -> r.getSatisfaction() != null)
                    .collect(Collectors.groupingBy(
                        r -> r.getSatisfaction(),
                        Collectors.counting()
                    ));
                    
                    // Count positive and negative comments
                    commentairesPositifs = reclamationsWithSatisfaction.stream()
                        .filter(r -> r.getSatisfaction() != null && r.getSatisfaction() >= 4)
                        .count();
                    commentairesNegatifs = reclamationsWithSatisfaction.stream()
                        .filter(r -> r.getSatisfaction() != null && r.getSatisfaction() <= 2)
                        .count();
                }
                
                double tauxSatisfaction = totalEvaluations > 0 ? 
                    (commentairesPositifs * 100.0 / totalEvaluations) : 0.0;
                
                satisfactionStats = AgentDashboardResponse.StatistiquesSatisfaction.builder()
                    .noteMoyenne(noteMoyenne)
                    .totalEvaluations(totalEvaluations)
                    .repartitionNotes(repartitionNotes)
                    .tauxSatisfaction(tauxSatisfaction)
                    .commentairesPositifs(commentairesPositifs)
                    .commentairesNegatifs(commentairesNegatifs)
                    .build();
            } catch (Exception e) {
                satisfactionStats = AgentDashboardResponse.StatistiquesSatisfaction.builder()
                    .noteMoyenne(0.0)
                    .totalEvaluations(0L)
                    .repartitionNotes(Map.of())
                    .tauxSatisfaction(0.0)
                    .commentairesPositifs(0L)
                    .commentairesNegatifs(0L)
                    .build();
            }

            // Agent trends
            List<AgentDashboardResponse.TendanceJournaliere> tendances7Jours;
            try {
                LocalDateTime sevenDaysAgo = now.minusDays(7);
                List<Object[]> dailyTrends = reclamationRepository.findDailyTrendsByAgent(agentUuid, sevenDaysAgo);
                tendances7Jours = dailyTrends.stream()
                    .map(trend -> AgentDashboardResponse.TendanceJournaliere.builder()
                        .date(trend[0].toString())
                        .nouvellesAssignations(((Number) trend[1]).longValue())
                        .reclamationsResolues(0L)
                        .reclamationsEnCours(0L)
                        .build())
                    .collect(Collectors.toList());
            } catch (Exception e) {
                tendances7Jours = List.of();
            }

            // Calculate monthly performance
            AgentDashboardResponse.PerformanceMois performanceMois;
            try {
                LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                long reclamationsTraitees = assignedReclamations.stream()
                    .filter(r -> r.getDateResolution() != null && r.getDateResolution().isAfter(monthStart))
                    .count();
                
                Double tempsResolutionMoyen = reclamationRepository.findAverageResolutionTimeByAgent(agentUuid);
                Double satisfactionMoyenne = userRepository.findAverageSatisfactionByAgent(agentUuid);
                
                // Simple objective: resolve at least 80% of assigned complaints
                boolean objectifAtteint = totalAssigned > 0 && (double) resolvedAssigned / totalAssigned >= 0.8;
                double pourcentageObjectif = totalAssigned > 0 ? (double) resolvedAssigned / totalAssigned * 100 : 100.0;
                
                performanceMois = AgentDashboardResponse.PerformanceMois.builder()
                    .reclamationsTraitees(reclamationsTraitees)
                    .tempsResolutionMoyen(tempsResolutionMoyen != null ? tempsResolutionMoyen : 0.0)
                    .satisfactionMoyenne(satisfactionMoyenne != null ? satisfactionMoyenne : 0.0)
                    .objectifAtteint(objectifAtteint)
                    .pourcentageObjectif(pourcentageObjectif)
                    .build();
            } catch (Exception e) {
                performanceMois = AgentDashboardResponse.PerformanceMois.builder()
                    .reclamationsTraitees(0L)
                    .tempsResolutionMoyen(0.0)
                    .satisfactionMoyenne(0.0)
                    .objectifAtteint(true)
                    .pourcentageObjectif(100.0)
                    .build();
            }

            // Create statistiques personnelles
            AgentDashboardResponse.StatistiquesPersonnelles statistiquesPersonnelles = AgentDashboardResponse.StatistiquesPersonnelles.builder()
                    .totalAssignees(totalAssigned)
                    .reclamationsEnCours(inProgressAssigned)
                    .reclamationsResolues(resolvedAssigned)
                    .reclamationsUrgentes(urgentReclamations)
                    .reclamationsEnRetard(overdueReclamations)
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
                    .repartitionParPriorite(repartitionParPriorite)
                    .repartitionParCategorie(repartitionParCategorie)
                    .reclamationsUrgentes(reclamationsUrgentes)
                    .reclamationsEnRetard(reclamationsEnRetard)
                    .performanceMois(performanceMois)
                    .tendances7Jours(tendances7Jours)
                    .satisfactionClients(satisfactionStats)
                    .dateGeneration(LocalDateTime.now())
                    .build();
        } catch (IllegalArgumentException e) {
            // Invalid UUID format
            throw new IllegalArgumentException("Invalid agent ID format: " + agentId);
        }
    }

    /**
     * Retrieves complaint statistics for a given period.
     *
     * @param dateDebut start date of the period
     * @param dateFin end date of the period
     * @return AdminDashboardResponse containing statistics for the specified period
     */
    @Override
    public AdminDashboardResponse getReclamationStatistics(LocalDate dateDebut, LocalDate dateFin) {
        LocalDateTime startDateTime = dateDebut.atStartOfDay();
        LocalDateTime endDateTime = dateFin.atTime(23, 59, 59);
        LocalDateTime now = LocalDateTime.now();

        // Calculate period statistics
        long totalReclamations;
        long pendingReclamations;
        long inProgressReclamations;
        long resolvedReclamations;
        long closedReclamations;
        long urgentReclamations;
        
        try {
            totalReclamations = reclamationRepository.countByDateCreationBetween(startDateTime, endDateTime);
            pendingReclamations = reclamationRepository.countByStatutAndDateCreationBetween(Reclamation.Statut.SOUMISE, startDateTime, endDateTime);
            inProgressReclamations = reclamationRepository.countByStatutAndDateCreationBetween(Reclamation.Statut.EN_COURS, startDateTime, endDateTime);
            resolvedReclamations = reclamationRepository.countByStatutAndDateCreationBetween(Reclamation.Statut.RESOLUE, startDateTime, endDateTime);
            closedReclamations = reclamationRepository.countByStatutAndDateCreationBetween(Reclamation.Statut.FERMEE, startDateTime, endDateTime);
            urgentReclamations = reclamationRepository.countByPriorite(Reclamation.Priorite.URGENTE);
        } catch (Exception e) {
            // Fallback if queries fail
            totalReclamations = 0;
            pendingReclamations = 0;
            inProgressReclamations = 0;
            resolvedReclamations = 0;
            closedReclamations = 0;
            urgentReclamations = 0;
        }

        // Calculate average resolution time for the period
        Double avgResolutionTime;
        try {
            avgResolutionTime = reclamationRepository.findAverageResolutionTimeBetween(startDateTime, endDateTime);
        } catch (Exception e) {
            avgResolutionTime = 0.0;
        }

        // Calculate category distribution for the period
        Map<String, Long> repartitionParCategorie = new HashMap<>();
        try {
            List<Object[]> categoryStats = reclamationRepository.countByCategorie();
            for (Object[] stat : categoryStats) {
                String categoryName = (String) stat[0];
                Long count = ((Number) stat[1]).longValue();
                repartitionParCategorie.put(categoryName, count);
            }
        } catch (Exception e) {
            repartitionParCategorie = Map.of();
        }

        // Calculate priority distribution for the period
        Map<String, Long> repartitionParPriorite = new HashMap<>();
        try {
            List<Object[]> priorityStats = reclamationRepository.countByPrioriteGroup();
            for (Object[] stat : priorityStats) {
                String priority = stat[0].toString();
                Long count = ((Number) stat[1]).longValue();
                repartitionParPriorite.put(priority, count);
            }
        } catch (Exception e) {
            repartitionParPriorite = Map.of();
        }

        // Calculate satisfaction statistics for the period
        AdminDashboardResponse.StatistiquesSatisfaction satisfactionStats;
        try {
            List<Reclamation> reclamationsWithSatisfaction = reclamationRepository.findReclamationsWithSatisfaction();
            // Filter by date range
            List<Reclamation> periodReclamations = reclamationsWithSatisfaction.stream()
                .filter(r -> r.getDateCreation() != null && 
                           r.getDateCreation().isAfter(startDateTime) && 
                           r.getDateCreation().isBefore(endDateTime))
                .collect(Collectors.toList());
            
            long totalEvaluations = periodReclamations.size();
            double noteMoyenne = 0.0;
            Map<Integer, Long> repartitionNotes = new HashMap<>();
            
            if (totalEvaluations > 0) {
                double totalRating = periodReclamations.stream()
                    .mapToInt(r -> r.getSatisfaction() != null ? r.getSatisfaction() : 0)
                    .sum();
                noteMoyenne = totalRating / totalEvaluations;
                
                // Calculate rating distribution
                repartitionNotes = periodReclamations.stream()
                    .filter(r -> r.getSatisfaction() != null)
                    .collect(Collectors.groupingBy(
                        r -> r.getSatisfaction(),
                        Collectors.counting()
                    ));
            }
            
            double tauxSatisfaction = totalEvaluations > 0 ? 
                (periodReclamations.stream()
                    .filter(r -> r.getSatisfaction() != null && r.getSatisfaction() >= 4)
                    .count() * 100.0 / totalEvaluations) : 0.0;
            
            satisfactionStats = AdminDashboardResponse.StatistiquesSatisfaction.builder()
                .noteMoyenne(noteMoyenne)
                .totalEvaluations(totalEvaluations)
                .repartitionNotes(repartitionNotes)
                .tauxSatisfaction(tauxSatisfaction)
                .build();
        } catch (Exception e) {
            satisfactionStats = AdminDashboardResponse.StatistiquesSatisfaction.builder()
                .noteMoyenne(0.0)
                .totalEvaluations(0L)
                .repartitionNotes(Map.of())
                .tauxSatisfaction(0.0)
                .build();
        }

        // Calculate resolution time by category for the period
        Map<String, Double> tempsResolutionMoyen = new HashMap<>();
        try {
            List<Object[]> resolutionStats = reclamationRepository.findAverageResolutionTimeByCategorie();
            for (Object[] stat : resolutionStats) {
                String categoryName = (String) stat[0];
                Double avgTime = stat[1] != null ? ((Number) stat[1]).doubleValue() : 0.0;
                tempsResolutionMoyen.put(categoryName, avgTime);
            }
        } catch (Exception e) {
            tempsResolutionMoyen = Map.of();
        }

        // Calculate trends for the period
        List<AdminDashboardResponse.TendanceJournaliere> tendances30Jours;
        try {
            List<Object[]> dailyTrends = reclamationRepository.findDailyTrends(startDateTime);
            tendances30Jours = dailyTrends.stream()
                .map(trend -> AdminDashboardResponse.TendanceJournaliere.builder()
                    .date(trend[0].toString())
                    .nouvellesReclamations(((Number) trend[1]).longValue())
                    .reclamationsResolues(0L)
                    .reclamationsEnCours(0L)
                    .build())
                .collect(Collectors.toList());
        } catch (Exception e) {
            tendances30Jours = List.of();
        }

        AdminDashboardResponse.StatistiquesGlobales statistiquesGlobales = AdminDashboardResponse.StatistiquesGlobales.builder()
                .totalReclamations(totalReclamations)
                .reclamationsEnCours(inProgressReclamations)
                .reclamationsResolues(resolvedReclamations)
                .reclamationsUrgentes(urgentReclamations)
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
                .repartitionParCategorie(repartitionParCategorie)
                .repartitionParPriorite(repartitionParPriorite)
                .performanceAgents(List.of()) // Not calculated for period statistics
                .tendances30Jours(tendances30Jours)
                .satisfactionClient(satisfactionStats)
                .tempsResolutionMoyen(tempsResolutionMoyen)
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
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        LocalDateTime now = LocalDateTime.now();

        // Calculate today's statistics
        long todayReclamations;
        long todayPending;
        long todayInProgress;
        long todayResolved;
        long todayClosed;
        long todayUrgent;
        
        try {
            todayReclamations = reclamationRepository.countByDateCreationBetween(startOfDay, endOfDay);
            todayPending = reclamationRepository.countByStatutAndDateCreationBetween(Reclamation.Statut.SOUMISE, startOfDay, endOfDay);
            todayInProgress = reclamationRepository.countByStatutAndDateCreationBetween(Reclamation.Statut.EN_COURS, startOfDay, endOfDay);
            todayResolved = reclamationRepository.countByStatutAndDateCreationBetween(Reclamation.Statut.RESOLUE, startOfDay, endOfDay);
            todayClosed = reclamationRepository.countByStatutAndDateCreationBetween(Reclamation.Statut.FERMEE, startOfDay, endOfDay);
            todayUrgent = reclamationRepository.countByPriorite(Reclamation.Priorite.URGENTE);
        } catch (Exception e) {
            // Fallback if queries fail
            todayReclamations = 0;
            todayPending = 0;
            todayInProgress = 0;
            todayResolved = 0;
            todayClosed = 0;
            todayUrgent = 0;
        }

        // Calculate today's category distribution
        Map<String, Long> repartitionParCategorie = new HashMap<>();
        try {
            List<Object[]> categoryStats = reclamationRepository.countByCategorie();
            for (Object[] stat : categoryStats) {
                String categoryName = (String) stat[0];
                Long count = ((Number) stat[1]).longValue();
                repartitionParCategorie.put(categoryName, count);
            }
        } catch (Exception e) {
            repartitionParCategorie = Map.of();
        }

        // Calculate today's priority distribution
        Map<String, Long> repartitionParPriorite = new HashMap<>();
        try {
            List<Object[]> priorityStats = reclamationRepository.countByPrioriteGroup();
            for (Object[] stat : priorityStats) {
                String priority = stat[0].toString();
                Long count = ((Number) stat[1]).longValue();
                repartitionParPriorite.put(priority, count);
            }
        } catch (Exception e) {
            repartitionParPriorite = Map.of();
        }

        // Calculate today's satisfaction statistics
        AdminDashboardResponse.StatistiquesSatisfaction satisfactionStats;
        try {
            List<Reclamation> reclamationsWithSatisfaction = reclamationRepository.findReclamationsWithSatisfaction();
            // Filter by today
            List<Reclamation> todayReclamationsWithSatisfaction = reclamationsWithSatisfaction.stream()
                .filter(r -> r.getDateCreation() != null && 
                           r.getDateCreation().isAfter(startOfDay) && 
                           r.getDateCreation().isBefore(endOfDay))
                .collect(Collectors.toList());
            
            long totalEvaluations = todayReclamationsWithSatisfaction.size();
            double noteMoyenne = 0.0;
            Map<Integer, Long> repartitionNotes = new HashMap<>();
            
            if (totalEvaluations > 0) {
                double totalRating = todayReclamationsWithSatisfaction.stream()
                    .mapToInt(r -> r.getSatisfaction() != null ? r.getSatisfaction() : 0)
                    .sum();
                noteMoyenne = totalRating / totalEvaluations;
                
                // Calculate rating distribution
                repartitionNotes = todayReclamationsWithSatisfaction.stream()
                    .filter(r -> r.getSatisfaction() != null)
                    .collect(Collectors.groupingBy(
                        r -> r.getSatisfaction(),
                        Collectors.counting()
                    ));
            }
            
            double tauxSatisfaction = totalEvaluations > 0 ? 
                (todayReclamationsWithSatisfaction.stream()
                    .filter(r -> r.getSatisfaction() != null && r.getSatisfaction() >= 4)
                    .count() * 100.0 / totalEvaluations) : 0.0;
            
            satisfactionStats = AdminDashboardResponse.StatistiquesSatisfaction.builder()
                .noteMoyenne(noteMoyenne)
                .totalEvaluations(totalEvaluations)
                .repartitionNotes(repartitionNotes)
                .tauxSatisfaction(tauxSatisfaction)
                .build();
        } catch (Exception e) {
            satisfactionStats = AdminDashboardResponse.StatistiquesSatisfaction.builder()
                .noteMoyenne(0.0)
                .totalEvaluations(0L)
                .repartitionNotes(Map.of())
                .tauxSatisfaction(0.0)
                .build();
        }

        // Calculate today's resolution time by category
        Map<String, Double> tempsResolutionMoyen = new HashMap<>();
        try {
            List<Object[]> resolutionStats = reclamationRepository.findAverageResolutionTimeByCategorie();
            for (Object[] stat : resolutionStats) {
                String categoryName = (String) stat[0];
                Double avgTime = stat[1] != null ? ((Number) stat[1]).doubleValue() : 0.0;
                tempsResolutionMoyen.put(categoryName, avgTime);
            }
        } catch (Exception e) {
            tempsResolutionMoyen = Map.of();
        }

        // Calculate today's trends
        List<AdminDashboardResponse.TendanceJournaliere> tendances30Jours;
        try {
            List<Object[]> dailyTrends = reclamationRepository.findDailyTrends(startOfDay);
            tendances30Jours = dailyTrends.stream()
                .map(trend -> AdminDashboardResponse.TendanceJournaliere.builder()
                    .date(trend[0].toString())
                    .nouvellesReclamations(((Number) trend[1]).longValue())
                    .reclamationsResolues(0L)
                    .reclamationsEnCours(0L)
                    .build())
                .collect(Collectors.toList());
        } catch (Exception e) {
            tendances30Jours = List.of();
        }

        AdminDashboardResponse.StatistiquesGlobales statistiquesGlobales = AdminDashboardResponse.StatistiquesGlobales.builder()
                .totalReclamations(todayReclamations)
                .reclamationsEnCours(todayInProgress)
                .reclamationsResolues(todayResolved)
                .reclamationsUrgentes(todayUrgent)
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
                .repartitionParCategorie(repartitionParCategorie)
                .repartitionParPriorite(repartitionParPriorite)
                .performanceAgents(List.of()) // Not calculated for real-time
                .tendances30Jours(tendances30Jours)
                .satisfactionClient(satisfactionStats)
                .tempsResolutionMoyen(tempsResolutionMoyen)
                .dateGeneration(LocalDateTime.now())
                .build();
    }
} 