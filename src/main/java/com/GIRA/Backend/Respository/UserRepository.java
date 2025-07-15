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

/**
 * Repository interface for managing User entities.
 * Provides data access methods for user management including authentication, search, and statistics.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    /**
     * Finds a user by email address.
     * @param email the user's email
     * @return optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    /**
     * Checks if a user exists with the given email.
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
    /**
     * Finds a user by email and active status.
     * @param email the user's email
     * @param actif the active status
     * @return optional containing the user if found
     */
    Optional<User> findByEmailAndActif(String email, Boolean actif);
    /**
     * Finds users by role ID.
     * @param roleId the role UUID
     * @return list of users with the specified role
     */
    List<User> findByRole_Id(UUID roleId);
    /**
     * Finds users by role ID and active status.
     * @param roleId the role UUID
     * @param actif the active status
     * @return list of users with the specified role and active status
     */
    List<User> findByRole_IdAndActif(UUID roleId, Boolean actif);
    /**
     * Searches users by name or surname (case-insensitive).
     * @param terme the search term
     * @return list of active users matching the search term
     */
    @Query("SELECT u FROM User u WHERE (LOWER(u.nom) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(u.prenom) LIKE LOWER(CONCAT('%', :terme, '%'))) AND u.actif = true")
    List<User> findByNomOrPrenomContainingIgnoreCase(@Param("terme") String terme);
    /**
     * Advanced search with filters and pagination.
     * @param email email filter (optional)
     * @param nom name filter (optional)
     * @param prenom first name filter (optional)
     * @param roleId role ID filter (optional)
     * @param actif active status filter (optional)
     * @param emailVerifie email verified filter (optional)
     * @param pageable pagination parameters
     * @return page of users matching the filters
     */
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
    /**
     * Finds users created within a date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return list of users created in the date range
     */
    @Query("SELECT u FROM User u WHERE u.dateCreation BETWEEN :dateDebut AND :dateFin")
    List<User> findByDateCreationBetween(@Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);
    /**
     * Finds active users with verified email.
     * @return list of active users with verified email
     */
    List<User> findByActifTrueAndEmailVerifieTrue();
    /**
     * Finds users by language preference.
     * @param langue the language preference
     * @return list of users with the specified language preference
     */
    List<User> findByLangue(String langue);
    /**
     * Counts users by role and active status.
     * @param roleId the role UUID
     * @return number of active users with the specified role
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.role.id = :roleId AND u.actif = true")
    long countByRoleIdAndActif(@Param("roleId") UUID roleId);
    /**
     * Gets user statistics (total, active, verified emails).
     * @return array with total, active, and verified counts
     */
    @Query("SELECT COUNT(u) as total, COUNT(CASE WHEN u.actif = true THEN 1 END) as actifs, COUNT(CASE WHEN u.emailVerifie = true THEN 1 END) as emailsVerifies FROM User u")
    Object[] getUserStatistics();
    /**
     * Updates the last login date for a user.
     * @param id the user UUID
     * @param date the last login date
     */
    @Modifying
    @Query("UPDATE User u SET u.derniereConnexion = :date WHERE u.id = :id")
    void updateDerniereConnexion(@Param("id") UUID id, @Param("date") LocalDateTime date);
    /**
     * Activates or deactivates a user.
     * @param id the user UUID
     * @param actif the active status
     */
    @Modifying
    @Query("UPDATE User u SET u.actif = :actif WHERE u.id = :id")
    void updateActifStatus(@Param("id") UUID id, @Param("actif") Boolean actif);
    /**
     * Verifies a user's email.
     * @param id the user UUID
     */
    @Modifying
    @Query("UPDATE User u SET u.emailVerifie = true WHERE u.id = :id")
    void verifyEmail(@Param("id") UUID id);
    /**
     * Finds users assigned as agents for complaints.
     * @return list of active users who are assigned as agents
     */
    @Query("SELECT DISTINCT u FROM User u JOIN Reclamation r ON u.id = r.agentAssigne.id WHERE u.actif = true")
    List<User> findAgentsWithAssignedReclamations();
    /**
     * Finds users ordered by complaint count (paginated).
     * @param pageable pagination parameters
     * @return list of active users ordered by number of complaints
     */
    @Query("SELECT u FROM User u LEFT JOIN Reclamation r ON u.id = r.utilisateur.id WHERE u.actif = true GROUP BY u.id ORDER BY COUNT(r.id) DESC")
    List<User> findUsersOrderByReclamationCount(Pageable pageable);
    
    /**
     * Finds top agents by resolution count.
     * @param limit maximum number of agents to return
     * @return list of top performing agents
     */
    @Query(value = "SELECT u.* FROM users u " +
           "JOIN reclamations r ON u.id = r.agent_assigne_id " +
           "WHERE u.actif = true AND r.statut = 'RESOLUE' " +
           "GROUP BY u.id ORDER BY COUNT(r.id) DESC LIMIT :limit", 
           nativeQuery = true)
    List<User> findTopAgentsByResolutionCount(@Param("limit") int limit);
} 