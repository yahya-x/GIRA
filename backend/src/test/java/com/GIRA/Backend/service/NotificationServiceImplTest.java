package com.GIRA.Backend.service;

import com.GIRA.Backend.Entities.Notification;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.Respository.NotificationRepository;
import com.GIRA.Backend.service.impl.NotificationServiceImpl;
import com.GIRA.Backend.service.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationServiceImpl(notificationRepository, simpMessagingTemplate, emailService);
    }

    @Test
    void sendNotification_EmailType_SendsEmailAndPersists() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        Notification notification = new Notification();
        notification.setType(Notification.Type.EMAIL);
        notification.setDestinataire(user);
        notification.setSujet("Test Subject");
        notification.setContenu("Test Body");

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.sendNotification(notification);

        verify(emailService, times(1)).sendNotificationEmail(eq("test@example.com"), eq("Test Subject"), eq("Test Body"));
        verify(notificationRepository, times(1)).save(notification);
        assertEquals(Notification.Statut.ENVOYE, result.getStatut());
        assertNotNull(result.getDateEnvoi());
    }

    @Test
    void sendNotification_PushType_BroadcastsWebSocketAndPersists() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        Notification notification = new Notification();
        notification.setType(Notification.Type.PUSH);
        notification.setDestinataire(user);
        notification.setContenu("Push Content");

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.sendNotification(notification);

        String expectedTopic = "/topic/notifications/" + userId;
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq(expectedTopic), eq(notification));
        verify(notificationRepository, times(1)).save(notification);
        assertEquals(Notification.Statut.ENVOYE, result.getStatut());
        assertNotNull(result.getDateEnvoi());
    }
} 