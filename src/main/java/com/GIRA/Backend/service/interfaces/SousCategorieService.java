package com.GIRA.Backend.service.interfaces;

import com.GIRA.Backend.Entities.SousCategorie;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing subcategories (SousCategorie).
 * Provides methods for retrieving subcategory entities by ID.
 *
 * @author Your Name
 */
public interface SousCategorieService {
    /**
     * Retrieves a subcategory by its unique identifier.
     *
     * @param id UUID of the subcategory
     * @return Optional containing the subcategory if found, or empty if not found
     */
    Optional<SousCategorie> getSousCategorieById(UUID id);
} 