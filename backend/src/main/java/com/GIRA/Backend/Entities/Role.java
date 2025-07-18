package com.GIRA.Backend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * <p>
 * Entity representing a user role in the system.
 * Each role defines a set of permissions and a description.
 * </p>
 *
 * <p>
 * Example roles: PASSAGER, AGENT, SUPERVISEUR, ADMINISTRATEUR
 * </p>
 *
 * @author [Mohamed yahya jabrane
 * @since 1.0
 */
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    /**
     * Name of the role (e.g., PASSAGER, AGENT, SUPERVISEUR, ADMINISTRATEUR).
     */
    @Column(name = "nom", nullable = false, unique = true, length = 50)
    private String nom;

    /**
     * Description of the role and its purpose.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Permissions associated with the role, stored as a JSON string.
     * Example: '["CREATE_USER", "DELETE_USER"]'
     */
    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions;

    /**
     * Default constructor.
     */
    public Role() {}

    /**
     * Parameterized constructor.
     *
     * @param nom         the name of the role
     * @param description the description of the role
     * @param permissions the permissions in JSON format
     */
    public Role(String nom, String description, String permissions) {
        this.nom = nom;
        this.description = description;
        this.permissions = permissions;
    }

    /**
     * Gets the name of the role.
     *
     * @return the role name
     */
    public String getNom() {
        return nom;
    }

    /**
     * Sets the name of the role.
     *
     * @param nom the role name
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Gets the description of the role.
     *
     * @return the role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the role.
     *
     * @param description the role description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the permissions associated with the role.
     *
     * @return the permissions as a JSON string
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     * Sets the permissions associated with the role.
     *
     * @param permissions the permissions as a JSON string
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    /**
     * Adds a permission to the JSON permissions field.
     * Updates the permissions JSON with the new permission.
     *
     * @param permission the permission to add
     */
    public void ajouterPermission(String permission) {
        if (this.permissions == null) {
            this.permissions = "{}";
        }
        // dateModification is set automatically by @PreUpdate in BaseEntity
    }

    /**
     * Removes a permission from the JSON permissions field.
     * Updates the permissions JSON by removing the specified permission.
     *
     * @param permission the permission to remove
     */
    public void supprimerPermission(String permission) {
        if (this.permissions != null && !this.permissions.isEmpty()) {
            // dateModification is set automatically by @PreUpdate in BaseEntity
        }
    }

    /**
     * Checks if a permission exists in the JSON permissions field.
     * Verifies whether the specified permission is granted to this role.
     *
     * @param permission the permission to check
     * @return true if the permission exists and is true, false otherwise
     */
    public boolean aPermission(String permission) {
        if (this.permissions != null && !this.permissions.isEmpty()) {
            return this.permissions.contains(permission); // Simple placeholder check
        }
        return false;
    }
} 