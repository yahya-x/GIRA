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
 * Contains information about a comment on a complaint.
 *
 * @author Mohamed Yahya Jabrane
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
     * Content of the comment.
     */
    @JsonProperty("contenu")
    private String contenu;

    /**
     * Type of the comment (PUBLIC, INTERNE, SYSTEME).
     */
    @JsonProperty("type")
    private String type;

    /**
     * Date and time when the comment was created.
     */
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    /**
     * Date and time when the comment was last modified.
     */
    @JsonProperty("dateModification")
    private LocalDateTime dateModification;

    /**
     * Whether the comment has been read.
     */
    @JsonProperty("lu")
    private Boolean lu;

    /**
     * Date and time when the comment was marked as read.
     */
    @JsonProperty("dateMarkageLu")
    private LocalDateTime dateMarkageLu;

    /**
     * ID of the user who wrote the comment.
     */
    @JsonProperty("auteurId")
    private String auteurId;

    /**
     * ID of the user who last modified the comment.
     */
    @JsonProperty("modifiePar")
    private String modifiePar;
} 