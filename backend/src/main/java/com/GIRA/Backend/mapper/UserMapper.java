package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.DTO.request.UserCreateRequest;
import com.GIRA.Backend.DTO.request.UserUpdateRequest;
import com.GIRA.Backend.DTO.response.UserResponse;
import com.GIRA.Backend.DTO.response.RoleResponse;

/**
 * Utility class for mapping between User entities and DTOs.
 * Provides static methods for converting between entity and DTO representations.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
public class UserMapper {

    /**
     * Convertit une entité User en UserResponse DTO.
     *
     * @param user l'entité utilisateur
     * @return le DTO de réponse utilisateur
     */
    public static UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .telephone(user.getTelephone())
                .langue(user.getLangue())
                .emailVerifie(user.isEmailVerifie())
                .role(user.getRole() != null ? RoleMapper.toResponse(user.getRole()) : null)
                .actif(user.isActif())
                .dateCreation(user.getDateCreation())
                .build();
    }

    /**
     * Converts a UserCreateRequest DTO to a User entity.
     * Maps all available fields for professional user creation.
     *
     * @param request the DTO containing user creation data
     * @return the mapped User entity
     */
    public static User fromCreateRequest(UserCreateRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setEmail(request.getEmail());
        user.setMotDePasse(request.getPassword());
        user.setNom(request.getUsername());
        // Map additional fields if present in the DTO
        if (request.getPrenom() != null) user.setPrenom(request.getPrenom());
        if (request.getTelephone() != null) user.setTelephone(request.getTelephone());
        if (request.getLangue() != null) user.setLangue(request.getLangue());
        return user;
    }

    /**
     * Met à jour une entité User à partir d'un UserUpdateRequest.
     *
     * @param user l'entité utilisateur à mettre à jour
     * @param request la requête de mise à jour
     */
    public static void updateUserFromRequest(User user, UserUpdateRequest request) {
        if (user == null || request == null) return;
        if (request.getUsername() != null) user.setNom(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
    }
} 