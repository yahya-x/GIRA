package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.SousCategorieService;
import com.GIRA.Backend.Entities.SousCategorie;
import com.GIRA.Backend.Respository.SousCategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of SousCategorieService.
 * Provides methods for retrieving subcategory entities by ID.
 *
 * @author Your Name
 */
@Service
public class SousCategorieServiceImpl implements SousCategorieService {
    private final SousCategorieRepository sousCategorieRepository;

    @Autowired
    public SousCategorieServiceImpl(SousCategorieRepository sousCategorieRepository) {
        this.sousCategorieRepository = sousCategorieRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SousCategorie> getSousCategorieById(UUID id) {
        return sousCategorieRepository.findById(id);
    }
} 