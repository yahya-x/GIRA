package com.GIRA.Backend.Respository;

import com.GIRA.Backend.Entities.Reclamation;
import com.GIRA.Backend.DTO.request.ReclamationFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Specification utilitaire pour le filtrage dynamique des réclamations selon les critères métier.
 * Permet de construire dynamiquement une requête JPA à partir d'un DTO de filtre.
 */
public class ReclamationSpecification {
    public static Specification<Reclamation> fromFilterRequest(ReclamationFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatut() != null && !filter.getStatut().isEmpty()) {
                predicates.add(cb.equal(root.get("statut"), filter.getStatut()));
            }
            if (filter.getPriorite() != null && !filter.getPriorite().isEmpty()) {
                predicates.add(cb.equal(root.get("priorite"), filter.getPriorite()));
            }
            if (filter.getCategorieId() != null) {
                predicates.add(cb.equal(root.get("categorie").get("id"), filter.getCategorieId()));
            }
            if (filter.getSousCategorieId() != null) {
                predicates.add(cb.equal(root.get("sousCategorie").get("id"), filter.getSousCategorieId()));
            }
            // Keyword search (titre, description)
            if (filter.getMotCle() != null && !filter.getMotCle().isEmpty()) {
                String pattern = "%" + filter.getMotCle().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("titre")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
                ));
            }
            // Date range (dateDebut/dateFin)
            if (filter.getDateDebut() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateCreation"), filter.getDateDebut().atStartOfDay()));
            }
            if (filter.getDateFin() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateCreation"), filter.getDateFin().atTime(LocalTime.MAX)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
} 