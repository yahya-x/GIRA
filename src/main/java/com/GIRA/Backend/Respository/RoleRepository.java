package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

/**
 * Repository interface for Role entity.
 *
 * @author Mohamed Yahya Jabrane
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {
    // Add custom query methods if needed
    Optional<Role> findByNom(String nom);
} 