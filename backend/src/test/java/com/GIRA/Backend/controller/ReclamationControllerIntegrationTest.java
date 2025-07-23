package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.request.ReclamationFilterRequest;
import com.GIRA.Backend.Entities.Role;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.Respository.RoleRepository;
import com.GIRA.Backend.Respository.UserRepository;
import com.GIRA.Backend.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration tests for the ReclamationController.
 * <p>
 * These tests verify the complete request-response cycle for reclamation search operations,
 * including authentication, validation, and business logic integration.
 * </p>
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 2025-07-14
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReclamationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String authToken;
    private User testUser;

    /**
     * Setup method to initialize test data and authentication token.
     * <p>
     * Creates a test user with appropriate role and generates a valid JWT token
     * for authenticated requests.
     * </p>
     */
    @BeforeEach
    void setUp() {
        // Create test role if it doesn't exist - use PASSAGER role to match controller requirements
        Role userRole = roleRepository.findByNom("PASSAGER")
            .orElseGet(() -> {
                Role role = new Role();
                role.setNom("PASSAGER");
                role.setDescription("Passenger role for regular users");
                role.setActif(true);
                return roleRepository.save(role);
            });

        // Create test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setMotDePasse(passwordEncoder.encode("password123"));
        testUser.setNom("Test");
        testUser.setPrenom("User");
        testUser.setRole(userRole);
        testUser.setActif(true);
        testUser.setEmailVerifie(true);
        testUser = userRepository.save(testUser);

        // Generate JWT token
        authToken = "Bearer " + jwtTokenProvider.generateToken(testUser);
    }

    @Test
    @DisplayName("Should return OK status for basic filtering with valid authentication")
    void searchReclamations_BasicFiltering_ReturnsOk() throws Exception {
        // Given
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setStatut("EN_COURS");
        filter.setPage(0);
        filter.setSize(5);

        // When & Then
        mockMvc.perform(post("/api/reclamations/search")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should return Bad Request for invalid input parameters")
    void searchReclamations_InvalidInput_ReturnsBadRequest() throws Exception {
        // Given
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setPage(-1); // Invalid page number
        filter.setSize(0);  // Invalid size

        // When & Then
        mockMvc.perform(post("/api/reclamations/search")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return OK status for date range filtering")
    void searchReclamations_DateRangeFiltering() throws Exception {
        // Given
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setDateDebut(LocalDate.now().minusDays(30));
        filter.setDateFin(LocalDate.now());
        filter.setPage(0);
        filter.setSize(5);

        // When & Then
        mockMvc.perform(post("/api/reclamations/search")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should return OK status for sorting operations")
    void searchReclamations_Sorting() throws Exception {
        // Given
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setSort("titre,asc");
        filter.setPage(0);
        filter.setSize(5);

        // When & Then
        mockMvc.perform(post("/api/reclamations/search")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should return Unauthorized when no authentication token is provided")
    void searchReclamations_NoAuthentication_ReturnsUnauthorized() throws Exception {
        // Given
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setPage(0);
        filter.setSize(5);

        // When & Then
        mockMvc.perform(post("/api/reclamations/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return OK status for empty filter with valid authentication")
    void searchReclamations_EmptyFilter_ReturnsOk() throws Exception {
        // Given
        ReclamationFilterRequest filter = new ReclamationFilterRequest();
        filter.setPage(0);
        filter.setSize(10);

        // When & Then
        mockMvc.perform(post("/api/reclamations/search")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
} 