package com.GIRA.Backend.service;

import com.GIRA.Backend.service.impl.DashboardServiceImpl;
import com.GIRA.Backend.Respository.ReclamationRepository;
import com.GIRA.Backend.Respository.UserRepository;
import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.DTO.response.AdminDashboardResponse;
import com.GIRA.Backend.DTO.response.AgentDashboardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private ReclamationRepository reclamationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private User testAgent;
    private Reclamation testReclamation;
    private Reclamation resolvedReclamation;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        // Setup test agent
        testAgent = new User();
        testAgent.setId(UUID.randomUUID());
        testAgent.setNom("Doe");
        testAgent.setPrenom("John");
        testAgent.setEmail("john.doe@example.com");
        
        // Setup test reclamation (resolved for agent dashboard)
        testReclamation = new Reclamation();
        testReclamation.setId(UUID.randomUUID());
        testReclamation.setTitre("Test Complaint");
        testReclamation.setStatut(Reclamation.Statut.RESOLUE);
        testReclamation.setPriorite(Reclamation.Priorite.NORMALE);
        testReclamation.setDateCreation(now.minusDays(1));
        testReclamation.setDateResolution(now.minusHours(1));
        testReclamation.setSatisfaction(4); // Has satisfaction rating
        
        // Setup resolved reclamation (for satisfaction calculations)
        resolvedReclamation = new Reclamation();
        resolvedReclamation.setId(UUID.randomUUID());
        resolvedReclamation.setTitre("Resolved Complaint");
        resolvedReclamation.setStatut(Reclamation.Statut.RESOLUE);
        resolvedReclamation.setPriorite(Reclamation.Priorite.NORMALE);
        resolvedReclamation.setDateCreation(now.minusHours(2)); // Created today
        resolvedReclamation.setDateResolution(now.minusHours(1));
        resolvedReclamation.setSatisfaction(4); // Has satisfaction rating
    }

    @Test
    void getAdminDashboard_ShouldReturnCompleteDashboard() {
        // Arrange
        when(reclamationRepository.count()).thenReturn(100L);
        when(reclamationRepository.countByDateCreationBetween(any(), any())).thenReturn(10L);
        when(reclamationRepository.countByStatut(Reclamation.Statut.SOUMISE)).thenReturn(20L);
        when(reclamationRepository.countByStatut(Reclamation.Statut.EN_COURS)).thenReturn(30L);
        when(reclamationRepository.countByStatut(Reclamation.Statut.RESOLUE)).thenReturn(40L);
        when(reclamationRepository.countByStatut(Reclamation.Statut.FERMEE)).thenReturn(10L);
        when(reclamationRepository.countByPriorite(Reclamation.Priorite.URGENTE)).thenReturn(5L);
        when(reclamationRepository.findAverageResolutionTime()).thenReturn(24.5);
        
        List<User> topAgents = Arrays.asList(testAgent);
        when(userRepository.findTopAgentsByResolutionCount(5)).thenReturn(topAgents);
        
        Object[] agentStats = {10L, 8L, 4.2};
        when(userRepository.getAgentPerformanceStats(testAgent.getId())).thenReturn(agentStats);
        when(reclamationRepository.findAverageResolutionTimeByAgent(testAgent.getId())).thenReturn(20.0);
        
        List<Object[]> categoryStats = Arrays.asList(
            new Object[]{"Retards", 30L},
            new Object[]{"Bagages", 25L}
        );
        when(reclamationRepository.countByCategorie()).thenReturn(categoryStats);
        
        List<Object[]> priorityStats = Arrays.asList(
            new Object[]{Reclamation.Priorite.NORMALE, 70L},
            new Object[]{Reclamation.Priorite.URGENTE, 5L}
        );
        when(reclamationRepository.countByPrioriteGroup()).thenReturn(priorityStats);
        
        // Use resolved reclamation for satisfaction calculations
        List<Reclamation> reclamationsWithSatisfaction = Arrays.asList(resolvedReclamation);
        when(reclamationRepository.findReclamationsWithSatisfaction()).thenReturn(reclamationsWithSatisfaction);
        
        List<Object[]> resolutionStats = Arrays.asList(
            new Object[]{"Retards", 18.5},
            new Object[]{"Bagages", 30.2}
        );
        when(reclamationRepository.findAverageResolutionTimeByCategorie()).thenReturn(resolutionStats);
        
        List<Object[]> dailyTrends = Arrays.asList(
            new Object[]{"2025-01-01", 5L},
            new Object[]{"2025-01-02", 8L}
        );
        when(reclamationRepository.findDailyTrends(any())).thenReturn(dailyTrends);

        // Act
        AdminDashboardResponse result = dashboardService.getAdminDashboard();

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getStatistiquesGlobales().getTotalReclamations());
        assertEquals(30L, result.getStatistiquesGlobales().getReclamationsEnCours());
        assertEquals(40L, result.getStatistiquesGlobales().getReclamationsResolues());
        assertEquals(5L, result.getStatistiquesGlobales().getReclamationsUrgentes());
        assertEquals(40.0, result.getStatistiquesGlobales().getTauxResolution());
        assertEquals(24.5, result.getStatistiquesGlobales().getTempsResolutionMoyen());
        
        // Verify category distribution
        assertEquals(2, result.getRepartitionParCategorie().size());
        assertEquals(30L, result.getRepartitionParCategorie().get("Retards"));
        assertEquals(25L, result.getRepartitionParCategorie().get("Bagages"));
        
        // Verify priority distribution
        assertEquals(2, result.getRepartitionParPriorite().size());
        assertEquals(70L, result.getRepartitionParPriorite().get("NORMALE"));
        assertEquals(5L, result.getRepartitionParPriorite().get("URGENTE"));
        
        // Verify satisfaction statistics
        assertEquals(4.0, result.getSatisfactionClient().getNoteMoyenne());
        assertEquals(1L, result.getSatisfactionClient().getTotalEvaluations());
        assertEquals(100.0, result.getSatisfactionClient().getTauxSatisfaction());
        
        // Verify resolution time by category
        assertEquals(2, result.getTempsResolutionMoyen().size());
        assertEquals(18.5, result.getTempsResolutionMoyen().get("Retards"));
        assertEquals(30.2, result.getTempsResolutionMoyen().get("Bagages"));
        
        // Verify trends
        assertEquals(2, result.getTendances30Jours().size());
        
        // Verify performance agents
        assertEquals(1, result.getPerformanceAgents().size());
        AdminDashboardResponse.PerformanceAgent agent = result.getPerformanceAgents().get(0);
        assertEquals(testAgent.getId().toString(), agent.getAgentId());
        assertEquals("Doe John", agent.getNomAgent());
        assertEquals(10L, agent.getReclamationsAssignees());
        assertEquals(8L, agent.getReclamationsResolues());
        assertEquals(80.0, agent.getTauxResolution());
        assertEquals(20.0, agent.getTempsResolutionMoyen());
        assertEquals(4.2, agent.getSatisfactionMoyenne());
    }

    @Test
    void getAgentDashboard_ShouldReturnCompleteAgentDashboard() {
        // Arrange
        String agentId = testAgent.getId().toString();
        List<Reclamation> assignedReclamations = Arrays.asList(testReclamation);
        
        when(reclamationRepository.findByAgentAssigne_Id(testAgent.getId())).thenReturn(assignedReclamations);
        when(reclamationRepository.countUrgentReclamationsByAgent(testAgent.getId())).thenReturn(2L);
        when(reclamationRepository.countOverdueReclamationsByAgent(eq(testAgent.getId()), any())).thenReturn(1L);
        when(reclamationRepository.findAverageResolutionTimeByAgent(testAgent.getId())).thenReturn(18.5);
        
        List<Object[]> priorityStats = Arrays.asList(
            new Object[]{Reclamation.Priorite.NORMALE, 8L},
            new Object[]{Reclamation.Priorite.URGENTE, 2L}
        );
        when(reclamationRepository.countByPrioriteGroupForAgent(testAgent.getId())).thenReturn(priorityStats);
        
        List<Object[]> categoryStats = Arrays.asList(
            new Object[]{"Retards", 5L},
            new Object[]{"Bagages", 5L}
        );
        when(reclamationRepository.countByCategorieForAgent(testAgent.getId())).thenReturn(categoryStats);
        
        List<Reclamation> urgentReclamations = Arrays.asList(testReclamation);
        when(reclamationRepository.findUrgentReclamationsByAgent(testAgent.getId())).thenReturn(urgentReclamations);
        
        List<Reclamation> overdueReclamations = Arrays.asList(testReclamation);
        when(reclamationRepository.findOverdueReclamationsByAgent(eq(testAgent.getId()), any())).thenReturn(overdueReclamations);
        
        // Use resolved reclamation for satisfaction calculations
        List<Reclamation> reclamationsWithSatisfaction = Arrays.asList(resolvedReclamation);
        when(reclamationRepository.findReclamationsWithSatisfactionByAgent(testAgent.getId())).thenReturn(reclamationsWithSatisfaction);
        
        List<Object[]> dailyTrends = Arrays.asList(
            new Object[]{"2025-01-01", 2L},
            new Object[]{"2025-01-02", 3L}
        );
        when(reclamationRepository.findDailyTrendsByAgent(eq(testAgent.getId()), any())).thenReturn(dailyTrends);
        
        when(userRepository.findAverageSatisfactionByAgent(testAgent.getId())).thenReturn(4.2);

        // Act
        AgentDashboardResponse result = dashboardService.getAgentDashboard(agentId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getStatistiquesPersonnelles().getTotalAssignees());
        assertEquals(0L, result.getStatistiquesPersonnelles().getReclamationsEnCours());
        assertEquals(1L, result.getStatistiquesPersonnelles().getReclamationsResolues());
        assertEquals(2L, result.getStatistiquesPersonnelles().getReclamationsUrgentes());
        assertEquals(1L, result.getStatistiquesPersonnelles().getReclamationsEnRetard());
        assertEquals(100.0, result.getStatistiquesPersonnelles().getTauxResolution());
        assertEquals(18.5, result.getStatistiquesPersonnelles().getTempsResolutionMoyen());
        assertEquals("FAIBLE", result.getStatistiquesPersonnelles().getChargeTravail());
        
        // Verify priority distribution
        assertEquals(2, result.getRepartitionParPriorite().size());
        assertEquals(8L, result.getRepartitionParPriorite().get("NORMALE"));
        assertEquals(2L, result.getRepartitionParPriorite().get("URGENTE"));
        
        // Verify category distribution
        assertEquals(2, result.getRepartitionParCategorie().size());
        assertEquals(5L, result.getRepartitionParCategorie().get("Retards"));
        assertEquals(5L, result.getRepartitionParCategorie().get("Bagages"));
        
        // Verify urgent complaints
        assertEquals(1, result.getReclamationsUrgentes().size());
        assertEquals(testReclamation.getId().toString(), result.getReclamationsUrgentes().get(0).getId());
        assertEquals("Test Complaint", result.getReclamationsUrgentes().get(0).getTitre());
        
        // Verify overdue complaints
        assertEquals(1, result.getReclamationsEnRetard().size());
        assertEquals(testReclamation.getId().toString(), result.getReclamationsEnRetard().get(0).getId());
        assertEquals("Test Complaint", result.getReclamationsEnRetard().get(0).getTitre());
        
        // Verify satisfaction statistics
        assertEquals(4.0, result.getSatisfactionClients().getNoteMoyenne());
        assertEquals(1L, result.getSatisfactionClients().getTotalEvaluations());
        assertEquals(100.0, result.getSatisfactionClients().getTauxSatisfaction());
        assertEquals(1L, result.getSatisfactionClients().getCommentairesPositifs());
        assertEquals(0L, result.getSatisfactionClients().getCommentairesNegatifs());
        
        // Verify trends
        assertEquals(2, result.getTendances7Jours().size());
        
        // Verify monthly performance
        assertEquals(1L, result.getPerformanceMois().getReclamationsTraitees());
        assertEquals(18.5, result.getPerformanceMois().getTempsResolutionMoyen());
        assertEquals(4.2, result.getPerformanceMois().getSatisfactionMoyenne());
        assertTrue(result.getPerformanceMois().getObjectifAtteint());
        assertEquals(100.0, result.getPerformanceMois().getPourcentageObjectif());
    }

    @Test
    void getAgentDashboard_WithInvalidAgentId_ShouldThrowException() {
        // Arrange
        String invalidAgentId = "invalid-uuid";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            dashboardService.getAgentDashboard(invalidAgentId);
        });
    }

    @Test
    void getReclamationStatistics_ShouldReturnPeriodStatistics() {
        // Arrange
        LocalDate dateDebut = LocalDate.now().minusDays(30);
        LocalDate dateFin = LocalDate.now();
        
        when(reclamationRepository.countByDateCreationBetween(any(), any())).thenReturn(50L);
        when(reclamationRepository.countByStatutAndDateCreationBetween(eq(Reclamation.Statut.SOUMISE), any(), any())).thenReturn(15L);
        when(reclamationRepository.countByStatutAndDateCreationBetween(eq(Reclamation.Statut.EN_COURS), any(), any())).thenReturn(15L);
        when(reclamationRepository.countByStatutAndDateCreationBetween(eq(Reclamation.Statut.RESOLUE), any(), any())).thenReturn(20L);
        when(reclamationRepository.countByStatutAndDateCreationBetween(eq(Reclamation.Statut.FERMEE), any(), any())).thenReturn(0L);
        when(reclamationRepository.countByPriorite(Reclamation.Priorite.URGENTE)).thenReturn(3L);
        when(reclamationRepository.findAverageResolutionTimeBetween(any(), any())).thenReturn(22.5);
        
        List<Object[]> categoryStats = Arrays.asList(
            new Object[]{"Retards", 20L},
            new Object[]{"Bagages", 15L}
        );
        when(reclamationRepository.countByCategorie()).thenReturn(categoryStats);
        
        List<Object[]> priorityStats = Arrays.asList(
            new Object[]{Reclamation.Priorite.NORMALE, 35L},
            new Object[]{Reclamation.Priorite.URGENTE, 3L}
        );
        when(reclamationRepository.countByPrioriteGroup()).thenReturn(priorityStats);
        
        // Use resolved reclamation for satisfaction calculations
        List<Reclamation> reclamationsWithSatisfaction = Arrays.asList(resolvedReclamation);
        when(reclamationRepository.findReclamationsWithSatisfaction()).thenReturn(reclamationsWithSatisfaction);
        
        List<Object[]> resolutionStats = Arrays.asList(
            new Object[]{"Retards", 20.0},
            new Object[]{"Bagages", 25.0}
        );
        when(reclamationRepository.findAverageResolutionTimeByCategorie()).thenReturn(resolutionStats);
        
        List<Object[]> dailyTrends = Arrays.asList(
            new Object[]{"2025-01-01", 3L},
            new Object[]{"2025-01-02", 5L}
        );
        when(reclamationRepository.findDailyTrends(any())).thenReturn(dailyTrends);

        // Act
        AdminDashboardResponse result = dashboardService.getReclamationStatistics(dateDebut, dateFin);

        // Assert
        assertNotNull(result);
        assertEquals(50L, result.getStatistiquesGlobales().getTotalReclamations());
        assertEquals(15L, result.getStatistiquesGlobales().getReclamationsEnCours());
        assertEquals(20L, result.getStatistiquesGlobales().getReclamationsResolues());
        assertEquals(3L, result.getStatistiquesGlobales().getReclamationsUrgentes());
        assertEquals(40.0, result.getStatistiquesGlobales().getTauxResolution());
        assertEquals(22.5, result.getStatistiquesGlobales().getTempsResolutionMoyen());
        
        // Verify category distribution
        assertEquals(2, result.getRepartitionParCategorie().size());
        assertEquals(20L, result.getRepartitionParCategorie().get("Retards"));
        assertEquals(15L, result.getRepartitionParCategorie().get("Bagages"));
        
        // Verify priority distribution
        assertEquals(2, result.getRepartitionParPriorite().size());
        assertEquals(35L, result.getRepartitionParPriorite().get("NORMALE"));
        assertEquals(3L, result.getRepartitionParPriorite().get("URGENTE"));
        
        // Verify satisfaction statistics
        assertEquals(4.0, result.getSatisfactionClient().getNoteMoyenne());
        assertEquals(1L, result.getSatisfactionClient().getTotalEvaluations());
        assertEquals(100.0, result.getSatisfactionClient().getTauxSatisfaction());
        
        // Verify resolution time by category
        assertEquals(2, result.getTempsResolutionMoyen().size());
        assertEquals(20.0, result.getTempsResolutionMoyen().get("Retards"));
        assertEquals(25.0, result.getTempsResolutionMoyen().get("Bagages"));
        
        // Verify trends
        assertEquals(2, result.getTendances30Jours().size());
    }

    @Test
    void getRealTimeStatistics_ShouldReturnTodayStatistics() {
        // Arrange
        when(reclamationRepository.countByDateCreationBetween(any(), any())).thenReturn(8L);
        when(reclamationRepository.countByStatutAndDateCreationBetween(eq(Reclamation.Statut.SOUMISE), any(), any())).thenReturn(2L);
        when(reclamationRepository.countByStatutAndDateCreationBetween(eq(Reclamation.Statut.EN_COURS), any(), any())).thenReturn(3L);
        when(reclamationRepository.countByStatutAndDateCreationBetween(eq(Reclamation.Statut.RESOLUE), any(), any())).thenReturn(2L);
        when(reclamationRepository.countByStatutAndDateCreationBetween(eq(Reclamation.Statut.FERMEE), any(), any())).thenReturn(1L);
        when(reclamationRepository.countByPriorite(Reclamation.Priorite.URGENTE)).thenReturn(1L);
        
        List<Object[]> categoryStats = Arrays.asList(
            new Object[]{"Retards", 4L},
            new Object[]{"Bagages", 4L}
        );
        when(reclamationRepository.countByCategorie()).thenReturn(categoryStats);
        
        List<Object[]> priorityStats = Arrays.asList(
            new Object[]{Reclamation.Priorite.NORMALE, 6L},
            new Object[]{Reclamation.Priorite.URGENTE, 1L}
        );
        when(reclamationRepository.countByPrioriteGroup()).thenReturn(priorityStats);
        
        // Use resolved reclamation for satisfaction calculations
        List<Reclamation> reclamationsWithSatisfaction = Arrays.asList(resolvedReclamation);
        when(reclamationRepository.findReclamationsWithSatisfaction()).thenReturn(reclamationsWithSatisfaction);
        
        List<Object[]> resolutionStats = Arrays.asList(
            new Object[]{"Retards", 15.0},
            new Object[]{"Bagages", 20.0}
        );
        when(reclamationRepository.findAverageResolutionTimeByCategorie()).thenReturn(resolutionStats);
        
        List<Object[]> dailyTrends = Arrays.asList(
            new Object[]{"2025-01-01", 2L},
            new Object[]{"2025-01-02", 3L}
        );
        when(reclamationRepository.findDailyTrends(any())).thenReturn(dailyTrends);

        // Act
        AdminDashboardResponse result = dashboardService.getRealTimeStatistics();

        // Assert
        assertNotNull(result);
        assertEquals(8L, result.getStatistiquesGlobales().getTotalReclamations());
        assertEquals(3L, result.getStatistiquesGlobales().getReclamationsEnCours());
        assertEquals(2L, result.getStatistiquesGlobales().getReclamationsResolues());
        assertEquals(1L, result.getStatistiquesGlobales().getReclamationsUrgentes());
        assertEquals(25.0, result.getStatistiquesGlobales().getTauxResolution());
        assertEquals(0.0, result.getStatistiquesGlobales().getTempsResolutionMoyen()); // Not calculated for real-time
        
        // Verify category distribution
        assertEquals(2, result.getRepartitionParCategorie().size());
        assertEquals(4L, result.getRepartitionParCategorie().get("Retards"));
        assertEquals(4L, result.getRepartitionParCategorie().get("Bagages"));
        
        // Verify priority distribution
        assertEquals(2, result.getRepartitionParPriorite().size());
        assertEquals(6L, result.getRepartitionParPriorite().get("NORMALE"));
        assertEquals(1L, result.getRepartitionParPriorite().get("URGENTE"));
        
        // Verify satisfaction statistics
        assertEquals(4.0, result.getSatisfactionClient().getNoteMoyenne());
        assertEquals(1L, result.getSatisfactionClient().getTotalEvaluations());
        assertEquals(100.0, result.getSatisfactionClient().getTauxSatisfaction());
        
        // Verify resolution time by category
        assertEquals(2, result.getTempsResolutionMoyen().size());
        assertEquals(15.0, result.getTempsResolutionMoyen().get("Retards"));
        assertEquals(20.0, result.getTempsResolutionMoyen().get("Bagages"));
        
        // Verify trends
        assertEquals(2, result.getTendances30Jours().size());
    }

    @Test
    void getAdminDashboard_WithRepositoryErrors_ShouldHandleGracefully() {
        // Arrange - Use lenient stubbing for error handling tests
        lenient().when(reclamationRepository.count()).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countByDateCreationBetween(any(), any())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countByStatut(any())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countByPriorite(any())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findAverageResolutionTime()).thenThrow(new RuntimeException("Database error"));
        lenient().when(userRepository.findTopAgentsByResolutionCount(anyInt())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countByCategorie()).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countByPrioriteGroup()).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findReclamationsWithSatisfaction()).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findAverageResolutionTimeByCategorie()).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findDailyTrends(any())).thenThrow(new RuntimeException("Database error"));

        // Act
        AdminDashboardResponse result = dashboardService.getAdminDashboard();

        // Assert
        assertNotNull(result);
        assertEquals(0L, result.getStatistiquesGlobales().getTotalReclamations());
        assertEquals(0L, result.getStatistiquesGlobales().getReclamationsEnCours());
        assertEquals(0L, result.getStatistiquesGlobales().getReclamationsResolues());
        assertEquals(0L, result.getStatistiquesGlobales().getReclamationsUrgentes());
        assertEquals(0.0, result.getStatistiquesGlobales().getTauxResolution());
        assertEquals(0.0, result.getStatistiquesGlobales().getTempsResolutionMoyen());
        assertEquals(0, result.getRepartitionParCategorie().size());
        assertEquals(0, result.getRepartitionParPriorite().size());
        assertEquals(0, result.getPerformanceAgents().size());
        assertEquals(0, result.getTendances30Jours().size());
        assertEquals(0.0, result.getSatisfactionClient().getNoteMoyenne());
        assertEquals(0L, result.getSatisfactionClient().getTotalEvaluations());
        assertEquals(0.0, result.getSatisfactionClient().getTauxSatisfaction());
        assertEquals(0, result.getTempsResolutionMoyen().size());
    }

    @Test
    void getAgentDashboard_WithRepositoryErrors_ShouldHandleGracefully() {
        // Arrange
        String agentId = testAgent.getId().toString();
        
        // Use lenient stubbing for error handling tests
        lenient().when(reclamationRepository.findByAgentAssigne_Id(testAgent.getId())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countUrgentReclamationsByAgent(testAgent.getId())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countOverdueReclamationsByAgent(eq(testAgent.getId()), any())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findAverageResolutionTimeByAgent(testAgent.getId())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countByPrioriteGroupForAgent(testAgent.getId())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.countByCategorieForAgent(testAgent.getId())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findUrgentReclamationsByAgent(testAgent.getId())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findOverdueReclamationsByAgent(eq(testAgent.getId()), any())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findReclamationsWithSatisfactionByAgent(testAgent.getId())).thenThrow(new RuntimeException("Database error"));
        lenient().when(reclamationRepository.findDailyTrendsByAgent(eq(testAgent.getId()), any())).thenThrow(new RuntimeException("Database error"));
        lenient().when(userRepository.findAverageSatisfactionByAgent(testAgent.getId())).thenThrow(new RuntimeException("Database error"));

        // Act
        AgentDashboardResponse result = dashboardService.getAgentDashboard(agentId);

        // Assert
        assertNotNull(result);
        assertEquals(0L, result.getStatistiquesPersonnelles().getTotalAssignees());
        assertEquals(0L, result.getStatistiquesPersonnelles().getReclamationsEnCours());
        assertEquals(0L, result.getStatistiquesPersonnelles().getReclamationsResolues());
        assertEquals(0L, result.getStatistiquesPersonnelles().getReclamationsUrgentes());
        assertEquals(0L, result.getStatistiquesPersonnelles().getReclamationsEnRetard());
        assertEquals(0.0, result.getStatistiquesPersonnelles().getTauxResolution());
        assertEquals(0.0, result.getStatistiquesPersonnelles().getTempsResolutionMoyen());
        assertEquals("FAIBLE", result.getStatistiquesPersonnelles().getChargeTravail());
        assertEquals(0, result.getRepartitionParPriorite().size());
        assertEquals(0, result.getRepartitionParCategorie().size());
        assertEquals(0, result.getReclamationsUrgentes().size());
        assertEquals(0, result.getReclamationsEnRetard().size());
        assertEquals(0, result.getTendances7Jours().size());
        assertEquals(0.0, result.getSatisfactionClients().getNoteMoyenne());
        assertEquals(0L, result.getSatisfactionClients().getTotalEvaluations());
        assertEquals(0.0, result.getSatisfactionClients().getTauxSatisfaction());
        assertEquals(0L, result.getSatisfactionClients().getCommentairesPositifs());
        assertEquals(0L, result.getSatisfactionClients().getCommentairesNegatifs());
        assertEquals(0L, result.getPerformanceMois().getReclamationsTraitees());
        assertEquals(0.0, result.getPerformanceMois().getTempsResolutionMoyen());
        assertEquals(0.0, result.getPerformanceMois().getSatisfactionMoyenne());
        assertTrue(result.getPerformanceMois().getObjectifAtteint());
        assertEquals(100.0, result.getPerformanceMois().getPourcentageObjectif());
    }
} 