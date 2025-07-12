package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.CommentaireService;
import com.GIRA.Backend.Respository.CommentaireRepository;
import com.GIRA.Backend.Entities.Commentaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of CommentaireService.
 * Provides add, retrieval, advanced queries, and statistics for comments.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class CommentaireServiceImpl implements CommentaireService {
    private final CommentaireRepository commentaireRepository;

    @Autowired
    public CommentaireServiceImpl(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    @Override
    public Commentaire addComment(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    @Override
    public Commentaire getCommentById(UUID id) {
        return commentaireRepository.findById(id).orElse(null);
    }

    @Override
    public List<Commentaire> getCommentsByReclamationId(UUID reclamationId) {
        return commentaireRepository.findByReclamation_Id(reclamationId);
    }

    @Override
    public List<Commentaire> getCommentsByAuteurId(UUID auteurId) {
        return commentaireRepository.findByAuteur_Id(auteurId);
    }

    @Override
    public List<Commentaire> getCommentsByType(String type) {
        return commentaireRepository.findByType(type);
    }

    @Override
    public List<Commentaire> getCommentsByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return commentaireRepository.findByDateCreationBetween(dateDebut, dateFin);
    }

    @Override
    public long countByReclamationId(UUID reclamationId) {
        return commentaireRepository.countByReclamation_Id(reclamationId);
    }

    @Override
    public List<Commentaire> findByContenuContainingIgnoreCase(String contenu) {
        return commentaireRepository.findByContenuContainingIgnoreCase(contenu);
    }

    @Override
    public void deleteComment(UUID id) {
        commentaireRepository.deleteById(id);
    }
} 