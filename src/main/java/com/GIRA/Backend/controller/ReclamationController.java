package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.request.ReclamationCreateRequest;
import com.GIRA.Backend.DTO.request.ReclamationUpdateRequest;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;
import com.GIRA.Backend.DTO.response.ReclamationResponse;
import com.GIRA.Backend.service.interfaces.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    private final ReclamationService reclamationService;

    @Autowired
    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    // Create a new complaint
    @PostMapping
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ReclamationResponse> createReclamation(@RequestBody ReclamationCreateRequest request) {
        ReclamationResponse response = reclamationService.createReclamation(request);
        return ResponseEntity.ok(response);
    }

    // List complaints (own for PASSAGER, all/assigned for AGENT/ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<List<ReclamationListResponse>> getReclamations() {
        List<ReclamationListResponse> responses = reclamationService.getReclamationsForCurrentUser();
        return ResponseEntity.ok(responses);
    }

    // Get complaint by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ReclamationResponse> getReclamationById(@PathVariable UUID id) {
        ReclamationResponse response = reclamationService.getReclamationByIdForCurrentUser(id);
        return ResponseEntity.ok(response);
    }

    // Update complaint (status, assignment, etc.)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    public ResponseEntity<ReclamationResponse> updateReclamation(@PathVariable UUID id, @RequestBody ReclamationUpdateRequest request) {
        ReclamationResponse response = reclamationService.updateReclamation(id, request);
        return ResponseEntity.ok(response);
    }

    // Delete complaint (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReclamation(@PathVariable UUID id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.noContent().build();
    }
} 