package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * Abstract base class for all persistent entities in the application.
 * Provides a unique identifier, creation and modification timestamps, and an active status flag.
 * </p>
 *
 * <p>
 * All entities should extend this class to inherit common audit fields and behaviors.
 * </p>
 *
 * @author Mohamed yahya Jabrane
 * @since 1.0
 */
@MappedSuperclass
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEntity   {
    /**
     * Universally unique identifier for the entity.
     * Generated automatically upon persistence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    protected UUID id;

    /**
     * Timestamp indicating when the entity was created.
     * Set automatically when the entity is persisted.
     */
    @Column(name = "date_creation", nullable = false)
    @CreationTimestamp
    private LocalDateTime dateCreation;

    /**
     * Timestamp indicating the last time the entity was modified.
     * Updated automatically on each update.
     */
    @Column(name = "date_modification")
    @UpdateTimestamp
    private LocalDateTime dateModification;

    /**
     * Indicates whether the entity is active.
     * Default is {@code true} upon creation.
     */
    @Column(name = "actif" , nullable = false)
    private boolean actif = true;

    /**
     * Initializes audit fields before the entity is persisted.
     * Sets creation and modification timestamps, and marks as active.
     */
    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
        if (!actif) {
            actif = true;
        }
    }

    /**
     * Updates the modification timestamp before the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }



}
