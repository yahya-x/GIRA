package com.GIRA.Backend.service;

import com.GIRA.Backend.DTO.request.ReclamationFilterRequest;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;
import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.Respository.ReclamationRepository;
import com.GIRA.Backend.service.impl.ReclamationServiceImpl;
import com.GIRA.Backend.service.interfaces.UserService;
import com.GIRA.Backend.service.interfaces.CategorieService;
import com.GIRA.Backend.service.interfaces.SousCategorieService;
import com.GIRA.Backend.Respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ReclamationServiceImpl advanced filtering.
 */
class ReclamationServiceImplTest {
    @Mock
    private ReclamationRepository reclamationRepository;
    @Mock
    private UserService userService;
    @Mock
    private CategorieService categorieService;
    @Mock
    private SousCategorieService sousCategorieService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReclamationServiceImpl reclamationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindWithFiltersDto_StatusAndCategory() {
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setStatut("EN_COURS");
        filter.setCategorieId(UUID.randomUUID());
        filter.setPage(0);
        filter.setSize(10);
        Reclamation rec = new Reclamation();
        rec.setTitre("Test");
        Page<Reclamation> page = new PageImpl<>(List.of(rec));
        when(reclamationRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class))).thenReturn(page);
        Page<ReclamationListResponse> result = reclamationService.findWithFiltersDto(filter);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test", result.getContent().get(0).getTitre());
    }

    @Test
    void testFindWithFiltersDto_Keyword() {
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setMotCle("incident");
        filter.setPage(0);
        filter.setSize(5);
        Reclamation rec = new Reclamation();
        rec.setTitre("Incident bagages");
        Page<Reclamation> page = new PageImpl<>(List.of(rec));
        when(reclamationRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class))).thenReturn(page);
        Page<ReclamationListResponse> result = reclamationService.findWithFiltersDto(filter);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getTitre().contains("Incident"));
    }

    @Test
    void testFindWithFiltersDto_EmptyResult() {
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setPage(0);
        filter.setSize(5);
        when(reclamationRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        Page<ReclamationListResponse> result = reclamationService.findWithFiltersDto(filter);
        assertEquals(0, result.getTotalElements());
    }
} 