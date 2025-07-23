package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.Notification;
import com.GIRA.Backend.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing notifications.
 * Provides sending, retrieval, advanced queries, statistics, and batch operations.
 * @author Mohamed Yahya Jabrane
 */
public interface NotificationService {
    /**
     * Sends a notification.
     * @param notification The notification entity to send
     * @return The sent notification entity
     */
    Notification sendNotification(Notification notification);

    /**
     * Retrieves a notification by its ID.
     * @param id The notification UUID
     * @return The notification entity
     */
    Notification getNotificationById(UUID id);

    /**
     * Retrieves all notifications.
     * @return List of notification entities
     */
    List<Notification> getAllNotifications();

    /**
     * Marks a notification as read.
     * @param id The notification UUID
     */
    void markAsRead(UUID id);

    /**
     * Deletes a notification by its ID.
     * @param id The notification UUID
     */
    void deleteNotification(UUID id);

    /**
     * Finds notifications by recipient user.
     * @param destinataire The recipient user entity
     * @return List of notification entities
     */
    List<Notification> findByDestinataire(User destinataire);

    /**
     * Finds notifications by recipient user with pagination.
     * @param destinataire The recipient user entity
     * @param pageable Pagination parameters
     * @return Page of notification entities
     */
    Page<Notification> findByDestinataire(User destinataire, Pageable pageable);

    /**
     * Finds notifications by status.
     * @param statut The notification status
     * @return List of notification entities
     */
    List<Notification> findByStatut(Notification.Statut statut);

    /**
     * Finds notifications by type.
     * @param type The notification type
     * @return List of notification entities
     */
    List<Notification> findByType(Notification.Type type);

    /**
     * Finds notifications by recipient user and status.
     * @param destinataire The recipient user entity
     * @param statut The notification status
     * @return List of notification entities
     */
    List<Notification> findByDestinataireAndStatut(User destinataire, Notification.Statut statut);

    /**
     * Finds unread notifications for a user.
     * @param destinataireId The recipient user UUID
     * @return List of unread notification entities
     */
    List<Notification> findUnreadByDestinataireId(UUID destinataireId);

    /**
     * Counts unread notifications for a user.
     * @param destinataireId The recipient user UUID
     * @return Number of unread notifications
     */
    long countUnreadByDestinataireId(UUID destinataireId);

    /**
     * Finds notifications sent within a date range.
     * @param startDate Start date
     * @param endDate End date
     * @return List of notification entities
     */
    List<Notification> findByDateEnvoiBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds notifications by recipient user and type.
     * @param destinataireId The recipient user UUID
     * @param type The notification type
     * @return List of notification entities
     */
    List<Notification> findByDestinataireIdAndType(UUID destinataireId, Notification.Type type);

    /**
     * Finds notifications related to a specific complaint.
     * @param reclamationId The complaint UUID
     * @return List of notification entities
     */
    List<Notification> findByReclamationId(UUID reclamationId);

    /**
     * Finds failed notifications (status = ECHEC).
     * @return List of failed notification entities
     */
    List<Notification> findFailedNotifications();

    /**
     * Finds pending notifications (status = EN_ATTENTE).
     * @return List of pending notification entities
     */
    List<Notification> findPendingNotifications();

    /**
     * Counts notifications by status for a user.
     * @param destinataireId The recipient user UUID
     * @return List of status and count pairs
     */
    List<Object[]> countByStatutForUser(UUID destinataireId);

    /**
     * Counts notifications by type for a user.
     * @param destinataireId The recipient user UUID
     * @return List of type and count pairs
     */
    List<Object[]> countByTypeForUser(UUID destinataireId);

    /**
     * Gets notification statistics for the entire system.
     * @return List of status, type, and count triples
     */
    List<Object[]> getNotificationStatistics();

    /**
     * Counts notifications sent today.
     * @param startOfDay Start of day
     * @param endOfDay End of day
     * @return Number of notifications sent today
     */
    long countNotificationsSentToday(LocalDateTime startOfDay, LocalDateTime endOfDay);

    /**
     * Counts notifications sent this week.
     * @param startOfWeek Start of week
     * @return Number of notifications sent this week
     */
    long countNotificationsSentThisWeek(LocalDateTime startOfWeek);

    /**
     * Counts notifications sent this month.
     * @param startOfMonth Start of month
     * @param endOfMonth End of month
     * @return Number of notifications sent this month
     */
    long countNotificationsSentThisMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth);

    /**
     * Searches notifications by content (case-insensitive).
     * @param keyword The search keyword
     * @return List of notification entities
     */
    List<Notification> searchByContent(String keyword);

    /**
     * Finds notifications by recipient user with advanced filtering.
     * @param destinataireId The recipient user UUID
     * @param statut The notification status (optional)
     * @param type The notification type (optional)
     * @param startDate Start date (optional)
     * @param endDate End date (optional)
     * @return List of filtered notification entities
     */
    List<Notification> findWithAdvancedFiltering(UUID destinataireId, Notification.Statut statut, Notification.Type type, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds notifications that need to be sent (pending and scheduled).
     * @param currentTime Current time
     * @return List of notifications ready to be sent
     */
    List<Notification> findNotificationsToSend(LocalDateTime currentTime);

    /**
     * Marks all notifications as read for a user.
     * @param destinataireId The recipient user UUID
     * @param readTime The time when notifications were marked as read
     * @return Number of notifications updated
     */
    int markAllAsReadForUser(UUID destinataireId, LocalDateTime readTime);

    /**
     * Deletes old notifications (older than specified date).
     * @param cutoffDate Cutoff date for deletion
     * @return Number of notifications deleted
     */
    int deleteOldNotifications(LocalDateTime cutoffDate);

    /**
     * Retries failed notifications.
     * @return Number of notifications reset for retry
     */
    int retryFailedNotifications();

    /**
     * Finds notifications with delivery issues.
     * @param threshold Time threshold
     * @return List of notifications with delivery issues
     */
    List<Notification> findNotificationsWithDeliveryIssues(LocalDateTime threshold);

    /**
     * Gets notification delivery success rate.
     * @return Array with sent, failed, and read counts
     */
    Object[] getDeliverySuccessRate();

    /**
     * Finds high-priority notifications.
     * @return List of high-priority notification entities
     */
    List<Notification> findHighPriorityNotifications();
} 