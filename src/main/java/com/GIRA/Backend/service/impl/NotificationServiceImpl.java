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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.GIRA.Backend.service.interfaces.EmailService;

/**
 * Implementation of NotificationService.
 * Provides sending, retrieval, advanced queries, statistics, and batch operations for notifications.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final EmailService emailService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, SimpMessagingTemplate simpMessagingTemplate, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.emailService = emailService;
    }

    /**
     * Sends a notification via the appropriate channel (email, push, SMS, WebSocket).
     * Updates the notification status, persists it, and broadcasts via WebSocket if needed.
     *
     * @param notification the notification entity to send
     * @return the sent notification entity
     */
    @Override
    public Notification sendNotification(Notification notification) {
        try {
            switch (notification.getType()) {
                case EMAIL:
                    // Send email using EmailService
                    if (notification.getDestinataire() != null && notification.getDestinataire().getEmail() != null) {
                        String subject = notification.getSujet() != null ? notification.getSujet() : "Notification GIRA";
                        String body = notification.getContenu();
                        emailService.sendNotificationEmail(notification.getDestinataire().getEmail(), subject, body);
                    }
                    break;
                case PUSH:
                    // Broadcast via WebSocket for in-app notification
                    if (notification.getDestinataire() != null && notification.getDestinataire().getId() != null) {
                        String topic = "/topic/notifications/" + notification.getDestinataire().getId();
                        simpMessagingTemplate.convertAndSend(topic, notification);
                    }
                    break;
                case SMS:
                    // TODO: Integrate with SMS service provider
                    break;
            }
            notification.setStatut(Notification.Statut.ENVOYE);
            notification.setDateEnvoi(LocalDateTime.now());
        } catch (Exception e) {
            notification.setStatut(Notification.Statut.ECHEC);
        }
        return notificationRepository.save(notification);
    }

    /**
     * Retrieves a notification by its unique ID.
     *
     * @param id the notification UUID
     * @return the notification entity, or null if not found
     */
    @Override
    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all notifications in the system.
     *
     * @return list of all notification entities
     */
    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    /**
     * Marks a notification as read by its ID.
     * Updates the status and timestamps.
     *
     * @param id the notification UUID
     */
    @Override
    public void markAsRead(UUID id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setStatut(Notification.Statut.LU);
            notification.setDateLecture(LocalDateTime.now());
            notification.setDateModification(LocalDateTime.now());
            notificationRepository.save(notification);
        });
    }

    /**
     * Deletes a notification by its ID.
     *
     * @param id the notification UUID
     */
    @Override
    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }

    /**
     * Finds notifications for a specific recipient.
     *
     * @param destinataire the recipient user
     * @return list of notifications for the user
     */
    @Override
    public List<Notification> findByDestinataire(User destinataire) {
        return notificationRepository.findByDestinataire(destinataire);
    }

    /**
     * Finds notifications for a specific recipient with pagination.
     *
     * @param destinataire the recipient user
     * @param pageable pagination information
     * @return page of notifications for the user
     */
    @Override
    public Page<Notification> findByDestinataire(User destinataire, Pageable pageable) {
        return notificationRepository.findByDestinataire(destinataire, pageable);
    }

    /**
     * Finds notifications by status.
     *
     * @param statut the notification status
     * @return list of notifications with the given status
     */
    @Override
    public List<Notification> findByStatut(Notification.Statut statut) {
        return notificationRepository.findByStatut(statut);
    }

    /**
     * Finds notifications by type.
     *
     * @param type the notification type
     * @return list of notifications with the given type
     */
    @Override
    public List<Notification> findByType(Notification.Type type) {
        return notificationRepository.findByType(type);
    }

    /**
     * Finds notifications for a recipient with a specific status.
     *
     * @param destinataire the recipient user
     * @param statut the notification status
     * @return list of notifications for the user with the given status
     */
    @Override
    public List<Notification> findByDestinataireAndStatut(User destinataire, Notification.Statut statut) {
        return notificationRepository.findByDestinataireAndStatut(destinataire, statut);
    }

    /**
     * Finds unread notifications for a recipient by their ID.
     *
     * @param destinataireId the recipient UUID
     * @return list of unread notifications
     */
    @Override
    public List<Notification> findUnreadByDestinataireId(UUID destinataireId) {
        return notificationRepository.findUnreadByDestinataireId(destinataireId);
    }

    /**
     * Counts unread notifications for a recipient by their ID.
     *
     * @param destinataireId the recipient UUID
     * @return number of unread notifications
     */
    @Override
    public long countUnreadByDestinataireId(UUID destinataireId) {
        return notificationRepository.countUnreadByDestinataireId(destinataireId);
    }

    /**
     * Finds notifications sent between two dates.
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of notifications sent in the date range
     */
    @Override
    public List<Notification> findByDateEnvoiBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findByDateEnvoiBetween(startDate, endDate);
    }

    /**
     * Finds notifications for a recipient and type.
     *
     * @param destinataireId the recipient UUID
     * @param type the notification type
     * @return list of notifications
     */
    @Override
    public List<Notification> findByDestinataireIdAndType(UUID destinataireId, Notification.Type type) {
        return notificationRepository.findByDestinataireIdAndType(destinataireId, type);
    }

    /**
     * Finds notifications related to a specific complaint.
     *
     * @param reclamationId the complaint UUID
     * @return list of notifications for the complaint
     */
    @Override
    public List<Notification> findByReclamationId(UUID reclamationId) {
        return notificationRepository.findByReclamationId(reclamationId);
    }

    /**
     * Finds notifications that failed to send.
     *
     * @return list of failed notifications
     */
    @Override
    public List<Notification> findFailedNotifications() {
        return notificationRepository.findFailedNotifications();
    }

    /**
     * Finds notifications that are pending to be sent.
     *
     * @return list of pending notifications
     */
    @Override
    public List<Notification> findPendingNotifications() {
        return notificationRepository.findPendingNotifications();
    }

    /**
     * Counts notifications by status for a user.
     *
     * @param destinataireId the recipient UUID
     * @return list of status counts
     */
    @Override
    public List<Object[]> countByStatutForUser(UUID destinataireId) {
        return notificationRepository.countByStatutForUser(destinataireId);
    }

    /**
     * Counts notifications by type for a user.
     *
     * @param destinataireId the recipient UUID
     * @return list of type counts
     */
    @Override
    public List<Object[]> countByTypeForUser(UUID destinataireId) {
        return notificationRepository.countByTypeForUser(destinataireId);
    }

    /**
     * Gets global notification statistics.
     *
     * @return list of statistics
     */
    @Override
    public List<Object[]> getNotificationStatistics() {
        return notificationRepository.getNotificationStatistics();
    }

    /**
     * Counts notifications sent today.
     *
     * @param startOfDay start of the day
     * @param endOfDay end of the day
     * @return number of notifications sent today
     */
    @Override
    public long countNotificationsSentToday(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return notificationRepository.countNotificationsSentToday(startOfDay, endOfDay);
    }

    /**
     * Counts notifications sent this week.
     *
     * @param startOfWeek start of the week
     * @return number of notifications sent this week
     */
    @Override
    public long countNotificationsSentThisWeek(LocalDateTime startOfWeek) {
        return notificationRepository.countNotificationsSentThisWeek(startOfWeek);
    }

    /**
     * Counts notifications sent this month.
     *
     * @param startOfMonth start of the month
     * @param endOfMonth end of the month
     * @return number of notifications sent this month
     */
    @Override
    public long countNotificationsSentThisMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth) {
        return notificationRepository.countNotificationsSentThisMonth(startOfMonth, endOfMonth);
    }

    /**
     * Searches notifications by content keyword.
     *
     * @param keyword the search keyword
     * @return list of matching notifications
     */
    @Override
    public List<Notification> searchByContent(String keyword) {
        return notificationRepository.searchByContent(keyword);
    }

    /**
     * Finds notifications with advanced filtering options.
     *
     * @param destinataireId the recipient UUID
     * @param statut the notification status
     * @param type the notification type
     * @param startDate start date
     * @param endDate end date
     * @return list of notifications matching the filters
     */
    @Override
    public List<Notification> findWithAdvancedFiltering(UUID destinataireId, Notification.Statut statut, Notification.Type type, LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findWithAdvancedFiltering(destinataireId, statut, type, startDate, endDate);
    }

    /**
     * Finds notifications scheduled to be sent at or before the given time.
     *
     * @param currentTime the current time
     * @return list of notifications to send
     */
    @Override
    public List<Notification> findNotificationsToSend(LocalDateTime currentTime) {
        return notificationRepository.findNotificationsToSend(currentTime);
    }

    /**
     * Marks all notifications as read for a user.
     *
     * @param destinataireId the recipient UUID
     * @param readTime the time to set as read
     * @return number of notifications marked as read
     */
    @Override
    public int markAllAsReadForUser(UUID destinataireId, LocalDateTime readTime) {
        return notificationRepository.markAllAsReadForUser(destinataireId, readTime);
    }

    /**
     * Deletes old notifications before a cutoff date.
     *
     * @param cutoffDate the cutoff date
     * @return number of notifications deleted
     */
    @Override
    public int deleteOldNotifications(LocalDateTime cutoffDate) {
        return notificationRepository.deleteOldNotifications(cutoffDate);
    }

    /**
     * Retries failed notifications.
     *
     * @return number of notifications retried
     */
    @Override
    public int retryFailedNotifications() {
        return notificationRepository.retryFailedNotifications();
    }

    /**
     * Finds notifications with delivery issues after a threshold.
     *
     * @param threshold the threshold time
     * @return list of notifications with delivery issues
     */
    @Override
    public List<Notification> findNotificationsWithDeliveryIssues(LocalDateTime threshold) {
        return notificationRepository.findNotificationsWithDeliveryIssues(threshold);
    }

    /**
     * Gets the delivery success rate for notifications.
     *
     * @return array containing delivery statistics
     */
    @Override
    public Object[] getDeliverySuccessRate() {
        return notificationRepository.getDeliverySuccessRate();
    }

    /**
     * Finds high priority notifications.
     *
     * @return list of high priority notifications
     */
    @Override
    public List<Notification> findHighPriorityNotifications() {
        return notificationRepository.findHighPriorityNotifications();
    }
} 