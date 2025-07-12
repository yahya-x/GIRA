package com.GIRA.Backend.service.interfaces;

/**
 * Service interface for sending emails (verification, notifications, etc.).
 * @author Mohamed Yahya Jabrane
 */
public interface EmailService {
    void sendVerificationEmail(String to, String token);
    void sendNotificationEmail(String to, String subject, String body);
    // Add more as needed
} 