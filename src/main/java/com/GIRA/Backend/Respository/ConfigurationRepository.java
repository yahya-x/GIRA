package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {

    // Find by key
    Optional<Configuration> findByCle(String cle);

    // Find by type
    List<Configuration> findByType(String type);

    // Find by last modified date range
    List<Configuration> findByDateModificationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Count by type
    long countByType(String type);
} 