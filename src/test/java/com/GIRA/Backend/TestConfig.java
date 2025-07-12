package com.GIRA.Backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Properties;

/**
 * Test configuration to provide mock beans for testing.
 * This resolves dependency issues with JavaMailSender and TemplateEngine.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Provides a mock JavaMailSender for tests.
     * This prevents the application from trying to connect to a real mail server during tests.
     */
    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(1025); // Use a dummy port
        mailSender.setUsername("test@example.com");
        mailSender.setPassword("password");
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "false");
        
        return mailSender;
    }

    /**
     * Provides a mock TemplateEngine for tests.
     * This allows email templates to be processed without requiring actual template files.
     */
    @Bean
    @Primary
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
        
        return templateEngine;
    }
} 