package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a complaint (reclamation) submitted by a user.
 * Stores all details, status, and relationships for the complaint lifecycle.
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
     * Generates a unique complaint number in the format AERO-YYYY-NNNNNN.
     * This should be called when creating a new complaint.
     *
     * @return the generated complaint number
     */
    public String genererNumero() {
        // Example: AERO-2025-000123
        String year = String.valueOf(LocalDateTime.now().getYear());
        int randomNum = (int) (Math.random() * 1_000_000);
        this.numero = String.format("AERO-%s-%06d", year, randomNum);
        return this.numero;
    }

    /**
     * Changes the status of the complaint and records the modification date.
     * Optionally, a comment can be provided for the status change.
     *
     * @param nouveauStatut the new status
     * @param commentaire   the reason or comment for the change
     */
    public void changerStatut(Statut nouveauStatut, String commentaire) {
        Statut ancienStatut = this.statut;
        this.statut = nouveauStatut;
        this.dateModification = LocalDateTime.now();
        
        // Auto-assign due date for certain status changes
        if (nouveauStatut == Statut.EN_COURS) {
            this.dateEcheance = LocalDateTime.now().plusHours(24); // 24 hours default
        } else if (nouveauStatut == Statut.RESOLUE) {
            this.dateResolution = LocalDateTime.now();
        }
    }

    /**
     * Assigns the complaint to an agent by their user ID.
     *
     * @param agentId the agent's user ID
     */
    public void assigner(UUID agentId) {
        // In a real app, this would be handled by the service layer
        // For now, we'll just update the modification date
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Escalates the complaint to a supervisor.
     *
     * @param superviseurId the supervisor's user ID
     * @param raison        the reason for escalation
     */
    public void escalader(UUID superviseurId, String raison) {
        this.priorite = Priorite.URGENTE;
        this.dateModification = LocalDateTime.now();
        // Update metadata with escalation info
        if (this.metadonnees == null) {
            this.metadonnees = "{}";
        }
        // In real app, add escalation info to metadata JSON
    }

    /**
     * Adds a comment to the complaint.
     *
     * @param commentaire the comment to add
     */
    public void ajouterCommentaire(String commentaire) {
        this.dateModification = LocalDateTime.now();
        // In real app, this would create a Commentaire entity via service
    }

    /**
     * Adds a file to the complaint.
     *
     * @param fichier the file to add (should be a Fichier entity or DTO)
     */
    public void ajouterFichier(Object fichier) {
        this.dateModification = LocalDateTime.now();
        // In real app, this would create a Fichier entity via service
    }

    /**
     * Calculates the processing time for the complaint in hours.
     *
     * @return the processing time in hours, or null if not resolved
     */
    public Integer calculerTempTraitement() {
        if (this.dateCreation != null && this.dateResolution != null) {
            return (int) java.time.Duration.between(this.dateCreation, this.dateResolution).toHours();
        }
        return null;
    }

    /**
     * Sends a notification related to the complaint.
     *
     * @param type        the notification type (EMAIL, PUSH, SMS, etc.)
     * @param destinataire the recipient (should be a User entity)
     */
    public void envoyerNotification(String type, Object destinataire) {
        // In real app, this would create a Notification entity via service
        // The actual sending would be handled by the notification service
    }

    /**
     * Evaluates the complaint by creating an Evaluation entity.
     *
     * @param note        the rating (1-5)
     * @param commentaire the evaluation comment
     */
    public void evaluer(Integer note, String commentaire) {
        this.satisfaction = note;
        this.commentaireSatisfaction = commentaire;
        this.dateModification = LocalDateTime.now();
        // In real app, this would create an Evaluation entity via service
    }

    /**
     * Closes the complaint by setting its status to FERMEE and recording the resolution date.
     */
    public void fermer() {
        this.statut = Statut.FERMEE;
        this.dateResolution = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Automatically determines and sets the priority based on business rules.
     * Example: keywords in title/description, category, user history, etc.
     *
     * @return the calculated priority
     */
    public Priorite determinerPrioriteAutomatique() {
        // Example logic: urgent if title/description contains "urgent" or "danger"
        String text = (titre != null ? titre : "") + " " + (description != null ? description : "");
        if (text.toLowerCase().contains("urgent") || text.toLowerCase().contains("danger")) {
            this.priorite = Priorite.URGENTE;
        } else if (categorie != null && categorie.getNom() != null && categorie.getNom().toLowerCase().contains("retard")) {
            this.priorite = Priorite.HAUTE;
        } else {
            this.priorite = Priorite.NORMALE;
        }
        return this.priorite;
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

    // ====== Getters and Setters ======
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public User getUtilisateur() { return utilisateur; }
    public void setUtilisateur(User utilisateur) { this.utilisateur = utilisateur; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Priorite getPriorite() { return priorite; }
    public void setPriorite(Priorite priorite) { this.priorite = priorite; }
    
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    
    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
    
    public LocalDateTime getDateResolution() { return dateResolution; }
    public void setDateResolution(LocalDateTime dateResolution) { this.dateResolution = dateResolution; }
    
    public LocalDateTime getDateEcheance() { return dateEcheance; }
    public void setDateEcheance(LocalDateTime dateEcheance) { this.dateEcheance = dateEcheance; }
    
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    
    public String getLieuDescription() { return lieuDescription; }
    public void setLieuDescription(String lieuDescription) { this.lieuDescription = lieuDescription; }
    
    public User getAgentAssigne() { return agentAssigne; }
    public void setAgentAssigne(User agentAssigne) { this.agentAssigne = agentAssigne; }
    
    public Integer getSatisfaction() { return satisfaction; }
    public void setSatisfaction(Integer satisfaction) { this.satisfaction = satisfaction; }
    
    public String getCommentaireSatisfaction() { return commentaireSatisfaction; }
    public void setCommentaireSatisfaction(String commentaireSatisfaction) { this.commentaireSatisfaction = commentaireSatisfaction; }
    
    public String getMetadonnees() { return metadonnees; }
    public void setMetadonnees(String metadonnees) { this.metadonnees = metadonnees; }
    
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    
    public SousCategorie getSousCategorie() { return sousCategorie; }
    public void setSousCategorie(SousCategorie sousCategorie) { this.sousCategorie = sousCategorie; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public boolean isActif() { return actif; }
} 