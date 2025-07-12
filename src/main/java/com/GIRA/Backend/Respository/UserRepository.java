package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    // Find by email
    Optional<User> findByEmail(String email);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find by email and active status
    Optional<User> findByEmailAndActif(String email, Boolean actif);

    // Find by role
    List<User> findByRole_Id(UUID roleId);

    // Find by role and active status
    List<User> findByRole_IdAndActif(UUID roleId, Boolean actif);

    // Search by name or surname (case-insensitive)
    @Query("SELECT u FROM User u WHERE (LOWER(u.nom) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(u.prenom) LIKE LOWER(CONCAT('%', :terme, '%'))) AND u.actif = true")
    List<User> findByNomOrPrenomContainingIgnoreCase(@Param("terme") String terme);

    // Advanced search with filters and pagination
    @Query("SELECT u FROM User u WHERE " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:nom IS NULL OR LOWER(u.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:prenom IS NULL OR LOWER(u.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
           "(:roleId IS NULL OR u.role.id = :roleId) AND " +
           "(:actif IS NULL OR u.actif = :actif) AND " +
           "(:emailVerifie IS NULL OR u.emailVerifie = :emailVerifie)")
    Page<User> findWithFilters(
        @Param("email") String email,
        @Param("nom") String nom,
        @Param("prenom") String prenom,
        @Param("roleId") UUID roleId,
        @Param("actif") Boolean actif,
        @Param("emailVerifie") Boolean emailVerifie,
        Pageable pageable
    );

    // Users created within a period
    @Query("SELECT u FROM User u WHERE u.dateCreation BETWEEN :dateDebut AND :dateFin")
    List<User> findByDateCreationBetween(@Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

    // Active users with verified email
    List<User> findByActifTrueAndEmailVerifieTrue();

    // Users by language
    List<User> findByLangue(String langue);

    // Count users by role
    @Query("SELECT COUNT(u) FROM User u WHERE u.role.id = :roleId AND u.actif = true")
    long countByRoleIdAndActif(@Param("roleId") UUID roleId);

    // User statistics
    @Query("SELECT COUNT(u) as total, COUNT(CASE WHEN u.actif = true THEN 1 END) as actifs, COUNT(CASE WHEN u.emailVerifie = true THEN 1 END) as emailsVerifies FROM User u")
    Object[] getUserStatistics();

    // Update last login
    @Modifying
    @Query("UPDATE User u SET u.derniereConnexion = :date WHERE u.id = :id")
    void updateDerniereConnexion(@Param("id") UUID id, @Param("date") LocalDateTime date);

    // Activate/deactivate user
    @Modifying
    @Query("UPDATE User u SET u.actif = :actif WHERE u.id = :id")
    void updateActifStatus(@Param("id") UUID id, @Param("actif") Boolean actif);

    // Verify email
    @Modifying
    @Query("UPDATE User u SET u.emailVerifie = true WHERE u.id = :id")
    void verifyEmail(@Param("id") UUID id);

    // Users assigned as agents
    @Query("SELECT DISTINCT u FROM User u JOIN Reclamation r ON u.id = r.agentAssigne.id WHERE u.actif = true")
    List<User> findAgentsWithAssignedReclamations();

    // Users with the most complaints
    @Query("SELECT u FROM User u LEFT JOIN Reclamation r ON u.id = r.utilisateur.id WHERE u.actif = true GROUP BY u.id ORDER BY COUNT(r.id) DESC")
    List<User> findUsersOrderByReclamationCount(Pageable pageable);
} 