package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.response.AdminDashboardResponse;
import com.GIRA.Backend.DTO.response.AgentDashboardResponse;
import com.GIRA.Backend.DTO.common.ApiResponse;
import com.GIRA.Backend.service.interfaces.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST controller for dashboard operations.
 * Provides endpoints for admin and agent dashboards with comprehensive statistics and analytics.
 *
 * @author GIRA
 * @since 1.0
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Récupère le tableau de bord administrateur avec toutes les statistiques globales.
     * Inclut les statistiques des réclamations, performances des agents, et tendances.
     *
     * @return tableau de bord administrateur complet
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getAdminDashboard() {
        AdminDashboardResponse dashboard = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(ApiResponse.success("Tableau de bord administrateur récupéré", dashboard));
    }

    /**
     * Récupère le tableau de bord agent avec les statistiques personnelles.
     * Inclut la charge de travail, les performances, et les réclamations assignées.
     *
     * @return tableau de bord agent complet
     */
    @GetMapping("/agent")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponse<AgentDashboardResponse>> getAgentDashboard() {
        // TODO: Extract agent ID from current user context
        String agentId = "current-agent-id"; // Placeholder
        AgentDashboardResponse dashboard = dashboardService.getAgentDashboard(agentId);
        return ResponseEntity.ok(ApiResponse.success("Tableau de bord agent récupéré", dashboard));
    }

    /**
     * Récupère les statistiques de réclamations pour une période donnée.
     *
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return statistiques de la période spécifiée
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getReclamationStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        AdminDashboardResponse statistics = dashboardService.getReclamationStatistics(dateDebut, dateFin);
        return ResponseEntity.ok(ApiResponse.success("Statistiques de réclamations récupérées", statistics));
    }

    /**
     * Récupère les statistiques en temps réel (jour en cours).
     *
     * @return statistiques du jour en cours
     */
    @GetMapping("/realtime")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getRealTimeStatistics() {
        AdminDashboardResponse statistics = dashboardService.getRealTimeStatistics();
        return ResponseEntity.ok(ApiResponse.success("Statistiques en temps réel récupérées", statistics));
    }
} 