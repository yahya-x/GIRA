package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * Entity representing an application user.
 * Stores authentication, profile, and preference information.
 * </p>
 *
 * <p>
 * Each user is assigned a role and can submit or manage complaints (reclamations).
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    /**
     * User's email address (unique, not null).
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * User's hashed password (not null).
     */
    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    /**
     * User's last name.
     */
    @Column(name = "nom", length = 50)
    private String nom;

    /**
     * User's first name.
     */
    @Column(name = "prenom", length = 50)
    private String prenom;

    /**
     * User's phone number.
     */
    @Column(name = "telephone", length = 30)
    private String telephone;

    /**
     * Preferred language (default 'fr').
     */
    @Column(name = "langue", length = 10)
    private String langue = "fr";

    /**
     * Indicates whether the user's email is verified (default false).
     */
    @Column(name = "email_verifie", nullable = false)
    private boolean emailVerifie = false;

    /**
     * Token used for email verification.
     */
    @Column(name = "token_verification_email", length = 255)
    private String tokenVerification;

    /**
     * Token used for password reset.
     */
    @Column(name = "token_reset_password", length = 255)
    private String tokenResetPassword;

    /**
     * User preferences, stored as JSONB in the database.
     * Example: '{"theme":"dark","notifications":true}'
     */
    @Column(name = "preferences", columnDefinition = "jsonb")
    private String preferences;

    /**
     * Date and time of the user's last login.
     */
    @Column(name = "derniere_connexion")
    private LocalDateTime derniereConnexion;

    /**
     * Indicates whether the user is active (enabled).
     */
    @Column(name = "actif", nullable = false)
    private boolean actif = true;

    /**
     * The role assigned to the user (many users can have the same role).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Default constructor.
     */
    public User() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Creates a new user account with the given email and password.
     *
     * @param email      the user's email
     * @param motDePasse the user's password (plain text)
     */
    public void creerCompte(String email, String motDePasse) {
        // TODO: Implement account creation logic
    }

    /**
     * Authenticates the user with the given credentials.
     *
     * @param email      the user's email
     * @param motDePasse the user's password (plain text)
     * @return a JWT or session token if authentication is successful
     */
    public String authentifier(String email, String motDePasse) {
        // TODO: Implement authentication logic
        return null;
    }

    /**
     * Updates the user's profile with the provided data.
     *
     * @param donnees the profile data to update
     */
    public void mettreAJourProfil(Object donnees) {
        // TODO: Implement profile update logic
    }

    /**
     * Changes the user's password.
     *
     * @param ancienMdp  the current password
     * @param nouveauMdp the new password
     */
    public void changerMotDePasse(String ancienMdp, String nouveauMdp) {
        // TODO: Implement password change logic
    }

    /**
     * Verifies the user's email using the provided token.
     *
     * @param token the verification token
     * @return true if verification is successful, false otherwise
     */
    public boolean verifierEmail(String token) {
        // TODO: Implement email verification logic
        return false;
    }

    /**
     * Deactivates the user's account.
     */
    public void desactiverCompte() {
        // TODO: Implement account deactivation logic
    }

    /**
     * Retrieves the list of complaints (reclamations) submitted by the user.
     *
     * @return a list of Reclamation objects
     */
    public Object obtenirReclamations() {
        // TODO: Implement retrieval of user's complaints
        return null;
    }

    // ====== UserDetails Interface Implementation ======

    /**
     * Returns the authorities granted to the user. Typically derived from the user's role and permissions.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Example: return role/permissions as authorities
        // You may want to parse the permissions JSON and convert to SimpleGrantedAuthority
        return Collections.singletonList(new SimpleGrantedAuthority(role != null ? role.getNom() : "USER"));
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the hashed password
     */
    @Override
    public String getPassword() {
        return motDePasse;
    }

    /**
     * Returns the username used to authenticate the user (email in this case).
     *
     * @return the user's email
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired. Always true (not expired) by default.
     *
     * @return true if the account is non-expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. Always true (not locked) by default.
     *
     * @return true if the account is non-locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Always true (not expired) by default.
     *
     * @return true if credentials are non-expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. Uses the 'actif' field.
     *
     * @return true if the user is active
     */
    @Override
    public boolean isEnabled() {
        return isActif();
    }

    // ====== Getters and Setters ======

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public boolean isEmailVerifie() {
        return emailVerifie;
    }

    public void setEmailVerifie(boolean emailVerifie) {
        this.emailVerifie = emailVerifie;
    }

    public String getTokenVerification() {
        return tokenVerification;
    }

    public void setTokenVerification(String tokenVerification) {
        this.tokenVerification = tokenVerification;
    }

    public String getTokenResetPassword() {
        return tokenResetPassword;
    }

    public void setTokenResetPassword(String tokenResetPassword) {
        this.tokenResetPassword = tokenResetPassword;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
 