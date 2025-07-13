package com.GIRA.Backend.DTO.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * Detailed DTO for returning complaint (reclamation) information in API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamationResponse {
    private String id;
    private String numero;
    private String titre;
    private String description;
    private String statut;
    private String priorite;
    private String categorieNom;
    private String sousCategorieNom;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private LocalDateTime dateResolution;
    private LocalDateTime dateEcheance;
    private String localisation;
    private String lieuDescription;
    private String assignedAgentNomComplet;
    private Integer satisfaction;
    private String commentaireSatisfaction;
    private String metadonnees;
    // Add more fields as needed (e.g., files, comments)
    
    // ====== Getters and Setters ======
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    
    public String getCategorieNom() { return categorieNom; }
    public void setCategorieNom(String categorieNom) { this.categorieNom = categorieNom; }
    
    public String getSousCategorieNom() { return sousCategorieNom; }
    public void setSousCategorieNom(String sousCategorieNom) { this.sousCategorieNom = sousCategorieNom; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
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
    
    public String getAssignedAgentNomComplet() { return assignedAgentNomComplet; }
    public void setAssignedAgentNomComplet(String assignedAgentNomComplet) { this.assignedAgentNomComplet = assignedAgentNomComplet; }
    
    public Integer getSatisfaction() { return satisfaction; }
    public void setSatisfaction(Integer satisfaction) { this.satisfaction = satisfaction; }
    
    public String getCommentaireSatisfaction() { return commentaireSatisfaction; }
    public void setCommentaireSatisfaction(String commentaireSatisfaction) { this.commentaireSatisfaction = commentaireSatisfaction; }
    
    public String getMetadonnees() { return metadonnees; }
    public void setMetadonnees(String metadonnees) { this.metadonnees = metadonnees; }
} 