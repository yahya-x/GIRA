package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.common.ApiResponse;
import com.GIRA.Backend.DTO.request.UserCreateRequest;
import com.GIRA.Backend.DTO.request.UserLoginRequest;
import com.GIRA.Backend.DTO.response.AuthResponse;
import com.GIRA.Backend.DTO.response.UserResponse;
import com.GIRA.Backend.Entities.Role;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.Respository.RoleRepository;
import com.GIRA.Backend.security.JwtTokenProvider;
import com.GIRA.Backend.security.UserPrincipal;
import com.GIRA.Backend.service.interfaces.EmailService;
import com.GIRA.Backend.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Authentication controller for user registration, login, and token management.
 * 
 * @author Mohamed yahya jabrane
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private EmailService emailService;

    /**
     * User registration endpoint.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserCreateRequest request) {
        // Check if email already exists
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email already registered"));
        }

        // Get default role (PASSAGER)
        Optional<Role> defaultRole = roleRepository.findByNom("PASSAGER");
        if (defaultRole.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Default role not found"));
        }

        // Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setMotDePasse(request.getPassword());
        user.setNom(request.getUsername());
        user.setRole(defaultRole.get());
        user.setEmailVerifie(false);

        User savedUser = userService.registerUser(user);

        // Generate verification token and send email
        String verificationToken = tokenProvider.generateVerificationToken(savedUser);
        emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);

        // Convert to response
        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .nom(savedUser.getNom())
                .prenom(savedUser.getPrenom())
                .emailVerifie(savedUser.isEmailVerifie())
                .build();

        return ResponseEntity.ok(ApiResponse.success("User registered successfully", userResponse));
    }

    /**
     * User login endpoint.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody UserLoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate tokens
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // Get user details
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());

        // Update last login
        userService.updateDerniereConnexion(user.getId(), java.time.LocalDateTime.now());

        // Build response
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .emailVerifie(user.isEmailVerifie())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L) // 15 minutes
                .user(userResponse)
                .build();

        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    /**
     * Refresh token endpoint.
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestParam String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid refresh token"));
        }

        String email = tokenProvider.getUsernameFromToken(refreshToken);
        User user = userService.getUserById(tokenProvider.getUserIdFromToken(refreshToken));

        if (user == null || !user.getEmail().equals(email)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid user"));
        }

        // Create authentication
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());

        // Generate new tokens
        String newAccessToken = tokenProvider.generateAccessToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

        // Build response
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .emailVerifie(user.isEmailVerifie())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .user(userResponse)
                .build();

        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));
    }

    /**
     * Email verification endpoint.
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        if (!tokenProvider.validateToken(token) || !tokenProvider.isVerificationToken(token)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid verification token"));
        }

        UUID userId = tokenProvider.getUserIdFromToken(token);
        userService.verifyEmail(userId);

        return ResponseEntity.ok(ApiResponse.success("Email verified successfully", "Email verification completed"));
    }

    /**
     * Password reset request endpoint.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        User user = userService.getUserById(userService.findByEmail(email).map(User::getId).orElse(null));
        
        if (user == null) {
            // Don't reveal if email exists or not
            return ResponseEntity.ok(ApiResponse.success("If the email exists, a reset link has been sent", "Check your email"));
        }

        String resetToken = tokenProvider.generatePasswordResetToken(user);
        user.setTokenResetPassword(resetToken);
        userService.updateUser(user.getId(), user);

        emailService.sendNotificationEmail(user.getEmail(), 
                "Réinitialisation de mot de passe GIRA", 
                "Cliquez sur le lien pour réinitialiser votre mot de passe: " + 
                "http://localhost:3000/reset-password?token=" + resetToken);

        return ResponseEntity.ok(ApiResponse.success("If the email exists, a reset link has been sent", "Check your email"));
    }

    /**
     * Get current user information.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userService.getUserById(userPrincipal.getId());

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .emailVerifie(user.isEmailVerifie())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Current user retrieved", userResponse));
    }
} 