package com.GIRA.Backend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

/**
 * Test configuration for the GIRA application.
 * <p>
 * Provides mock implementations for external dependencies during testing,
 * including email services, template engines, and Jackson configuration
 * optimized for test environments.
 * </p>
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 2025-07-14
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Provides a mock JavaMailSender for tests.
     * <p>
     * This prevents actual email sending during test execution while maintaining
     * the same interface for testing email-related functionality.
     * </p>
     *
     * @return Mock JavaMailSender implementation
     */
    @Bean
    @Primary
    public JavaMailSender mockJavaMailSender() {
        return new JavaMailSender() {
            @Override
            public jakarta.mail.internet.MimeMessage createMimeMessage() {
                return null;
            }

            @Override
            public jakarta.mail.internet.MimeMessage createMimeMessage(java.io.InputStream contentStream) {
                return null;
            }

            @Override
            public void send(SimpleMailMessage simpleMessage) {
                // Mock implementation - do nothing
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) {
                // Mock implementation - do nothing
            }

            @Override
            public void send(MimeMessagePreparator mimeMessagePreparator) {
                // Mock implementation - do nothing
            }

            @Override
            public void send(MimeMessagePreparator... mimeMessagePreparators) {
                // Mock implementation - do nothing
            }

            @Override
            public void send(jakarta.mail.internet.MimeMessage mimeMessage) {
                // Mock implementation - do nothing
            }

            @Override
            public void send(jakarta.mail.internet.MimeMessage... mimeMessages) {
                // Mock implementation - do nothing
            }
        };
    }

    /**
     * Provides a simple TemplateEngine for tests.
     * <p>
     * This allows email templates to be processed during tests without requiring
     * complex template resolution. The bean is named 'testTemplateEngine' to avoid
     * conflicts with the default Thymeleaf auto-configuration.
     * </p>
     *
     * @return Configured TemplateEngine for testing
     */
    @Bean(name = "testTemplateEngine")
    public TemplateEngine testTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    /**
     * Provides a test-specific ObjectMapper configuration.
     * <p>
     * This ObjectMapper is optimized for testing with relaxed serialization settings
     * and proper Java 8 date/time support to prevent serialization issues during tests.
     * Note: This bean is NOT marked as @Primary to avoid conflicts with the main ObjectMapper.
     * </p>
     *
     * @return Configured ObjectMapper for testing
     */
    @Bean("testObjectMapper")
    public ObjectMapper testObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Register JavaTimeModule for Java 8 date/time support
        objectMapper.registerModule(new JavaTimeModule());
        
        // Configure serialization features for tests
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        
        // Configure deserialization features for tests (more permissive)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        
        // Configure inclusion features
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        
        return objectMapper;
    }
} 