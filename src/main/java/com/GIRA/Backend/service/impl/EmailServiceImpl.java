package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of EmailService.
 * Provides email sending functionality for verification, notifications, and other communications.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Sends a verification email to the specified recipient.
     * @param to The recipient email address
     * @param token The verification token
     */
    @Override
    public void sendVerificationEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Vérification de votre compte GIRA");

            // Create context for template
            Context context = new Context();
            context.setVariable("verificationUrl", frontendUrl + "/verify-email?token=" + token);
            context.setVariable("userEmail", to);

            // Process template
            String htmlContent = templateEngine.process("email-verification", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Verification email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send verification email to: {}", to, e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    /**
     * Sends a notification email to the specified recipient.
     * @param to The recipient email address
     * @param subject The email subject
     * @param body The email body content
     */
    @Override
    public void sendNotificationEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            // Create context for template
            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("body", body);
            context.setVariable("userEmail", to);

            // Process template
            String htmlContent = templateEngine.process("notification-email", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Notification email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send notification email to: {}", to, e);
            throw new RuntimeException("Failed to send notification email", e);
        }
    }
} 