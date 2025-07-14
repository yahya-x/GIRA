package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.common.ApiResponse;
import com.GIRA.Backend.DTO.request.UserCreateRequest;
import com.GIRA.Backend.DTO.request.UserUpdateRequest;
import com.GIRA.Backend.DTO.response.UserResponse;
import com.GIRA.Backend.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 * Fournit des endpoints pour les opérations CRUD, la pagination, le filtrage et la gestion des rôles.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param request la requête de création d'utilisateur
     * @return la réponse utilisateur créée
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur créé avec succès", response));
    }

    /**
     * Met à jour un utilisateur existant.
     *
     * @param id l'identifiant de l'utilisateur
     * @param request la requête de mise à jour
     * @return la réponse utilisateur mise à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur mis à jour", response));
    }

    /**
     * Supprime un utilisateur (admin uniquement).
     *
     * @param id l'identifiant de l'utilisateur
     * @return confirmation de suppression
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur supprimé", null));
    }

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return la réponse utilisateur
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        UserResponse response = userService.getUserByIdDto(id);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur récupéré", response));
    }

    /**
     * Liste paginée et filtrée des utilisateurs.
     *
     * @param email filtre email (optionnel)
     * @param nom filtre nom (optionnel)
     * @param prenom filtre prénom (optionnel)
     * @param roleId filtre rôle (optionnel)
     * @param actif filtre actif (optionnel)
     * @param emailVerifie filtre email vérifié (optionnel)
     * @param pageable pagination et tri
     * @return page de réponses utilisateur
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "prenom", required = false) String prenom,
            @RequestParam(value = "roleId", required = false) UUID roleId,
            @RequestParam(value = "actif", required = false) Boolean actif,
            @RequestParam(value = "emailVerifie", required = false) Boolean emailVerifie,
            @PageableDefault(size = 10, sort = "dateCreation", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserResponse> page = userService.findWithFiltersDto(email, nom, prenom, roleId, actif, emailVerifie, pageable);
        return ResponseEntity.ok(ApiResponse.success("Liste des utilisateurs récupérée", page));
    }
} 