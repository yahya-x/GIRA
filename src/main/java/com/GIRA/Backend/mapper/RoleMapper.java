package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.Role;
import com.GIRA.Backend.DTO.response.RoleResponse;
import java.util.Collections;
import java.util.List;

/**
 * Classe utilitaire pour le mapping entre l'entité Role et le DTO RoleResponse.
 * Fournit des méthodes statiques pour convertir entre Role et RoleResponse.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
public class RoleMapper {
    public static RoleResponse toResponse(Role role) {
        if (role == null) return null;
        return RoleResponse.builder()
                .id(role.getId() != null ? role.getId().toString() : null)
                .nom(role.getNom())
                .description(role.getDescription())
                .permissions(null) // TODO: parse role.getPermissions() JSON if needed
                .actif(role.isActif())
                .dateCreation(role.getDateCreation())
                .dateModification(role.getDateModification())
                .build();
    }
} 