package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.Role;
import com.GIRA.Backend.DTO.response.RoleResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
    /**
     * Mappe l'entité Role vers le DTO RoleResponse, en convertissant les permissions JSON en liste.
     *
     * @param role entité Role
     * @return DTO RoleResponse
     */
    public static RoleResponse toResponse(Role role) {
        if (role == null) return null;
        List<String> permissionsList = Collections.emptyList();
        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                permissionsList = mapper.readValue(role.getPermissions(), new TypeReference<List<String>>() {});
            } catch (IOException e) {
                // Log or handle error: permissions JSON is malformed
                permissionsList = Collections.emptyList();
            }
        }
        return RoleResponse.builder()
                .id(role.getId() != null ? role.getId().toString() : null)
                .nom(role.getNom())
                .description(role.getDescription())
                .permissions(permissionsList)
                .actif(role.isActif())
                .dateCreation(role.getDateCreation())
                .dateModification(role.getDateModification())
                .build();
    }
} 