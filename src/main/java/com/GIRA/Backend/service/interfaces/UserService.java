package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.DTO.request.UserCreateRequest;
import com.GIRA.Backend.DTO.request.UserUpdateRequest;
import com.GIRA.Backend.DTO.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing users.
 * Provides user registration, authentication, profile management, and advanced queries.
 * @author Mohamed Yahya Jabrane
 */
public interface UserService {
    /**
     * Registers a new user.
     * @param user User entity to register
     * @return Registered user
     */
    User registerUser(User user);

    /**
     * Retrieves a user by ID.
     * @param id User UUID
     * @return User entity
     */
    User getUserById(UUID id);

    /**
     * Retrieves all users.
     * @return List of users
     */
    List<User> getAllUsers();

    /**
     * Updates a user's profile.
     * @param id User UUID
     * @param user Updated user data
     * @return Updated user entity
     */
    User updateUser(UUID id, User user);

    /**
     * Deletes a user by ID.
     * @param id User UUID
     */
    void deleteUser(UUID id);

    /**
     * Authenticates a user by email and password.
     * @param email User email
     * @param password User password
     * @return Authenticated user
     */
    User authenticate(String email, String password);

    /**
     * Resets a user's password by email.
     * @param email User email
     */
    void resetPassword(String email);

    /**
     * Checks if an email exists.
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds users by role ID.
     * @param roleId Role UUID
     * @return List of users
     */
    List<User> findByRoleId(UUID roleId);

    /**
     * Finds users by role ID and active status.
     * @param roleId Role UUID
     * @param actif Active status
     * @return List of users
     */
    List<User> findByRoleIdAndActif(UUID roleId, Boolean actif);

    /**
     * Searches users by name or surname (case-insensitive).
     * @param terme Search term
     * @return List of users
     */
    List<User> searchByNomOrPrenom(String terme);

    /**
     * Advanced search with filters and pagination.
     */
    Page<User> findWithFilters(String email, String nom, String prenom, UUID roleId, Boolean actif, Boolean emailVerifie, Pageable pageable);

    /**
     * Finds users created within a date range.
     */
    List<User> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Finds all active users with verified email.
     */
    List<User> findActiveAndVerified();

    /**
     * Finds users by language.
     */
    List<User> findByLangue(String langue);

    /**
     * Counts users by role.
     */
    long countByRoleIdAndActif(UUID roleId);

    /**
     * Gets user statistics (total, active, verified emails).
     */
    Object[] getUserStatistics();

    /**
     * Updates last login date for a user.
     */
    void updateDerniereConnexion(UUID id, LocalDateTime date);

    /**
     * Activates or deactivates a user.
     */
    void updateActifStatus(UUID id, Boolean actif);

    /**
     * Verifies a user's email.
     */
    void verifyEmail(UUID id);

    /**
     * Finds agents with assigned complaints.
     */
    List<User> findAgentsWithAssignedReclamations();

    /**
     * Finds users with the most complaints (paginated).
     */
    List<User> findUsersOrderByReclamationCount(Pageable pageable);

    /**
     * Finds a user by email.
     */
    java.util.Optional<User> findByEmail(String email);

    /**
     * Crée un nouvel utilisateur à partir d'un DTO de requête.
     * @param request la requête de création d'utilisateur
     * @return la réponse utilisateur créée
     */
    UserResponse createUser(UserCreateRequest request);

    /**
     * Met à jour un utilisateur à partir d'un DTO de requête.
     * @param id l'identifiant de l'utilisateur
     * @param request la requête de mise à jour
     * @return la réponse utilisateur mise à jour
     */
    UserResponse updateUser(UUID id, UserUpdateRequest request);

    /**
     * Récupère un utilisateur par son identifiant et le convertit en DTO de réponse.
     * @param id l'identifiant de l'utilisateur
     * @return la réponse utilisateur
     */
    UserResponse getUserByIdDto(UUID id);

    /**
     * Recherche avancée paginée et filtrée des utilisateurs, retourne des DTOs de réponse.
     * @param email filtre email (optionnel)
     * @param nom filtre nom (optionnel)
     * @param prenom filtre prénom (optionnel)
     * @param roleId filtre rôle (optionnel)
     * @param actif filtre actif (optionnel)
     * @param emailVerifie filtre email vérifié (optionnel)
     * @param pageable pagination et tri
     * @return page de réponses utilisateur
     */
    Page<UserResponse> findWithFiltersDto(String email, String nom, String prenom, UUID roleId, Boolean actif, Boolean emailVerifie, Pageable pageable);
} 