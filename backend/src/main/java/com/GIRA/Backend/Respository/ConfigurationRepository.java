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

/**
 * Repository interface for managing Configuration entities.
 * Provides data access methods for system configuration settings.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {
    /**
     * Finds configuration by key.
     * @param cle the configuration key
     * @return optional containing the configuration if found
     */
    Optional<Configuration> findByCle(String cle);
    /**
     * Finds configurations by type.
     * @param type the configuration type
     * @return list of configurations with the specified type
     */
    List<Configuration> findByType(String type);
    /**
     * Finds configurations by modification date range.
     * @param dateDebut start date
     * @param dateFin end date
     * @return list of configurations modified in the date range
     */
    List<Configuration> findByDateModificationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    /**
     * Counts configurations by type.
     * @param type the configuration type
     * @return number of configurations with the specified type
     */
    long countByType(String type);
} 