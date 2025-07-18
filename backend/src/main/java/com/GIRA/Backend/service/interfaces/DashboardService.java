package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.DTO.response.AdminDashboardResponse;
import com.GIRA.Backend.DTO.response.AgentDashboardResponse;

/**
 * Service interface for dashboard operations.
 * Provides methods to generate comprehensive dashboard data for administrators and agents.
 *
 * @author GIRA
 * @since 1.0
 */
public interface DashboardService {

    /**
     * Génère le tableau de bord administrateur avec toutes les statistiques globales.
     * Inclut les statistiques des réclamations, performances des agents, et tendances.
     *
     * @return DTO contenant toutes les données du tableau de bord administrateur
     */
    AdminDashboardResponse getAdminDashboard();

    /**
     * Génère le tableau de bord agent avec les statistiques personnelles.
     * Inclut la charge de travail, les performances, et les réclamations assignées.
     *
     * @param agentId identifiant de l'agent
     * @return DTO contenant toutes les données du tableau de bord agent
     */
    AgentDashboardResponse getAgentDashboard(String agentId);

    /**
     * Génère les statistiques de réclamations pour une période donnée.
     *
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return DTO contenant les statistiques de la période
     */
    AdminDashboardResponse getReclamationStatistics(java.time.LocalDate dateDebut, java.time.LocalDate dateFin);

    /**
     * Génère les statistiques en temps réel (jour en cours).
     *
     * @return DTO contenant les statistiques du jour en cours
     */
    AdminDashboardResponse getRealTimeStatistics();
} 