package com.GIRA.Backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Email configuration for the GIRA application.
 * Provides different email configurations for development, testing, and production environments.
 * 
 * @author Mohamed yahya jabrane
 * @since 1.0
 */
@Configuration
public class EmailConfig {

    private static final Logger logger = LoggerFactory.getLogger(EmailConfig.class);

    @Value("${spring.mail.host:localhost}")
    private String host;

    @Value("${spring.mail.port:1025}")
    private int port;

    @Value("${spring.mail.username:dev@gira.local}")
    private String username;

    @Value("${spring.mail.password:dev-password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth:false}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:false}")
    private boolean starttls;

    /**
     * Creates a JavaMailSender bean for development and production environments.
     * This bean is only created when email is enabled.
     */
    @Bean
    @Profile({"dev", "prod", "default"})
    public JavaMailSender javaMailSender() {
        logger.info("Configuring JavaMailSender for host: {}:{}", host, port);
        
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", "false");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        return mailSender;
    }

    /**
     * Creates a mock JavaMailSender for test environment.
     * This prevents email sending during tests while maintaining the bean structure.
     * Note: This bean is only created when no other JavaMailSender bean exists,
     * allowing TestConfig to provide the primary mock implementation.
     */
    @Bean
    @Profile("test")
    @ConditionalOnMissingBean(name = "mockJavaMailSender")
    public JavaMailSender testJavaMailSender() {
        logger.info("Configuring fallback JavaMailSender for test environment");
        
        return new JavaMailSender() {
            @Override
            public jakarta.mail.internet.MimeMessage createMimeMessage() {
                logger.debug("Fallback Mock: Creating MimeMessage");
                return null;
            }

            @Override
            public jakarta.mail.internet.MimeMessage createMimeMessage(java.io.InputStream contentStream) {
                logger.debug("Fallback Mock: Creating MimeMessage from stream");
                return null;
            }

            @Override
            public void send(SimpleMailMessage simpleMessage) {
                logger.debug("Fallback Mock: Sending SimpleMailMessage to: {}", 
                    simpleMessage != null ? simpleMessage.getTo() : "null");
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) {
                logger.debug("Fallback Mock: Sending {} SimpleMailMessages", 
                    simpleMessages != null ? simpleMessages.length : 0);
            }

            @Override
            public void send(MimeMessagePreparator mimeMessagePreparator) {
                logger.debug("Fallback Mock: Sending MimeMessagePreparator");
            }

            @Override
            public void send(MimeMessagePreparator... mimeMessagePreparators) {
                logger.debug("Fallback Mock: Sending {} MimeMessagePreparators", 
                    mimeMessagePreparators != null ? mimeMessagePreparators.length : 0);
            }

            @Override
            public void send(jakarta.mail.internet.MimeMessage mimeMessage) {
                logger.debug("Fallback Mock: Sending MimeMessage");
            }

            @Override
            public void send(jakarta.mail.internet.MimeMessage... mimeMessages) {
                logger.debug("Fallback Mock: Sending {} MimeMessages", 
                    mimeMessages != null ? mimeMessages.length : 0);
            }
        };
    }
} 