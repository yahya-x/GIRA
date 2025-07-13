package com.GIRA.Backend.DTO.response;

import com.GIRA.Backend.Entities.Reclamation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concise DTO for displaying a list of complaints.
 * Used for user dashboards and agent queues with minimal data transfer.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReclamationListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the complaint.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Complaint number for easy reference.
     */
    @JsonProperty("numero")
    private String numero;

    /**
     * Title of the complaint.
     */
    @JsonProperty("titre")
    private String titre;

    /**
     * Current status of the complaint.
     */
    @JsonProperty("statut")
    private String statut;

    /**
     * Priority level of the complaint.
     */
    @JsonProperty("priorite")
    private String priorite;

    /**
     * Name of the category this complaint belongs to.
     */
    @JsonProperty("categorieNom")
    private String categorieNom;

    /**
     * Description of the category.
     */
    @JsonProperty("categorieDescription")
    private String categorieDescription;

    /**
     * Name of the subcategory this complaint belongs to.
     */
    @JsonProperty("sousCategorieNom")
    private String sousCategorieNom;

    /**
     * Description of the subcategory.
     */
    @JsonProperty("sousCategorieDescription")
    private String sousCategorieDescription;

    /**
     * When the complaint was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * When the complaint was last modified.
     */
    @JsonProperty("dateModification")
    private LocalDateTime dateModification;

    /**
     * When the complaint is due.
     */
    @JsonProperty("dateEcheance")
    private LocalDateTime dateEcheance;

    /**
     * When the complaint was resolved.
     */
    @JsonProperty("dateResolution")
    private LocalDateTime dateResolution;

    /**
     * Satisfaction level of the complaint.
     */
    @JsonProperty("satisfaction")
    private String satisfaction;

    /**
     * Comment on the satisfaction.
     */
    @JsonProperty("commentaireSatisfaction")
    private String commentaireSatisfaction;

    /**
     * Additional metadata for the complaint.
     */
    @JsonProperty("metadonnees")
    private String metadonnees;

    /**
     * Whether the complaint is active.
     */
    @JsonProperty("actif")
    private Boolean actif;

    /**
     * Full name of the assigned agent.
     */
    @JsonProperty("assignedAgentNomComplet")
    private String assignedAgentNomComplet;

    /**
     * First name of the assigned agent.
     */
    @JsonProperty("assignedAgentPrenom")
    private String assignedAgentPrenom;

    /**
     * Last name of the assigned agent.
     */
    @JsonProperty("assignedAgentNom")
    private String assignedAgentNom;

    /**
     * Email of the assigned agent.
     */
    @JsonProperty("assignedAgentEmail")
    private String assignedAgentEmail;

    /**
     * First name of the user who created the complaint.
     */
    @JsonProperty("utilisateurPrenom")
    private String utilisateurPrenom;

    /**
     * Last name of the user who created the complaint.
     */
    @JsonProperty("utilisateurNom")
    private String utilisateurNom;

    /**
     * Email of the user who created the complaint.
     */
    @JsonProperty("utilisateurEmail")
    private String utilisateurEmail;

    // ====== Getters and Setters ======
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    
    public String getCategorieNom() { return categorieNom; }
    public void setCategorieNom(String categorieNom) { this.categorieNom = categorieNom; }
    
    public String getCategorieDescription() { return categorieDescription; }
    public void setCategorieDescription(String categorieDescription) { this.categorieDescription = categorieDescription; }
    
    public String getSousCategorieNom() { return sousCategorieNom; }
    public void setSousCategorieNom(String sousCategorieNom) { this.sousCategorieNom = sousCategorieNom; }
    
    public String getSousCategorieDescription() { return sousCategorieDescription; }
    public void setSousCategorieDescription(String sousCategorieDescription) { this.sousCategorieDescription = sousCategorieDescription; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
    
    public LocalDateTime getDateEcheance() { return dateEcheance; }
    public void setDateEcheance(LocalDateTime dateEcheance) { this.dateEcheance = dateEcheance; }
    
    public LocalDateTime getDateResolution() { return dateResolution; }
    public void setDateResolution(LocalDateTime dateResolution) { this.dateResolution = dateResolution; }
    
    public String getSatisfaction() { return satisfaction; }
    public void setSatisfaction(String satisfaction) { this.satisfaction = satisfaction; }
    
    public String getCommentaireSatisfaction() { return commentaireSatisfaction; }
    public void setCommentaireSatisfaction(String commentaireSatisfaction) { this.commentaireSatisfaction = commentaireSatisfaction; }
    
    public String getMetadonnees() { return metadonnees; }
    public void setMetadonnees(String metadonnees) { this.metadonnees = metadonnees; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public String getAssignedAgentNomComplet() { return assignedAgentNomComplet; }
    public void setAssignedAgentNomComplet(String assignedAgentNomComplet) { this.assignedAgentNomComplet = assignedAgentNomComplet; }
    
    public String getAssignedAgentPrenom() { return assignedAgentPrenom; }
    public void setAssignedAgentPrenom(String assignedAgentPrenom) { this.assignedAgentPrenom = assignedAgentPrenom; }
    
    public String getAssignedAgentNom() { return assignedAgentNom; }
    public void setAssignedAgentNom(String assignedAgentNom) { this.assignedAgentNom = assignedAgentNom; }
    
    public String getAssignedAgentEmail() { return assignedAgentEmail; }
    public void setAssignedAgentEmail(String assignedAgentEmail) { this.assignedAgentEmail = assignedAgentEmail; }
    
    public String getUtilisateurPrenom() { return utilisateurPrenom; }
    public void setUtilisateurPrenom(String utilisateurPrenom) { this.utilisateurPrenom = utilisateurPrenom; }
    
    public String getUtilisateurNom() { return utilisateurNom; }
    public void setUtilisateurNom(String utilisateurNom) { this.utilisateurNom = utilisateurNom; }
    
    public String getUtilisateurEmail() { return utilisateurEmail; }
    public void setUtilisateurEmail(String utilisateurEmail) { this.utilisateurEmail = utilisateurEmail; }

    /**
     * Creates a ReclamationListResponse from a Reclamation entity.
     * Maps all relevant fields from the entity to the response DTO.
     *
     * @param reclamation the Reclamation entity
     * @return the mapped ReclamationListResponse
     */
    public static ReclamationListResponse fromEntity(Reclamation reclamation) {
        ReclamationListResponse response = new ReclamationListResponse();
        response.setId(reclamation.getId() != null ? reclamation.getId().toString() : null);
        response.setNumero(reclamation.getNumero());
        response.setTitre(reclamation.getTitre());
        response.setStatut(reclamation.getStatut() != null ? reclamation.getStatut().name() : null);
        response.setPriorite(reclamation.getPriorite() != null ? reclamation.getPriorite().name() : null);
        response.setDateCreation(reclamation.getDateCreation());
        response.setDateModification(reclamation.getDateModification());
        response.setDateEcheance(reclamation.getDateEcheance());
        response.setDateResolution(reclamation.getDateResolution());
        response.setSatisfaction(reclamation.getSatisfaction() != null ? reclamation.getSatisfaction().toString() : null);
        response.setCommentaireSatisfaction(reclamation.getCommentaireSatisfaction());
        response.setMetadonnees(reclamation.getMetadonnees());
        response.setActif(reclamation.isActif());
        
        // Map user information
        if (reclamation.getUtilisateur() != null) {
            response.setUtilisateurNom(reclamation.getUtilisateur().getNom());
            response.setUtilisateurPrenom(reclamation.getUtilisateur().getPrenom());
            response.setUtilisateurEmail(reclamation.getUtilisateur().getEmail());
        }
        
        // Map assigned agent information
        if (reclamation.getAgentAssigne() != null) {
            response.setAssignedAgentNom(reclamation.getAgentAssigne().getNom());
            response.setAssignedAgentPrenom(reclamation.getAgentAssigne().getPrenom());
            response.setAssignedAgentEmail(reclamation.getAgentAssigne().getEmail());
        }
        
        // Map category information
        if (reclamation.getCategorie() != null) {
            response.setCategorieNom(reclamation.getCategorie().getNom());
            response.setCategorieDescription(reclamation.getCategorie().getDescription());
        }
        
        // Map subcategory information
        if (reclamation.getSousCategorie() != null) {
            response.setSousCategorieNom(reclamation.getSousCategorie().getNom());
            response.setSousCategorieDescription(reclamation.getSousCategorie().getDescription());
        }
        
        return response;
    }

    /**
     * Creates a list of ReclamationListResponse from a list of Reclamation entities.
     * Maps all entities in the list to their corresponding response DTOs.
     *
     * @param reclamations the list of Reclamation entities
     * @return the list of mapped ReclamationListResponse objects
     */
    public static List<ReclamationListResponse> fromEntityList(List<Reclamation> reclamations) {
        return reclamations.stream()
                .map(ReclamationListResponse::fromEntity)
                .collect(Collectors.toList());
    }
} 