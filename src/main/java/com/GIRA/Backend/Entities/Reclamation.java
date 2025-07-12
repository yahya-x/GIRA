package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * Entity representing a complaint (reclamation) submitted by a user.
 * Stores all details, status, and relationships for the complaint lifecycle.
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "reclamations")
public class Reclamation extends BaseEntity {

    /**
     * Unique complaint number (format: AERO-YYYY-NNNNNN).
     */
    @Column(name = "numero", nullable = false, unique = true, length = 30)
    private String numero;

    /**
     * The user who submitted the complaint.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    /**
     * The category of the complaint.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    /**
     * The sub-category of the complaint.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sous_categorie_id")
    private SousCategorie sousCategorie;

    /**
     * Title of the complaint (not null).
     */
    @Column(name = "titre", nullable = false, length = 100)
    private String titre;

    /**
     * Detailed description of the complaint (not null).
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Priority of the complaint.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "priorite", nullable = false, length = 10)
    private Priorite priorite;

    /**
     * Status of the complaint.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private Statut statut;

    /**
     * Date and time when the complaint was last modified.
     */
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    /**
     * Date and time when the complaint was resolved.
     */
    @Column(name = "date_resolution")
    private LocalDateTime dateResolution;

    /**
     * Due date for resolution.
     */
    @Column(name = "date_echeance")
    private LocalDateTime dateEcheance;

    /**
     * Geographical location (WKT or GeoJSON as String).
     */
    @Column(name = "localisation", columnDefinition = "TEXT")
    private String localisation;

    /**
     * Free-text description of the location.
     */
    @Column(name = "lieu_description", length = 255)
    private String lieuDescription;

    /**
     * The agent assigned to handle the complaint.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_assigne_id")
    private User agentAssigne;

    /**
     * Satisfaction rating (1-5).
     */
    @Column(name = "satisfaction")
    private Integer satisfaction;

    /**
     * Satisfaction comment.
     */
    @Column(name = "commentaire_satisfaction", columnDefinition = "TEXT")
    private String commentaireSatisfaction;

    /**
     * Additional metadata, stored as a JSON string.
     */
    @Column(name = "metadonnees", columnDefinition = "TEXT")
    private String metadonnees;

    /**
     * Default constructor.
     */
    public Reclamation() {
        // Set default status for new complaints
        this.statut = Statut.SOUMISE;
    }

    // ====== Business Logic Method Stubs ======

    /**
     * Generates a unique complaint number.
     *
     * @return the generated complaint number
     */
    public String genererNumero() {
        // TODO: Implement number generation logic
        return null;
    }

    /**
     * Changes the status of the complaint.
     *
     * @param nouveauStatut the new status
     * @param commentaire   the reason or comment for the change
     */
    public void changerStatut(Statut nouveauStatut, String commentaire) {
        // TODO: Implement status change logic
    }

    /**
     * Assigns the complaint to an agent.
     *
     * @param agentId the agent's user ID
     */
    public void assigner(UUID agentId) {
        // TODO: Implement assignment logic
    }

    /**
     * Escalates the complaint to a supervisor.
     *
     * @param superviseurId the supervisor's user ID
     * @param raison        the reason for escalation
     */
    public void escalader(UUID superviseurId, String raison) {
        // TODO: Implement escalation logic
    }

    /**
     * Adds a comment to the complaint.
     *
     * @param commentaire the comment to add
     */
    public void ajouterCommentaire(String commentaire) {
        // TODO: Implement add comment logic
    }

    /**
     * Adds a file to the complaint.
     *
     * @param fichier the file to add
     */
    public void ajouterFichier(Object fichier) {
        // TODO: Implement add file logic
    }

    /**
     * Calculates the processing time for the complaint.
     *
     * @return the processing time in hours
     */
    public Integer calculerTempTraitement() {
        // TODO: Implement processing time calculation
        return null;
    }

