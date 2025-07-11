package com.GIRA.Backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * <p>
 * Entity representing a comment on a complaint (reclamation).
 * Supports public, internal, and system comment types.
 * </p>
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "commentaires")
public class Commentaire extends BaseEntity {

    /**
     * The complaint (reclamation) to which this comment belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamation_id", nullable = false)
    private Reclamation reclamation;

    /**
     * The user who authored the comment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private User auteur;

    /**
     * Content of the comment (not null).
     */
    @Column(name = "contenu", nullable = false, columnDefinition = "TEXT")
    private String contenu;

    /**
     * Type of the comment (PUBLIC, INTERNE, SYSTEME).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private Type type;

    /**
     * Date and time when the comment was last modified.
     */
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    /**
     * The user who last modified the comment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifie_par")
    private User modifiePar;

    /**
     * Indicates whether the comment has been read (default false).
     */
    @Column(name = "lu", nullable = false)
    private boolean lu = false;

    /**
     * Date and time when the comment was marked as read.
     */
    @Column(name = "date_markage_lu")
    private LocalDateTime dateMarkageLu;

    /**
     * Default constructor.
     */
    public Commentaire() {}

    // ====== Business Logic Method Stubs ======

    /**
     * Adds a new comment to a complaint.
     *
     * @param contenu the content of the comment
     * @param type    the type of the comment
     */
    public void ajouterCommentaire(String contenu, Type type) {
        // TODO: Implement add comment logic
    }

    /**
     * Modifies the content of the comment.
     *
     * @param nouveauContenu the new content
     */
    public void modifierCommentaire(String nouveauContenu) {
        // TODO: Implement modify comment logic
    }

    /**
     * Marks the comment as read.
     */
    public void marquerCommeLu() {
        // TODO: Implement mark as read logic
    }

    /**
     * Deletes the comment.
     */
    public void supprimerCommentaire() {
        // TODO: Implement delete comment logic
    }

    /**
     * Enum representing the type of comment.
     */
    public enum Type {
        PUBLIC, INTERNE, SYSTEME
    }
} 