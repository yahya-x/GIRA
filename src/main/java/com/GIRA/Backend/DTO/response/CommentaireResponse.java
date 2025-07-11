package com.GIRA.Backend.DTO.response;

import com.GIRA.Backend.DTO.common.UserSummaryResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for comment responses.
 * Contains comment information with author details and metadata.
 * 
 * @author Mohamed yahya jabrane
 * @version 1.0
 * @since 10/07/2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentaireResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the comment.
     */
    @JsonProperty("id")
    private String id;

    /**
     * ID of the complaint this comment belongs to.
     */
    @JsonProperty("reclamationId")
    private String reclamationId;

    /**
     * Author of the comment.
     */
    @JsonProperty("auteur")
    private UserSummaryResponse auteur;

    /**
     * Content of the comment.
     */
    @JsonProperty("contenu")
    private String contenu;

    /**
     * Type of comment (PUBLIC, INTERNE, SYSTEME).
     */
    @JsonProperty("type")
    private String type;

    /**
     * When the comment was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * When the comment was last modified.
     */
    @JsonProperty("dateModification")
    private LocalDateTime dateModification;

    /**
     * Whether the comment has been read.
     */
    @JsonProperty("lu")
    private Boolean lu;
} 