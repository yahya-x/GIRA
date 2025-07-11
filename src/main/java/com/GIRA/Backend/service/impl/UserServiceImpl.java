package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.UserService;
import com.GIRA.Backend.Respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.GIRA.Backend.Entities.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Implementation of UserService.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user.
     * @param user User data
     * @return Registered user
     */
    @Override
    public User registerUser(User user) {
        // TODO: Add password encoding, validation, etc.
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by ID.
     * @param id User UUID
     * @return User entity
     */
    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all users.
     * @return List of users
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates a user's profile.
     * @param id User UUID
     * @param user Updated user data
     * @return Updated user entity
     */
    @Override
    public User updateUser(UUID id, User user) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing == null) return null;
        // TODO: Update only allowed fields
        existing.setNom(user.getNom());
        existing.setPrenom(user.getPrenom());
        existing.setTelephone(user.getTelephone());
        existing.setLangue(user.getLangue());
        existing.setPreferences(user.getPreferences());
        // ... add more fields as needed
        return userRepository.save(existing);
    }

    /**
     * Deletes a user by ID.
     * @param id User UUID
     */
    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    /**
     * Authenticates a user by email and password.
     * @param email User email
     * @param password User password
     * @return Authenticated user
     */
    @Override
    public User authenticate(String email, String password) {
        // TODO: Add password encoding and secure comparison
        return userRepository.findByEmail(email)
                .filter(u -> u.getMotDePasse().equals(password))
                .orElse(null);
    }

    /**
     * Resets a user's password by email.
     * @param email User email
     */
    @Override
    public void resetPassword(String email) {
        // TODO: Generate token, send email, etc.
        // Example: set tokenResetPassword and save
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setTokenResetPassword(UUID.randomUUID().toString());
            userRepository.save(user);
        });
    }

    /**
     * Checks if an email exists.
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Finds users by role ID.
     * @param roleId Role UUID
     * @return List of users
     */
    @Override
    public List<User> findByRoleId(UUID roleId) {
        return userRepository.findByRole_Id(roleId);
    }

    /**
     * Finds users by role ID and active status.
     * @param roleId Role UUID
     * @param actif Active status
     * @return List of users
     */
    @Override
    public List<User> findByRoleIdAndActif(UUID roleId, Boolean actif) {
        return userRepository.findByRole_IdAndActif(roleId, actif);
    }

    /**
     * Searches users by name or surname (case-insensitive).
     * @param terme Search term
     * @return List of users
     */
    @Override
    public List<User> searchByNomOrPrenom(String terme) {
        return userRepository.findByNomOrPrenomContainingIgnoreCase(terme);
    }

    /**
     * Advanced search with filters and pagination.
     * @param email Email filter (optional)
     * @param nom Name filter (optional)
     * @param prenom First name filter (optional)
     * @param roleId Role ID filter (optional)
     * @param actif Active status filter (optional)
     * @param emailVerifie Email verified filter (optional)
     * @param pageable Pagination parameters
     * @return Page of users
     */
    @Override
    public Page<User> findWithFilters(String email, String nom, String prenom, UUID roleId, Boolean actif, Boolean emailVerifie, Pageable pageable) {
        return userRepository.findWithFilters(email, nom, prenom, roleId, actif, emailVerifie, pageable);
    }

    /**
     * Finds users created within a date range.
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of users
     */
    @Override
    public List<User> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return userRepository.findByDateCreationBetween(dateDebut, dateFin);
    }

    /**
     * Finds all active users with verified email.
     * @return List of active and verified users
     */
    @Override
    public List<User> findActiveAndVerified() {
        return userRepository.findByActifTrueAndEmailVerifieTrue();
    }

    /**
     * Finds users by language.
     * @param langue Language preference
     * @return List of users
     */
    @Override
    public List<User> findByLangue(String langue) {
        return userRepository.findByLangue(langue);
    }

    /**
     * Counts users by role.
     * @param roleId Role UUID
     * @return Number of users
     */
    @Override
    public long countByRoleIdAndActif(UUID roleId) {
        return userRepository.countByRoleIdAndActif(roleId);
    }

    /**
     * Gets user statistics (total, active, verified emails).
     * @return Array with total, active, and verified counts
     */
    @Override
    public Object[] getUserStatistics() {
        return userRepository.getUserStatistics();
    }

    /**
     * Updates last login date for a user.
     * @param id User UUID
     * @param date Last login date
     */
    @Override
    public void updateDerniereConnexion(UUID id, LocalDateTime date) {
        userRepository.updateDerniereConnexion(id, date);
    }

    /**
     * Activates or deactivates a user.
     * @param id User UUID
     * @param actif Active status
     */
    @Override
    public void updateActifStatus(UUID id, Boolean actif) {
        userRepository.updateActifStatus(id, actif);
    }

    /**
     * Verifies a user's email.
     * @param id User UUID
     */
    @Override
    public void verifyEmail(UUID id) {
        userRepository.verifyEmail(id);
    }

    /**
     * Finds agents with assigned complaints.
     * @return List of agents with assigned complaints
     */
    @Override
    public List<User> findAgentsWithAssignedReclamations() {
        return userRepository.findAgentsWithAssignedReclamations();
    }

    /**
     * Finds users with the most complaints (paginated).
     * @param pageable Pagination parameters
     * @return List of users ordered by complaint count
     */
    @Override
    public List<User> findUsersOrderByReclamationCount(Pageable pageable) {
        return userRepository.findUsersOrderByReclamationCount(pageable);
    }
} 