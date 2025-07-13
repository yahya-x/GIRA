package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.NotificationService;
import com.GIRA.Backend.Respository.NotificationRepository;
import com.GIRA.Backend.Entities.Notification;
import com.GIRA.Backend.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of NotificationService.
 * Provides sending, retrieval, advanced queries, statistics, and batch operations for notifications.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification sendNotification(Notification notification) {
        // Send notification via appropriate channel (email, push, SMS)
        try {
            switch (notification.getType()) {
                case EMAIL:
                    // Simulate email sending
                    break;
                case PUSH:
                    // Simulate push notification
                    break;
                case SMS:
                    // Simulate SMS sending
                    break;
            }
            notification.setStatut(Notification.Statut.ENVOYE);
            notification.setDateEnvoi(LocalDateTime.now());
        } catch (Exception e) {
            notification.setStatut(Notification.Statut.ECHEC);
        }
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public void markAsRead(UUID id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setStatut(Notification.Statut.LU);
            notification.setDateLecture(LocalDateTime.now());
            notification.setDateModification(LocalDateTime.now());
            notificationRepository.save(notification);
        });
    }

    @Override
    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> findByDestinataire(User destinataire) {
        return notificationRepository.findByDestinataire(destinataire);
    }

    @Override
    public Page<Notification> findByDestinataire(User destinataire, Pageable pageable) {
        return notificationRepository.findByDestinataire(destinataire, pageable);
    }

    @Override
    public List<Notification> findByStatut(Notification.Statut statut) {
        return notificationRepository.findByStatut(statut);
    }

    @Override
    public List<Notification> findByType(Notification.Type type) {
        return notificationRepository.findByType(type);
    }

    @Override
    public List<Notification> findByDestinataireAndStatut(User destinataire, Notification.Statut statut) {
        return notificationRepository.findByDestinataireAndStatut(destinataire, statut);
    }

    @Override
    public List<Notification> findUnreadByDestinataireId(UUID destinataireId) {
        return notificationRepository.findUnreadByDestinataireId(destinataireId);
    }

    @Override
    public long countUnreadByDestinataireId(UUID destinataireId) {
        return notificationRepository.countUnreadByDestinataireId(destinataireId);
    }

    @Override
    public List<Notification> findByDateEnvoiBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findByDateEnvoiBetween(startDate, endDate);
    }

    @Override
    public List<Notification> findByDestinataireIdAndType(UUID destinataireId, Notification.Type type) {
        return notificationRepository.findByDestinataireIdAndType(destinataireId, type);
    }

    @Override
    public List<Notification> findByReclamationId(UUID reclamationId) {
        return notificationRepository.findByReclamationId(reclamationId);
    }

    @Override
    public List<Notification> findFailedNotifications() {
        return notificationRepository.findFailedNotifications();
    }

    @Override
    public List<Notification> findPendingNotifications() {
        return notificationRepository.findPendingNotifications();
    }

    @Override
    public List<Object[]> countByStatutForUser(UUID destinataireId) {
        return notificationRepository.countByStatutForUser(destinataireId);
    }

    @Override
    public List<Object[]> countByTypeForUser(UUID destinataireId) {
        return notificationRepository.countByTypeForUser(destinataireId);
    }

    @Override
    public List<Object[]> getNotificationStatistics() {
        return notificationRepository.getNotificationStatistics();
    }

    @Override
    public long countNotificationsSentToday(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return notificationRepository.countNotificationsSentToday(startOfDay, endOfDay);
    }

    @Override
    public long countNotificationsSentThisWeek(LocalDateTime startOfWeek) {
        return notificationRepository.countNotificationsSentThisWeek(startOfWeek);
    }

    @Override
    public long countNotificationsSentThisMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth) {
        return notificationRepository.countNotificationsSentThisMonth(startOfMonth, endOfMonth);
    }

    @Override
    public List<Notification> searchByContent(String keyword) {
        return notificationRepository.searchByContent(keyword);
    }

    @Override
    public List<Notification> findWithAdvancedFiltering(UUID destinataireId, Notification.Statut statut, Notification.Type type, LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findWithAdvancedFiltering(destinataireId, statut, type, startDate, endDate);
    }

    @Override
    public List<Notification> findNotificationsToSend(LocalDateTime currentTime) {
        return notificationRepository.findNotificationsToSend(currentTime);
    }

    @Override
    public int markAllAsReadForUser(UUID destinataireId, LocalDateTime readTime) {
        return notificationRepository.markAllAsReadForUser(destinataireId, readTime);
    }

    @Override
    public int deleteOldNotifications(LocalDateTime cutoffDate) {
        return notificationRepository.deleteOldNotifications(cutoffDate);
    }

    @Override
    public int retryFailedNotifications() {
        return notificationRepository.retryFailedNotifications();
    }

    @Override
    public List<Notification> findNotificationsWithDeliveryIssues(LocalDateTime threshold) {
        return notificationRepository.findNotificationsWithDeliveryIssues(threshold);
    }

    @Override
    public Object[] getDeliverySuccessRate() {
        return notificationRepository.getDeliverySuccessRate();
    }

    @Override
    public List<Notification> findHighPriorityNotifications() {
        return notificationRepository.findHighPriorityNotifications();
    }
} 