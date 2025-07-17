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
import org.springframework.security.core.context.SecurityContextHolder;
import com.GIRA.Backend.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;

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
     * Retrieves the administrator dashboard with all global statistics.
     * Includes complaint statistics, agent performance, and trends.
     *
     * @return ResponseEntity containing the admin dashboard data
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getAdminDashboard() {
        AdminDashboardResponse dashboard = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(ApiResponse.success("Tableau de bord administrateur récupéré", dashboard));
    }

    /**
     * Retrieves the agent dashboard with personal statistics.
     * Includes workload, performance, and assigned complaints.
     *
     * @return ResponseEntity containing the agent dashboard data
     */
    @GetMapping("/agent")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponse<AgentDashboardResponse>> getAgentDashboard() {
        // Extract agent ID from the current user context
        String agentId = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) principal;
                agentId = userPrincipal.getId().toString();
            } else {
                throw new AccessDeniedException("Unable to identify current user");
            }
        } catch (Exception e) {
            throw new AccessDeniedException("Unable to access agent dashboard: " + e.getMessage());
        }
        
        AgentDashboardResponse dashboard = dashboardService.getAgentDashboard(agentId);
        return ResponseEntity.ok(ApiResponse.success("Tableau de bord agent récupéré", dashboard));
    }

    /**
     * Retrieves complaint statistics for a given period.
     *
     * @param dateDebut start date of the period
     * @param dateFin end date of the period
     * @return ResponseEntity containing statistics for the specified period
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
     * Retrieves real-time statistics for the current day.
     *
     * @return ResponseEntity containing today's statistics
     */
    @GetMapping("/realtime")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getRealTimeStatistics() {
        AdminDashboardResponse statistics = dashboardService.getRealTimeStatistics();
        return ResponseEntity.ok(ApiResponse.success("Statistiques en temps réel récupérées", statistics));
    }
} 