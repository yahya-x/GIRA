package com.GIRA.Backend.security;

import com.GIRA.Backend.Entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * UserPrincipal class that implements UserDetails for Spring Security.
 * Wraps the User entity to provide security context.
 * 
 * @author Mohamed yahya jabrane
 * @since 1.0
 */
public class UserPrincipal implements UserDetails {

    private final UUID id;
    private final String email;
    private final String nom;
    private final String prenom;
    private final String role;
    
    @JsonIgnore
    private final String motDePasse;
    
    private final boolean actif;
    private final boolean emailVerifie;

    public UserPrincipal(UUID id, String email, String nom, String prenom, String role, 
                        String motDePasse, boolean actif, boolean emailVerifie) {
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.motDePasse = motDePasse;
        this.actif = actif;
        this.emailVerifie = emailVerifie;
    }

    /**
     * Creates a UserPrincipal from a User entity.
     */
    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                user.getRole() != null ? user.getRole().getNom() : "PASSAGER",
                user.getMotDePasse(),
                user.isActif(),
                user.isEmailVerifie()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return actif && emailVerifie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", actif=" + actif +
                ", emailVerifie=" + emailVerifie +
                '}';
    }
} 