    /**
     * Sends a notification related to the complaint.
     *
     * @param type        the notification type
     * @param destinataire the recipient
     */
    public void envoyerNotification(String type, Object destinataire) {
        // TODO: Implement notification sending logic
    }

    /**
     * Evaluates the complaint.
     *
     * @param note       the rating
     * @param commentaire the evaluation comment
     */
    public void evaluer(Integer note, String commentaire) {
        // TODO: Implement evaluation logic
    }

    /**
     * Closes the complaint.
     */
    public void fermer() {
        // TODO: Implement close logic
    }

    /**
     * Automatically determines the priority based on business rules.
     * Priority is calculated based on category, keywords in title/description,
     * and other factors like location or user history.
     *
     * @return the calculated priority
     */
    public Priorite determinerPrioriteAutomatique() {
        // Default priority
        Priorite prioriteCalculee = Priorite.NORMALE;
        
        // Check category-based priority rules
        if (categorie != null) {
            String nomCategorie = categorie.getNom().toLowerCase();
            
            // High priority categories
            if (nomCategorie.contains("sécurité") || nomCategorie.contains("securite") ||
                nomCategorie.contains("urgence") || nomCategorie.contains("medical") ||
                nomCategorie.contains("médical")) {
                prioriteCalculee = Priorite.URGENTE;
            }
            // Medium-high priority categories
            else if (nomCategorie.contains("retard") || nomCategorie.contains("annulation") ||
                     nomCategorie.contains("vol") || nomCategorie.contains("bagage")) {
                prioriteCalculee = Priorite.HAUTE;
            }
            // Low priority categories
            else if (nomCategorie.contains("restauration") || nomCategorie.contains("commerce") ||
                     nomCategorie.contains("boutique")) {
                prioriteCalculee = Priorite.BASSE;
            }
        }
        
        // Check for urgent keywords in title and description
        String contenuComplet = (titre + " " + description).toLowerCase();
        if (contenuComplet.contains("urgent") || contenuComplet.contains("immédiat") ||
            contenuComplet.contains("immediat") || contenuComplet.contains("danger") ||
            contenuComplet.contains("accident") || contenuComplet.contains("blessé") ||
            contenuComplet.contains("blesse") || contenuComplet.contains("malade")) {
            prioriteCalculee = Priorite.URGENTE;
        }
        else if (contenuComplet.contains("important") || contenuComplet.contains("critique") ||
                 contenuComplet.contains("problème") || contenuComplet.contains("probleme")) {
            if (prioriteCalculee.ordinal() < Priorite.HAUTE.ordinal()) {
                prioriteCalculee = Priorite.HAUTE;
            }
        }
        
        // Check user history (VIP users might get higher priority)
        // TODO: Implement user priority logic based on user type/history
        
        return prioriteCalculee;
    }

    /**
     * Sets the priority automatically based on business rules.
     * This method should be called when creating a new complaint.
     */
    public void setPrioriteAutomatique() {
        this.priorite = determinerPrioriteAutomatique();
    }

    // ====== Enums ======

    /**
     * Priority levels for complaints.
     */
    public enum Priorite {
        BASSE, NORMALE, HAUTE, URGENTE
    }

    /**
     * Status values for complaints.
     */
    public enum Statut {
        SOUMISE, EN_COURS, EN_ATTENTE_INFO, RESOLUE, FERMEE, ANNULEE
    }

    public Statut getStatut() { return statut; }
    public Priorite getPriorite() { return priorite; }
    public Categorie getCategorie() { return categorie; }
    public SousCategorie getSousCategorie() { return sousCategorie; }
    public User getAgentAssigne() { return agentAssigne; }
    public String getDescription() { return description; }

    public void setStatut(Statut statut) { this.statut = statut; }
    public void setPriorite(Priorite priorite) { this.priorite = priorite; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public void setSousCategorie(SousCategorie sousCategorie) { this.sousCategorie = sousCategorie; }
    public void setAgentAssigne(User agentAssigne) { this.agentAssigne = agentAssigne; }
    public void setDescription(String description) { this.description = description; }
} 