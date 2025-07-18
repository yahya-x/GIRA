package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

/**
 * Repository interface for Role entity.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {
    /**
     * Finds a role by its name.
     * @param nom the role name
     * @return optional containing the role if found
     */
    Optional<Role> findByNom(String nom);
} 