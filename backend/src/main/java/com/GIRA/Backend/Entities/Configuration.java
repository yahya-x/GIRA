package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * <p>
 * Entity representing a dynamic application configuration setting.
 * Stores key-value pairs for runtime configuration, supporting multiple data types and audit fields.
 * </p>
 *
 * Example keys: TEMPS_MAX_RESOLUTION_HEURES, EMAIL_TEMPLATE_CONFIRMATION, LANGUES_SUPPORTEES, CATEGORIES_ACTIVES
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "configurations")
public class Configuration extends BaseEntity {

    /**
     * Unique key for the configuration setting (not null, unique).
     */
    @Column(name = "cle", nullable = false, unique = true, length = 100)
    private String cle;

    /**
     * Value of the configuration setting (can be string, int, bool, JSON, etc.).
     */
    @Column(name = "valeur", columnDefinition = "TEXT")
    private String valeur;

    /**
     * Data type of the configuration value (STRING, INTEGER, BOOLEAN, JSON).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private Type type;

    /**
     * Description of what this configuration controls.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Date and time when the configuration was last modified.
     */
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    /**
     * The user who last modified the configuration.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifie_par")
    private User modifiePar;

    /**
     * Default constructor.
     */
    public Configuration() {}

    /**
     * Enum representing the data type of the configuration value.
     */
    public enum Type {
        STRING, INTEGER, BOOLEAN, JSON
    }
} 