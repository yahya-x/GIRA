package com.GIRA.Backend.Config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson configuration for proper JSON serialization and deserialization.
 * <p>
 * This configuration provides comprehensive settings for handling Java 8 date/time types,
 * null value handling, and other serialization features to ensure consistent API responses.
 * </p>
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 2025-07-14
 */
@Configuration
public class JacksonConfig {

    /**
     * Configures the primary ObjectMapper with comprehensive settings.
     * <p>
     * This ObjectMapper is configured with:
     * - Java 8 date/time support via JavaTimeModule
     * - ISO-8601 date format for timestamps
     * - Null value exclusion for cleaner JSON
     * - Fail-safe deserialization features
     * - Pretty printing for development
     * </p>
     *
     * @return configured ObjectMapper instance
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Register JavaTimeModule for Java 8 date/time support
        objectMapper.registerModule(new JavaTimeModule());
        
        // Configure serialization features
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false); // Set to true for development if needed
        
        // Configure deserialization features
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