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
     * Date and time when the comment was created.
     */
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    /**
     * Indicates whether the comment is active (matches 'actif' in DB).
     */
    @Column(name = "actif", nullable = false)
    private boolean actif = true;

    /**
     * Type of the comment (PUBLIC, INTERNE, SYSTEME) as string in DB.
     */
    @Column(name = "type", length = 20)
    private String type;

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
     * Creates a new comment with proper metadata and associations.
     *
     * @param contenu the content of the comment
     * @param type    the type of the comment
     */
    public void ajouterCommentaire(String contenu, Type type) {
        this.contenu = contenu;
        this.type = type.name();
        this.dateCreation = LocalDateTime.now();
        this.actif = true;
        this.lu = false;
    }

    /**
     * Modifies the content of the comment.
     * Updates the comment content and modification timestamp.
     *
     * @param nouveauContenu the new content
     */
    public void modifierCommentaire(String nouveauContenu) {
        this.contenu = nouveauContenu;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Marks the comment as read.
     * Updates the read status and timestamp.
     */
    public void marquerCommeLu() {
        this.lu = true;
        this.dateMarkageLu = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Deletes the comment.
     * Marks the comment as inactive rather than physically deleting it.
     */
    public void supprimerCommentaire() {
        this.actif = false;
        this.dateModification = LocalDateTime.now();
    }

    public boolean getLu() { return this.lu; }

    /**
     * Enum representing the type of comment.
     */
    public enum Type {
        PUBLIC, INTERNE, SYSTEME
    }
} 