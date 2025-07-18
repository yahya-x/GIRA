package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Notification;
import com.GIRA.Backend.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * Repository interface for managing Notification entities in the airport complaint management system.
 * Provides data access methods for notifications including advanced queries, statistics, and analytics.
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    // ====== Essential CRUD Operations ======

    /**
     * Find notifications by recipient user.
     *
     * @param destinataire the recipient user
     * @return list of notifications for the user
     */
    List<Notification> findByDestinataire(User destinataire);

    /**
     * Find notifications by recipient user with pagination.
     *
     * @param destinataire the recipient user
     * @param pageable     pagination parameters
     * @return paginated notifications for the user
     */
    Page<Notification> findByDestinataire(User destinataire, Pageable pageable);

    /**
     * Find notifications by status.
     *
     * @param statut the notification status
     * @return list of notifications with the specified status
     */
    List<Notification> findByStatut(Notification.Statut statut);

    /**
     * Find notifications by type.
     *
     * @param type the notification type
     * @return list of notifications with the specified type
     */
    List<Notification> findByType(Notification.Type type);

    /**
     * Find notifications by recipient user and status.
     *
     * @param destinataire the recipient user
     * @param statut       the notification status
     * @return list of notifications for the user with the specified status
     */
    List<Notification> findByDestinataireAndStatut(User destinataire, Notification.Statut statut);

    // ====== Advanced Query Methods ======

    /**
     * Find unread notifications for a user.
     *
     * @param destinataireId the recipient user ID
     * @return list of unread notifications
     */
    @Query("SELECT n FROM Notification n WHERE n.destinataire.id = :destinataireId AND n.statut = 'LU'")
    List<Notification> findUnreadByDestinataireId(@Param("destinataireId") UUID destinataireId);

    /**
     * Count unread notifications for a user.
     *
     * @param destinataireId the recipient user ID
     * @return count of unread notifications
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataire.id = :destinataireId AND n.statut = 'EN_ATTENTE'")
    long countUnreadByDestinataireId(@Param("destinataireId") UUID destinataireId);

    /**
     * Find notifications sent within a date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @return list of notifications sent in the date range
     */
    @Query("SELECT n FROM Notification n WHERE n.dateEnvoi BETWEEN :startDate AND :endDate")
    List<Notification> findByDateEnvoiBetween(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);

    /**
     * Find notifications by recipient user and type.
     *
     * @param destinataireId the recipient user ID
     * @param type           the notification type
     * @return list of notifications for the user with the specified type
     */
    @Query("SELECT n FROM Notification n WHERE n.destinataire.id = :destinataireId AND n.type = :type")
    List<Notification> findByDestinataireIdAndType(@Param("destinataireId") UUID destinataireId, 
                                                   @Param("type") Notification.Type type);

    /**
     * Find notifications related to a specific complaint.
     *
     * @param reclamationId the complaint ID
     * @return list of notifications related to the complaint
     */
    @Query("SELECT n FROM Notification n WHERE n.reclamation.id = :reclamationId")
    List<Notification> findByReclamationId(@Param("reclamationId") UUID reclamationId);

    /**
     * Find failed notifications (status = ECHEC).
     *
     * @return list of failed notifications
     */
    @Query("SELECT n FROM Notification n WHERE n.statut = 'ECHEC'")
    List<Notification> findFailedNotifications();

    /**
     * Find pending notifications (status = EN_ATTENTE).
     *
     * @return list of pending notifications
     */
    @Query("SELECT n FROM Notification n WHERE n.statut = 'EN_ATTENTE'")
    List<Notification> findPendingNotifications();

    // ====== Statistics and Analytics ======

    /**
     * Count notifications by status for a user.
     *
     * @param destinataireId the recipient user ID
     * @return count of notifications by status
     */
    @Query("SELECT n.statut, COUNT(n) FROM Notification n WHERE n.destinataire.id = :destinataireId GROUP BY n.statut")
    List<Object[]> countByStatutForUser(@Param("destinataireId") UUID destinataireId);

    /**
     * Count notifications by type for a user.
     *
     * @param destinataireId the recipient user ID
     * @return count of notifications by type
     */
    @Query("SELECT n.type, COUNT(n) FROM Notification n WHERE n.destinataire.id = :destinataireId GROUP BY n.type")
    List<Object[]> countByTypeForUser(@Param("destinataireId") UUID destinataireId);

    /**
     * Get notification statistics for the entire system.
     *
     * @return notification statistics
     */
    @Query("SELECT n.statut, n.type, COUNT(n) FROM Notification n GROUP BY n.statut, n.type")
    List<Object[]> getNotificationStatistics();

    /**
     * Count notifications sent today.
     *
     * @return count of notifications sent today
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.dateEnvoi >= :startOfDay AND n.dateEnvoi < :endOfDay")
    long countNotificationsSentToday(@Param("startOfDay") LocalDateTime startOfDay, 
                                    @Param("endOfDay") LocalDateTime endOfDay);

    /**
     * Count notifications sent this week.
     *
     * @return count of notifications sent this week
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.dateEnvoi >= :startOfWeek")
    long countNotificationsSentThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek);

    /**
     * Count notifications sent this month.
     *
     * @param startOfMonth start of current month
     * @param endOfMonth   end of current month
     * @return count of notifications sent this month
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.dateEnvoi >= :startOfMonth AND n.dateEnvoi < :endOfMonth")
    long countNotificationsSentThisMonth(@Param("startOfMonth") LocalDateTime startOfMonth,
                                        @Param("endOfMonth") LocalDateTime endOfMonth);

    // ====== Advanced Search and Filtering ======

    /**
     * Search notifications by content (case-insensitive).
     *
     * @param keyword the search keyword
     * @return list of notifications containing the keyword
     */
    @Query("SELECT n FROM Notification n WHERE LOWER(n.contenu) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.sujet) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Notification> searchByContent(@Param("keyword") String keyword);

    /**
     * Find notifications by recipient user with advanced filtering.
     *
     * @param destinataireId the recipient user ID
     * @param statut         the notification status (optional)
     * @param type           the notification type (optional)
     * @param startDate      start date (optional)
     * @param endDate        end date (optional)
     * @return list of filtered notifications
     */
    @Query("SELECT n FROM Notification n WHERE n.destinataire.id = :destinataireId " +
           "AND (:statut IS NULL OR n.statut = :statut) " +
           "AND (:type IS NULL OR n.type = :type) " +
           "AND (:startDate IS NULL OR n.dateEnvoi >= :startDate) " +
           "AND (:endDate IS NULL OR n.dateEnvoi <= :endDate) " +
           "ORDER BY n.dateEnvoi DESC")
    List<Notification> findWithAdvancedFiltering(@Param("destinataireId") UUID destinataireId,
                                                @Param("statut") Notification.Statut statut,
                                                @Param("type") Notification.Type type,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    /**
     * Find notifications that need to be sent (pending and scheduled).
     *
     * @param currentTime current time
     * @return list of notifications ready to be sent
     */
    @Query("SELECT n FROM Notification n WHERE n.statut = 'EN_ATTENTE' AND (n.dateEnvoi IS NULL OR n.dateEnvoi <= :currentTime)")
    List<Notification> findNotificationsToSend(@Param("currentTime") LocalDateTime currentTime);

    // ====== Batch Operations ======

    /**
     * Mark all notifications as read for a user.
     *
     * @param destinataireId the recipient user ID
     * @param readTime       the time when notifications were marked as read
     * @return number of notifications updated
     */
    @Query("UPDATE Notification n SET n.statut = 'LU', n.dateLecture = :readTime WHERE n.destinataire.id = :destinataireId AND n.statut = 'EN_ATTENTE'")
    int markAllAsReadForUser(@Param("destinataireId") UUID destinataireId, 
                            @Param("readTime") LocalDateTime readTime);

    /**
     * Delete old notifications (older than specified date).
     *
     * @param cutoffDate cutoff date for deletion
     * @return number of notifications deleted
     */
    @Query("DELETE FROM Notification n WHERE n.dateEnvoi < :cutoffDate")
    int deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Retry failed notifications.
     *
     * @return number of notifications reset for retry
     */
    @Query("UPDATE Notification n SET n.statut = 'EN_ATTENTE' WHERE n.statut = 'ECHEC'")
    int retryFailedNotifications();

    // ====== Performance and Monitoring ======

    /**
     * Find notifications with delivery issues.
     *
     * @return list of notifications with delivery issues
     */
    @Query("SELECT n FROM Notification n WHERE n.statut = 'ECHEC' OR (n.statut = 'EN_ATTENTE' AND n.dateEnvoi < :threshold)")
    List<Notification> findNotificationsWithDeliveryIssues(@Param("threshold") LocalDateTime threshold);

    /**
     * Get notification delivery success rate.
     *
     * @return delivery success rate statistics
     */
    @Query("SELECT " +
           "COUNT(CASE WHEN n.statut = 'ENVOYE' THEN 1 END) as sent, " +
           "COUNT(CASE WHEN n.statut = 'ECHEC' THEN 1 END) as failed, " +
           "COUNT(CASE WHEN n.statut = 'LU' THEN 1 END) as read " +
           "FROM Notification n")
    Object[] getDeliverySuccessRate();

    /**
     * Find notifications by priority (based on type and content).
     *
     * @return list of high-priority notifications
     */
    @Query("SELECT n FROM Notification n WHERE n.type = 'PUSH' OR n.sujet LIKE '%urgent%' OR n.sujet LIKE '%important%' ORDER BY n.dateEnvoi DESC")
    List<Notification> findHighPriorityNotifications();
} 