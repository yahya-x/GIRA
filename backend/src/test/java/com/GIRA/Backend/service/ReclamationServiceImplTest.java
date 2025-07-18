package com.GIRA.Backend.service;

import com.GIRA.Backend.DTO.request.ReclamationFilterRequest;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;
import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.Respository.ReclamationRepository;
import com.GIRA.Backend.service.impl.ReclamationServiceImpl;
import com.GIRA.Backend.service.interfaces.UserService;
import com.GIRA.Backend.service.interfaces.CategorieService;
import com.GIRA.Backend.service.interfaces.SousCategorieService;
import com.GIRA.Backend.Respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.GIRA.Backend.service.interfaces.NotificationService;
import com.GIRA.Backend.Entities.Notification;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.DTO.request.ReclamationUpdateRequest;
import com.GIRA.Backend.Entities.Historique;
import com.GIRA.Backend.service.interfaces.HistoriqueService;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Unit tests for ReclamationServiceImpl advanced filtering.
 */
class ReclamationServiceImplTest {
    @Mock
    private ReclamationRepository reclamationRepository;
    @Mock
    private UserService userService;
    @Mock
    private CategorieService categorieService;
    @Mock
    private SousCategorieService sousCategorieService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private HistoriqueService historiqueService;

    @InjectMocks
    private ReclamationServiceImpl reclamationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindWithFiltersDto_StatusAndCategory() {
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setStatut("EN_COURS");
        filter.setCategorieId(UUID.randomUUID());
        filter.setPage(0);
        filter.setSize(10);
        Reclamation rec = new Reclamation();
        rec.setTitre("Test");
        Page<Reclamation> page = new PageImpl<>(List.of(rec));
        when(reclamationRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class))).thenReturn(page);
        Page<ReclamationListResponse> result = reclamationService.findWithFiltersDto(filter);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test", result.getContent().get(0).getTitre());
    }

    @Test
    void testFindWithFiltersDto_Keyword() {
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setMotCle("incident");
        filter.setPage(0);
        filter.setSize(5);
        Reclamation rec = new Reclamation();
        rec.setTitre("Incident bagages");
        Page<Reclamation> page = new PageImpl<>(List.of(rec));
        when(reclamationRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class))).thenReturn(page);
        Page<ReclamationListResponse> result = reclamationService.findWithFiltersDto(filter);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getTitre().contains("Incident"));
    }

    @Test
    void testFindWithFiltersDto_EmptyResult() {
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setPage(0);
        filter.setSize(5);
        when(reclamationRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        Page<ReclamationListResponse> result = reclamationService.findWithFiltersDto(filter);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testUpdateReclamation_AssignmentSendsNotifications() {
        // Setup
        User agent = new User();
        agent.setId(UUID.randomUUID());
        agent.setPrenom("AgentPrenom");
        User admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setPrenom("AdminPrenom");
        Reclamation rec = new Reclamation();
        rec.setId(UUID.randomUUID());
        rec.setTitre("Test Reclamation");
        rec.setStatut(Reclamation.Statut.EN_COURS);
        rec.setPriorite(Reclamation.Priorite.NORMALE);
        rec.setUtilisateur(admin);
        when(reclamationRepository.findById(rec.getId())).thenReturn(java.util.Optional.of(rec));
        when(userService.getUserById(any(UUID.class))).thenReturn(agent);
        // Simulate security context
        com.GIRA.Backend.security.UserPrincipal principal = new com.GIRA.Backend.security.UserPrincipal(
            agent.getId(),
            "agent@example.com",
            "AgentNom",
            "AgentPrenom",
            "AGENT",
            "password",
            true,
            true
        );
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(principal, null));
        // Prepare update request
        ReclamationUpdateRequest req = new ReclamationUpdateRequest();
        req.setAgentAssigneId(agent.getId().toString());
        // Call
        reclamationService.updateReclamation(rec.getId(), req);
        // Verify notifications
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationService, times(2)).sendNotification(captor.capture());
        boolean foundPush = false, foundEmail = false;
        for (Notification n : captor.getAllValues()) {
            if (n.getType() == Notification.Type.PUSH && n.getDestinataire().equals(agent)) foundPush = true;
            if (n.getType() == Notification.Type.EMAIL && n.getDestinataire().equals(agent)) foundEmail = true;
        }
        assertTrue(foundPush, "Agent should receive PUSH notification");
        assertTrue(foundEmail, "Agent should receive EMAIL notification");
    }

    @Test
    void testUpdateReclamation_StatusChangeSendsNotifications() {
        // Setup
        User owner = new User();
        owner.setId(UUID.randomUUID());
        owner.setPrenom("OwnerPrenom");
        User agent = new User();
        agent.setId(UUID.randomUUID());
        Reclamation rec = new Reclamation();
        rec.setId(UUID.randomUUID());
        rec.setTitre("Test Reclamation");
        rec.setStatut(Reclamation.Statut.EN_COURS);
        rec.setPriorite(Reclamation.Priorite.NORMALE);
        rec.setUtilisateur(owner);
        rec.setAgentAssigne(agent);
        when(reclamationRepository.findById(rec.getId())).thenReturn(java.util.Optional.of(rec));
        when(userService.getUserById(any(UUID.class))).thenReturn(agent);
        // Simulate security context
        com.GIRA.Backend.security.UserPrincipal principal = new com.GIRA.Backend.security.UserPrincipal(
            agent.getId(),
            "agent@example.com",
            "AgentNom",
            "AgentPrenom",
            "AGENT",
            "password",
            true,
            true
        );
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(principal, null));
        // Prepare update request
        ReclamationUpdateRequest req = new ReclamationUpdateRequest();
        req.setStatut("RESOLUE");
        // Call
        reclamationService.updateReclamation(rec.getId(), req);
        // Verify notifications
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationService, times(2)).sendNotification(captor.capture());
        boolean foundPush = false, foundEmail = false;
        for (Notification n : captor.getAllValues()) {
            if (n.getType() == Notification.Type.PUSH && n.getDestinataire().equals(owner)) foundPush = true;
            if (n.getType() == Notification.Type.EMAIL && n.getDestinataire().equals(owner)) foundEmail = true;
        }
        assertTrue(foundPush, "Owner should receive PUSH notification");
        assertTrue(foundEmail, "Owner should receive EMAIL notification");
    }

    @Test
    void testUpdateReclamation_PriorityChangeSendsPushNotification() {
        // Setup
        User owner = new User();
        owner.setId(UUID.randomUUID());
        owner.setPrenom("OwnerPrenom");
        User agent = new User();
        agent.setId(UUID.randomUUID());
        Reclamation rec = new Reclamation();
        rec.setId(UUID.randomUUID());
        rec.setTitre("Test Reclamation");
        rec.setStatut(Reclamation.Statut.EN_COURS);
        rec.setPriorite(Reclamation.Priorite.NORMALE);
        rec.setUtilisateur(owner);
        rec.setAgentAssigne(agent);
        when(reclamationRepository.findById(rec.getId())).thenReturn(java.util.Optional.of(rec));
        when(userService.getUserById(any(UUID.class))).thenReturn(agent);
        // Simulate security context
        com.GIRA.Backend.security.UserPrincipal principal = new com.GIRA.Backend.security.UserPrincipal(
            agent.getId(),
            "agent@example.com",
            "AgentNom",
            "AgentPrenom",
            "AGENT",
            "password",
            true,
            true
        );
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(principal, null));
        // Prepare update request
        ReclamationUpdateRequest req = new ReclamationUpdateRequest();
        req.setPriorite("HAUTE");
        // Call
        reclamationService.updateReclamation(rec.getId(), req);
        // Verify notifications
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationService, times(1)).sendNotification(captor.capture());
        Notification notif = captor.getValue();
        assertEquals(Notification.Type.PUSH, notif.getType());
        assertEquals(owner, notif.getDestinataire());
    }

    @Test
    void testUpdateReclamation_EvaluationSendsPushToAgent() {
        // Setup
        User owner = new User();
        owner.setId(UUID.randomUUID());
        owner.setPrenom("OwnerPrenom");
        User agent = new User();
        agent.setId(UUID.randomUUID());
        agent.setPrenom("AgentPrenom");
        Reclamation rec = new Reclamation();
        rec.setId(UUID.randomUUID());
        rec.setTitre("Test Reclamation");
        rec.setStatut(Reclamation.Statut.RESOLUE);
        rec.setPriorite(Reclamation.Priorite.NORMALE);
        rec.setUtilisateur(owner);
        rec.setAgentAssigne(agent);
        when(reclamationRepository.findById(rec.getId())).thenReturn(java.util.Optional.of(rec));
        when(userService.getUserById(any(UUID.class))).thenReturn(owner);
        // Simulate security context for PASSAGER
        com.GIRA.Backend.security.UserPrincipal principal = new com.GIRA.Backend.security.UserPrincipal(
            owner.getId(),
            "owner@example.com",
            "OwnerNom",
            "OwnerPrenom",
            "PASSAGER",
            "password",
            true,
            true
        );
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(principal, null));
        // Prepare update request
        ReclamationUpdateRequest req = new ReclamationUpdateRequest();
        req.setSatisfaction(5);
        req.setCommentaireSatisfaction("Très satisfait");
        // Call
        reclamationService.updateReclamation(rec.getId(), req);
        // Verify notification to agent
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationService, times(1)).sendNotification(captor.capture());
        Notification notif = captor.getValue();
        assertEquals(Notification.Type.PUSH, notif.getType());
        assertEquals(agent, notif.getDestinataire());
    }
} 