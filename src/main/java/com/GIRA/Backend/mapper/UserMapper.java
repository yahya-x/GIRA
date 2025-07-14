package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.DTO.request.UserCreateRequest;
import com.GIRA.Backend.DTO.request.UserUpdateRequest;
import com.GIRA.Backend.DTO.response.UserResponse;
import com.GIRA.Backend.DTO.response.RoleResponse;

/**
 * Classe utilitaire pour le mapping entre les entités User et les DTOs User.
 * Fournit des méthodes statiques pour convertir entre User, UserCreateRequest, UserUpdateRequest et UserResponse.
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
     * Convertit un UserCreateRequest en entité User.
     *
     * @param request la requête de création d'utilisateur
     * @return l'entité utilisateur
     */
    public static User fromCreateRequest(UserCreateRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setEmail(request.getEmail());
        user.setMotDePasse(request.getPassword());
        user.setNom(request.getUsername());
        // Prénom, téléphone, langue, etc. peuvent être ajoutés si présents dans le DTO
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