package com.GIRA.Backend.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for creating comments on complaints.
 * Supports different types of comments (public, internal, system).
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentaireCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID of the complaint this comment belongs to.
     */
    @NotNull(message = "L'ID de la réclamation est obligatoire")
    @JsonProperty("reclamationId")
    private String reclamationId;

    /**
     * The content of the comment.
     * Must be between 1 and 2000 characters.
     */
    @NotBlank(message = "Le contenu du commentaire est obligatoire")
    @Size(min = 1, max = 2000, message = "Le contenu doit contenir entre 1 et 2000 caractères")
    @JsonProperty("contenu")
    private String contenu;

    /**
     * Type of comment (PUBLIC, INTERNE, SYSTEME).
     */
    @NotNull(message = "Le type de commentaire est obligatoire")
    @JsonProperty("type")
    private String type;
} 