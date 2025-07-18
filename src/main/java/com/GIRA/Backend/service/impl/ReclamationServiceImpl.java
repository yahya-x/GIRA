package com.GIRA.Backend.service.impl;

import com.GIRA.Backend.service.interfaces.ReclamationService;
import com.GIRA.Backend.Respository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.GIRA.Backend.Entities.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.GIRA.Backend.service.interfaces.UserService;
import com.GIRA.Backend.service.interfaces.CategorieService;
import com.GIRA.Backend.service.interfaces.SousCategorieService;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.Entities.Categorie;
import com.GIRA.Backend.Entities.SousCategorie;
import com.GIRA.Backend.mapper.ReclamationMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import com.GIRA.Backend.security.UserPrincipal;
import com.GIRA.Backend.DTO.request.ReclamationCreateRequest;
import com.GIRA.Backend.DTO.request.ReclamationUpdateRequest;
import com.GIRA.Backend.DTO.response.ReclamationResponse;
import com.GIRA.Backend.DTO.response.ReclamationListResponse;
import java.util.stream.Collectors;
import com.GIRA.Backend.exception.ResourceNotFoundException;
import com.GIRA.Backend.exception.AccessDeniedException;
import com.GIRA.Backend.exception.BadRequestException;
import com.GIRA.Backend.Respository.UserRepository;
import org.springframework.data.domain.PageImpl;
import com.GIRA.Backend.DTO.request.ReclamationFilterRequest;
import com.GIRA.Backend.Respository.ReclamationSpecification;
import org.springframework.data.domain.Sort;
import com.GIRA.Backend.service.interfaces.HistoriqueService;
import com.GIRA.Backend.Entities.Historique;
import com.GIRA.Backend.service.interfaces.FichierService;
import com.GIRA.Backend.service.interfaces.CommentaireService;
import com.GIRA.Backend.service.interfaces.NotificationService;
import com.GIRA.Backend.Entities.Fichier;
import com.GIRA.Backend.Entities.Notification;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.stream.Stream;

/**
 * Implementation of ReclamationService.
 * @author Mohamed Yahya Jabrane
 */
@Service
public class ReclamationServiceImpl implements ReclamationService {
    private final ReclamationRepository reclamationRepository;
    private final UserService userService;
    private final CategorieService categorieService;
    private final SousCategorieService sousCategorieService;
    private final UserRepository userRepository;
    private final HistoriqueService historiqueService;
    private final FichierService fichierService;
    private final CommentaireService commentaireService;
    private final NotificationService notificationService;

    @Autowired
    public ReclamationServiceImpl(ReclamationRepository reclamationRepository, UserService userService, CategorieService categorieService, SousCategorieService sousCategorieService, UserRepository userRepository, HistoriqueService historiqueService, FichierService fichierService, CommentaireService commentaireService, NotificationService notificationService) {
        this.reclamationRepository = reclamationRepository;
        this.userService = userService;
        this.categorieService = categorieService;
        this.sousCategorieService = sousCategorieService;
        this.userRepository = userRepository;
        this.historiqueService = historiqueService;
        this.fichierService = fichierService;
        this.commentaireService = commentaireService;
        this.notificationService = notificationService;
    }

    /**
     * Creates a new complaint entity in the database.
     *
     * @param reclamation The complaint entity to create
     * @return The created complaint entity
     */
    public Reclamation createReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    /**
     * Retrieves a complaint by its unique ID.
     *
     * @param id The complaint UUID
     * @return Optional containing the complaint entity if found, otherwise empty
     */
    @Override
    public Optional<Reclamation> getReclamationById(UUID id) {
        return reclamationRepository.findById(id);
    }

    /**
     * Retrieves all complaints in the system.
     *
     * @return List of all complaint entities
     */
    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    /**
     * Updates a complaint by its ID.
     *
     * @param id The complaint UUID
     * @param reclamation The updated complaint data
     * @return The updated complaint entity
     */
    // TODO: Implement update logic and add @Override when implemented

    /**
     * Finds complaints by user ID.
     *
     * @param utilisateurId The user UUID
     * @return List of complaint entities submitted by the user
     */
    @Override
    public List<Reclamation> findByUtilisateurId(UUID utilisateurId) {
        return reclamationRepository.findByUtilisateur_Id(utilisateurId);
    }

    /**
     * Finds complaints by assigned agent ID.
     *
     * @param agentId The agent UUID
     * @return List of complaint entities assigned to the agent
     */
    @Override
    public List<Reclamation> findByAgentAssigneId(UUID agentId) {
        return reclamationRepository.findByAgentAssigne_Id(agentId);
    }

    /**
     * Finds complaints by status.
     *
     * @param statut The complaint status
     * @return List of complaint entities with the given status
     */
    @Override
    public List<Reclamation> findByStatut(Reclamation.Statut statut) {
        return reclamationRepository.findByStatut(statut);
    }

    /**
     * Finds complaints by category ID.
     *
     * @param categorieId The category UUID
     * @return List of complaint entities in the category
     */
    @Override
    public List<Reclamation> findByCategorieId(UUID categorieId) {
        return reclamationRepository.findByCategorie_Id(categorieId);
    }

    /**
     * Finds complaints by subcategory ID.
     *
     * @param sousCategorieId The subcategory UUID
     * @return List of complaint entities in the subcategory
     */
    @Override
    public List<Reclamation> findBySousCategorieId(UUID sousCategorieId) {
        return reclamationRepository.findBySousCategorie_Id(sousCategorieId);
    }

    /**
     * Finds a complaint by its unique number.
     *
     * @param numero The unique complaint number
     * @return Optional containing the complaint entity if found, otherwise empty
     */
    @Override
    public Optional<Reclamation> findByNumero(String numero) {
        return reclamationRepository.findByNumero(numero);
    }

    /**
     * Finds complaints by creation date range.
     *
     * @param dateDebut Start date
     * @param dateFin End date
     * @return List of complaint entities created in the date range
     */
    @Override
    public List<Reclamation> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return reclamationRepository.findByDateCreationBetween(dateDebut, dateFin);
    }

    /**
     * Finds complaints by priority.
     *
     * @param priorite The complaint priority
     * @return List of complaint entities with the given priority
     */
    @Override
    public List<Reclamation> findByPriorite(Reclamation.Priorite priorite) {
        return reclamationRepository.findByPriorite(priorite);
    }

    /**
     * Advanced search with filters and pagination, returning DTOs for controller.
     * Maps entities to ReclamationListResponse DTOs.
     *
     * @param statut         The complaint status
     * @param priorite       The complaint priority
     * @param categorieId    The category UUID
     * @param sousCategorieId The subcategory UUID
     * @param agentId        The agent UUID
     * @param utilisateurId  The user UUID
     * @param pageable       Pagination parameters
     * @return Page of complaint list response DTOs
     */
    @Override
    public Page<ReclamationListResponse> findWithFiltersDto(Reclamation.Statut statut, Reclamation.Priorite priorite, UUID categorieId, UUID sousCategorieId, UUID agentId, UUID utilisateurId, Pageable pageable) {
        Page<Reclamation> page = reclamationRepository.findWithFilters(statut, priorite, categorieId, sousCategorieId, agentId, utilisateurId, pageable);
        return page.map(ReclamationMapper::toListResponse);
    }

    /**
     * Advanced search with filters and pagination using a filter DTO.
     * Uses a JPA Specification to apply dynamic business criteria.
     *
     * @param filterRequest DTO containing filter and pagination criteria
     * @return Page of complaint list response DTOs
     */
    @Override
    public Page<ReclamationListResponse> findWithFiltersDto(ReclamationFilterRequest filterRequest) {
        // Build Pageable from DTO
        int page = filterRequest.getPage() != null ? filterRequest.getPage() : 0;
        int size = filterRequest.getSize() != null ? filterRequest.getSize() : 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "dateCreation");
        if (filterRequest.getSort() != null && !filterRequest.getSort().isEmpty()) {
            String[] sortParts = filterRequest.getSort().split(",");
            if (sortParts.length == 2) {
                sort = Sort.by(Sort.Direction.fromString(sortParts[1]), sortParts[0]);
            } else {
                sort = Sort.by(sortParts[0]);
            }
        }
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        // Build Specification
        var spec = ReclamationSpecification.fromFilterRequest(filterRequest);
        // Query
        Page<com.GIRA.Backend.Entities.Reclamation> reclamations = reclamationRepository.findAll(spec, pageable);
        // Map to DTOs
        return reclamations.map(com.GIRA.Backend.mapper.ReclamationMapper::toListResponse);
    }

    /**
     * Counts complaints by status.
     *
     * @param statut The complaint status
     * @return Number of complaints with the given status
     */
    @Override
    public long countByStatut(Reclamation.Statut statut) {
        return reclamationRepository.countByStatut(statut);
    }

    /**
     * Counts complaints by agent.
     *
     * @param agentId The agent UUID
     * @return Number of complaints assigned to the agent
     */
    @Override
    public long countByAgentAssigneId(UUID agentId) {
        return reclamationRepository.countByAgentAssigne_Id(agentId);
    }

    /**
     * Counts complaints by user.
     *
     * @param utilisateurId The user UUID
     * @return Number of complaints submitted by the user
     */
    @Override
    public long countByUtilisateurId(UUID utilisateurId) {
        return reclamationRepository.countByUtilisateur_Id(utilisateurId);
    }

    /**
     * Gets statistics: number of complaints per category.
     * @return List of category name and complaint count pairs
     */
    @Override
    public List<Object[]> countByCategorie() {
        return reclamationRepository.countByCategorie();
    }

    /**
     * Gets statistics: number of complaints per status.
     * @return List of status and complaint count pairs
     */
    @Override
    public List<Object[]> countByStatutGroup() {
        return reclamationRepository.countByStatutGroup();
    }

    // === DTO-based methods for controller ===
    /**
     * SLA Matrix: Maps (category, priority) to SLA duration in hours.
     * This can be externalized to config or DB in the future.
     */
    private static final Map<String, Map<String, Integer>> SLA_MATRIX = new HashMap<>();
    static {
        // Example categories and priorities (adjust as needed)
        // Key: category name (lowercase), Value: map of priority to hours
        Map<String, Integer> baggage = new HashMap<>();
        baggage.put("NORMALE", 24);
        baggage.put("URGENTE", 8);
        SLA_MATRIX.put("baggage", baggage);

        Map<String, Integer> security = new HashMap<>();
        security.put("CRITIQUE", 2);
        security.put("NORMALE", 12);
        SLA_MATRIX.put("security", security);

        Map<String, Integer> facilities = new HashMap<>();
        facilities.put("NORMALE", 48);
        facilities.put("URGENTE", 16);
        SLA_MATRIX.put("facilities", facilities);

        Map<String, Integer> customerService = new HashMap<>();
        customerService.put("NORMALE", 24);
        SLA_MATRIX.put("customer service", customerService);

        Map<String, Integer> lostAndFound = new HashMap<>();
        lostAndFound.put("NORMALE", 72);
        SLA_MATRIX.put("lost & found", lostAndFound);
    }
    private static final int DEFAULT_SLA_HOURS = 48;

    /**
     * Looks up the SLA duration (in hours) for a given category and priority.
     * @param categoryName the name of the category (case-insensitive)
     * @param priority the priority (enum name)
     * @return SLA duration in hours
     */
    private int getSlaDurationHours(String categoryName, String priority) {
        if (categoryName == null || priority == null) return DEFAULT_SLA_HOURS;
        Map<String, Integer> priorityMap = SLA_MATRIX.get(categoryName.trim().toLowerCase());
        if (priorityMap != null && priorityMap.containsKey(priority)) {
            return priorityMap.get(priority);
        }
        return DEFAULT_SLA_HOURS;
    }

    /**
     * Creates a new complaint (reclamation) for the currently authenticated user.
     * Maps the request DTO to an entity, performs category and subcategory lookups,
     * and saves the complaint. Returns a response DTO with the created complaint's details.
     *
     * @param request DTO containing complaint creation data
     * @return Response DTO with created complaint details
     * @throws RuntimeException if the category is not found
     */
    @Override
    public ReclamationResponse createReclamation(ReclamationCreateRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());
        Categorie categorie = categorieService.getCategorieById(request.getCategorieId())
            .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée"));
        SousCategorie sousCategorie = null;
        if (request.getSousCategorieId() != null) {
            sousCategorie = sousCategorieService.getSousCategorieById(request.getSousCategorieId()).orElse(null);
        }
        Reclamation reclamation = ReclamationMapper.fromCreateRequest(request, user, categorie, sousCategorie);

        // --- SLA Logic: Set SLA deadline based on category and priority ---
        String categoryName = categorie.getNom(); // Assumes Categorie has getNom()
        String priority = reclamation.getPriorite() != null ? reclamation.getPriorite().name() : null;
        int slaHours = getSlaDurationHours(categoryName, priority);
        reclamation.setDateEcheance(reclamation.getDateCreation().plus(slaHours, ChronoUnit.HOURS));
        reclamation.setSlaBreached(false);
        // --- End SLA Logic ---

        Reclamation saved = reclamationRepository.save(reclamation);
        return ReclamationMapper.toResponse(saved, fichierService, commentaireService, notificationService);
    }

    /**
     * Retrieves a list of complaints for the currently authenticated user.
     * Admins receive all complaints, agents receive those assigned to them,
     * and users receive only their own complaints. Returns a list of concise response DTOs.
     *
     * @return List of complaint list response DTOs
     */
    @Override
    public java.util.List<ReclamationListResponse> getReclamationsForCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());
        List<Reclamation> reclamations;
        String role = userPrincipal.getRole();
        if ("ADMIN".equals(role)) {
            reclamations = reclamationRepository.findAll();
        } else if ("AGENT".equals(role)) {
            reclamations = reclamationRepository.findByAgentAssigne_Id(user.getId());
        } else {
            reclamations = reclamationRepository.findByUtilisateur_Id(user.getId());
        }
        return reclamations.stream().map(ReclamationMapper::toListResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves a specific complaint by its ID for the currently authenticated user.
     * Admins can access all complaints, agents can access those assigned to them,
     * and users can access only their own complaints. Returns a detailed response DTO.
     *
     * @param id UUID of the complaint
     * @return Response DTO with complaint details
     * @throws RuntimeException if the complaint is not found or access is denied
     */
    @Override
    public ReclamationResponse getReclamationByIdForCurrentUser(java.util.UUID id) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());
        String role = userPrincipal.getRole();
        Reclamation reclamation = reclamationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reclamation non trouvée"));
        if ("ADMIN".equals(role)) {
            return ReclamationMapper.toResponse(reclamation, fichierService, commentaireService, notificationService);
        } else if ("AGENT".equals(role)) {
            if (reclamation.getAgentAssigne() != null && reclamation.getAgentAssigne().getId().equals(user.getId())) {
                return ReclamationMapper.toResponse(reclamation, fichierService, commentaireService, notificationService);
            }
            throw new AccessDeniedException("Accès refusé");
        } else {
            if (reclamation.getUtilisateur() != null && reclamation.getUtilisateur().getId().equals(user.getId())) {
                return ReclamationMapper.toResponse(reclamation, fichierService, commentaireService, notificationService);
            }
            throw new AccessDeniedException("Accès refusé");
        }
    }

    /**
     * Updates an existing complaint by its ID. Only agents and admins are authorized to update complaints.
     * Users can submit satisfaction and comments after resolution. All business rules are enforced.
     *
     * @param id UUID of the complaint to update
     * @param request DTO containing updated complaint data
     * @return Response DTO with updated complaint details
     * @throws RuntimeException if the complaint is not found or access is denied
     */
    @Override
    @Transactional
    public ReclamationResponse updateReclamation(java.util.UUID id, ReclamationUpdateRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());
        String role = userPrincipal.getRole();
        Reclamation reclamation = reclamationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reclamation non trouvée"));

        boolean isAdmin = "ADMIN".equals(role);
        boolean isAgent = "AGENT".equals(role);
        boolean isUser = "PASSAGER".equals(role);
        boolean isOwner = reclamation.getUtilisateur() != null && reclamation.getUtilisateur().getId().equals(user.getId());
        boolean isAssignedAgent = reclamation.getAgentAssigne() != null && reclamation.getAgentAssigne().getId().equals(user.getId());

        // === AGENT/ADMIN: Update core fields, status, assignment ===
        if (isAdmin || isAgent) {
        if (request.getTitre() != null) reclamation.setTitre(request.getTitre());
        if (request.getDescription() != null) reclamation.setDescription(request.getDescription());
        if (request.getPriorite() != null) {
            try {
                Reclamation.Priorite oldPriorite = reclamation.getPriorite();
                reclamation.setPriorite(Reclamation.Priorite.valueOf(request.getPriorite()));
                // Log priority change
                if (oldPriorite != reclamation.getPriorite()) {
                    Historique hist = new Historique();
                    hist.setReclamation(reclamation);
                    hist.setUtilisateur(user);
                    hist.setAction("CHANGEMENT_PRIORITE");
                    hist.setAncienneValeur(oldPriorite != null ? oldPriorite.name() : null);
                    hist.setNouvelleValeur(reclamation.getPriorite().name());
                    hist.setDateAction(LocalDateTime.now());
                    hist.setCommentaire("Changement de priorité par " + role);
                    historiqueService.addHistorique(hist);
                    // --- Priority change notification (to owner) ---
                    User owner = reclamation.getUtilisateur();
                    if (owner != null) {
                        Notification notifUser = new Notification();
                        notifUser.setDestinataire(owner);
                        notifUser.setType(Notification.Type.PUSH);
                        notifUser.setSujet("Priorité de votre réclamation modifiée");
                        notifUser.setContenu("La priorité de votre réclamation '" + reclamation.getTitre() + "' est maintenant : " + reclamation.getPriorite().name());
                        notifUser.setReclamation(reclamation);
                        notificationService.sendNotification(notifUser);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Priorité invalide");
            }
        }
        if (request.getStatut() != null) {
            try {
                Reclamation.Statut oldStatut = reclamation.getStatut();
                reclamation.changerStatut(Reclamation.Statut.valueOf(request.getStatut()), "Mise à jour par " + role);
                // Log status change
                if (oldStatut != reclamation.getStatut()) {
                    Historique hist = new Historique();
                    hist.setReclamation(reclamation);
                    hist.setUtilisateur(user);
                    hist.setAction("CHANGEMENT_STATUT");
                    hist.setAncienneValeur(oldStatut != null ? oldStatut.name() : null);
                    hist.setNouvelleValeur(reclamation.getStatut().name());
                    hist.setDateAction(LocalDateTime.now());
                    hist.setCommentaire("Changement de statut par " + role);
                    historiqueService.addHistorique(hist);
                    // --- Status change notification (to owner) ---
                    User owner = reclamation.getUtilisateur();
                    if (owner != null) {
                        Notification notifUser = new Notification();
                        notifUser.setDestinataire(owner);
                        notifUser.setType(Notification.Type.PUSH);
                        notifUser.setSujet("Mise à jour du statut de votre réclamation");
                        notifUser.setContenu("Le statut de votre réclamation '" + reclamation.getTitre() + "' est passé à : " + reclamation.getStatut().name());
                        notifUser.setReclamation(reclamation);
                        notificationService.sendNotification(notifUser);

                        Notification notifUserEmail = new Notification();
                        notifUserEmail.setDestinataire(owner);
                        notifUserEmail.setType(Notification.Type.EMAIL);
                        notifUserEmail.setSujet("[GIRA] Statut réclamation mis à jour");
                        notifUserEmail.setContenu("Bonjour " + owner.getPrenom() + ",\n\nLe statut de votre réclamation '" + reclamation.getTitre() + "' a été mis à jour : " + reclamation.getStatut().name() + ".\n\nCordialement,\nGIRA");
                        notifUserEmail.setReclamation(reclamation);
                        notificationService.sendNotification(notifUserEmail);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Statut invalide");
            }
        }
        if (request.getAgentAssigneId() != null) {
            User oldAgent = reclamation.getAgentAssigne();
            User agent = userService.getUserById(UUID.fromString(request.getAgentAssigneId()));
            reclamation.setAgentAssigne(agent);
            reclamation.assigner(agent.getId());
            // Log assignment change
            if (oldAgent == null || !oldAgent.getId().equals(agent.getId())) {
                Historique hist = new Historique();
                hist.setReclamation(reclamation);
                hist.setUtilisateur(user);
                hist.setAction("ASSIGNATION_AGENT");
                hist.setAncienneValeur(oldAgent != null ? oldAgent.getNom() + " " + oldAgent.getPrenom() : null);
                hist.setNouvelleValeur(agent.getNom() + " " + agent.getPrenom());
                hist.setDateAction(LocalDateTime.now());
                hist.setCommentaire("Assignation à l'agent par " + role);
                historiqueService.addHistorique(hist);
                // --- Assignment notification ---
                Notification notifAgent = new Notification();
                notifAgent.setDestinataire(agent);
                notifAgent.setType(Notification.Type.PUSH);
                notifAgent.setSujet("Nouvelle réclamation assignée");
                notifAgent.setContenu("Vous avez été assigné à la réclamation : " + reclamation.getTitre());
                notifAgent.setReclamation(reclamation);
                notificationService.sendNotification(notifAgent);

                Notification notifAgentEmail = new Notification();
                notifAgentEmail.setDestinataire(agent);
                notifAgentEmail.setType(Notification.Type.EMAIL);
                notifAgentEmail.setSujet("[GIRA] Nouvelle réclamation assignée");
                notifAgentEmail.setContenu("Bonjour " + agent.getPrenom() + ",\n\nVous avez été assigné à la réclamation : '" + reclamation.getTitre() + "'.\nMerci de la traiter dans les meilleurs délais.\n\nCordialement,\nGIRA");
                notifAgentEmail.setReclamation(reclamation);
                notificationService.sendNotification(notifAgentEmail);
            }
        }
            // === FILES: Add/Remove ===
            if (request.getFichiersToAdd() != null) {
                for (String fileId : request.getFichiersToAdd()) {
                    Fichier fichier = fichierService.getFileById(UUID.fromString(fileId));
                    if (fichier != null) {
                        fichier.setReclamation(reclamation);
                        fichierService.uploadFile(fichier);
                        // Notify file added
                        notifyFileChange(reclamation, fichier.getNomOriginal(), "ajouté");
                    }
                }
            }
            if (request.getFichiersToRemove() != null) {
                for (String fileId : request.getFichiersToRemove()) {
                    Fichier fichier = fichierService.getFileById(UUID.fromString(fileId));
                    if (fichier != null && fichier.getReclamation() != null && fichier.getReclamation().getId().equals(reclamation.getId())) {
                        fichier.setReclamation(null);
                        fichierService.uploadFile(fichier);
                        // Notify file removed
                        notifyFileChange(reclamation, fichier.getNomOriginal(), "supprimé");
                    }
                }
            }
        }

        // === USER: Only satisfaction/comment after resolution ===
        if (isUser && isOwner) {
            if (reclamation.getStatut() == Reclamation.Statut.RESOLUE || reclamation.getStatut() == Reclamation.Statut.FERMEE) {
                if (request.getSatisfaction() != null) {
                    reclamation.evaluer(request.getSatisfaction(), request.getCommentaireSatisfaction());
                    // Log evaluation
                    Historique hist = new Historique();
                    hist.setReclamation(reclamation);
                    hist.setUtilisateur(user);
                    hist.setAction("EVALUATION");
                    hist.setAncienneValeur(null);
                    hist.setNouvelleValeur("Note: " + request.getSatisfaction() + ", Commentaire: " + request.getCommentaireSatisfaction());
                    hist.setDateAction(LocalDateTime.now());
                    hist.setCommentaire("Évaluation de la réclamation par le passager");
                    historiqueService.addHistorique(hist);
                    // Notify agent/admin of evaluation
                    if (reclamation.getAgentAssigne() != null) {
                        Notification notifAgent = new Notification();
                        notifAgent.setDestinataire(reclamation.getAgentAssigne());
                        notifAgent.setType(Notification.Type.PUSH);
                        notifAgent.setSujet("Nouvelle évaluation reçue");
                        notifAgent.setContenu("La réclamation '" + reclamation.getTitre() + "' a été évaluée par le passager.");
                        notifAgent.setReclamation(reclamation);
                        notificationService.sendNotification(notifAgent);
                    }
                }
            } else {
                throw new AccessDeniedException("Vous ne pouvez évaluer qu'une réclamation résolue ou fermée.");
            }
        }

        // Always update modification date
        reclamation.setDateModification(java.time.LocalDateTime.now());
        Reclamation saved = reclamationRepository.save(reclamation);
        return ReclamationMapper.toResponse(saved, fichierService, commentaireService, notificationService);
    }

    /**
     * Escalates a complaint to a supervisor and notifies relevant parties.
     *
     * @param reclamationId the ID of the complaint to escalate
     * @param supervisorId the ID of the supervisor to escalate to
     * @param reason the reason for escalation
     * @param user the user performing the escalation
     */
    @Transactional
    public void escalateReclamation(UUID reclamationId, UUID supervisorId, String reason, User user) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
            .orElseThrow(() -> new ResourceNotFoundException("Reclamation non trouvée"));
        reclamation.escalader(supervisorId, reason);
        reclamationRepository.save(reclamation);

        // Notify owner
        Notification notifOwner = new Notification();
        notifOwner.setDestinataire(reclamation.getUtilisateur());
        notifOwner.setType(Notification.Type.PUSH);
        notifOwner.setSujet("Votre réclamation a été escaladée");
        notifOwner.setContenu("Votre réclamation '" + reclamation.getTitre() + "' a été escaladée pour la raison : " + reason);
        notifOwner.setReclamation(reclamation);
        notificationService.sendNotification(notifOwner);

        // Notify assigned agent (if any)
        if (reclamation.getAgentAssigne() != null) {
            Notification notifAgent = new Notification();
            notifAgent.setDestinataire(reclamation.getAgentAssigne());
            notifAgent.setType(Notification.Type.PUSH);
            notifAgent.setSujet("Réclamation escaladée");
            notifAgent.setContenu("La réclamation '" + reclamation.getTitre() + "' que vous traitez a été escaladée.");
            notifAgent.setReclamation(reclamation);
            notificationService.sendNotification(notifAgent);
        }

        // Notify supervisor (if needed)
        User supervisor = userService.getUserById(supervisorId);
        if (supervisor != null) {
            Notification notifSupervisor = new Notification();
            notifSupervisor.setDestinataire(supervisor);
            notifSupervisor.setType(Notification.Type.PUSH);
            notifSupervisor.setSujet("Nouvelle réclamation escaladée");
            notifSupervisor.setContenu("Une réclamation a été escaladée à votre attention : '" + reclamation.getTitre() + "'. Raison : " + reason);
            notifSupervisor.setReclamation(reclamation);
            notificationService.sendNotification(notifSupervisor);
        }
    }

    /**
     * Notifies relevant parties when a file is added or removed from a complaint.
     *
     * @param reclamation the complaint entity
     * @param fileName the name of the file added/removed
     * @param action "ajouté" or "supprimé"
     */
    private void notifyFileChange(Reclamation reclamation, String fileName, String action) {
        String message = "Un fichier a été " + action + " à la réclamation '" + reclamation.getTitre() + "': " + fileName;

        // Notify owner
        Notification notifOwner = new Notification();
        notifOwner.setDestinataire(reclamation.getUtilisateur());
        notifOwner.setType(Notification.Type.PUSH);
        notifOwner.setSujet("Fichier " + action + " à votre réclamation");
        notifOwner.setContenu(message);
        notifOwner.setReclamation(reclamation);
        notificationService.sendNotification(notifOwner);

        // Notify assigned agent (if any)
        if (reclamation.getAgentAssigne() != null) {
            Notification notifAgent = new Notification();
            notifAgent.setDestinataire(reclamation.getAgentAssigne());
            notifAgent.setType(Notification.Type.PUSH);
            notifAgent.setSujet("Fichier " + action + " à une réclamation assignée");
            notifAgent.setContenu(message);
            notifAgent.setReclamation(reclamation);
            notificationService.sendNotification(notifAgent);
        }
    }

    /**
     * Deletes a complaint by its ID. Only admins are authorized to delete complaints.
     *
     * @param id UUID of the complaint to delete
     * @throws RuntimeException if the user is not an admin
     */
    @Override
    public void deleteReclamation(java.util.UUID id) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userPrincipal.getRole();
        if (!"ADMIN".equals(role)) {
            throw new AccessDeniedException("Accès refusé");
        }
        if (!reclamationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reclamation non trouvée");
        }
        reclamationRepository.deleteById(id);
    }

    @Override
    public Page<Reclamation> findWithFilters(Reclamation.Statut statut, Reclamation.Priorite priorite, UUID categorieId, UUID sousCategorieId, UUID agentId, UUID utilisateurId, Pageable pageable) {
        return reclamationRepository.findWithFilters(statut, priorite, categorieId, sousCategorieId, agentId, utilisateurId, pageable);
    }

    /**
     * Scheduled SLA enforcement: checks for overdue complaints and triggers notifications.
     * Runs every hour by default (configurable via 'gira.sla.check.cron').
     * Notifies assigned agent and supervisor if available.
     */
    @Scheduled(cron = "${gira.sla.check.cron:0 0 * * * *}") // Default: every hour
    public void enforceSlaBreaches() {
        List<Reclamation> overdue = reclamationRepository.findAll().stream()
            .filter(r -> !r.isSlaBreached())
            .filter(r -> r.getDateEcheance() != null && r.getDateEcheance().isBefore(LocalDateTime.now()))
            .filter(r -> r.getStatut() != Reclamation.Statut.RESOLUE && r.getStatut() != Reclamation.Statut.FERMEE)
            .collect(Collectors.toList());
        // Find all supervisors
        List<User> supervisors = userRepository.findAll().stream()
            .filter(u -> u.getRole() != null && "SUPERVISEUR".equalsIgnoreCase(u.getRole().getNom()))
            .collect(Collectors.toList());
        for (Reclamation r : overdue) {
            r.setSlaBreached(true);
            User agent = r.getAgentAssigne();
            if (!supervisors.isEmpty()) {
                User assignedSupervisor = supervisors.get(0); // Assign to first supervisor
                r.setAgentAssigne(assignedSupervisor);
                r.setPriorite(Reclamation.Priorite.URGENTE);
                // Log escalation in history
                Historique hist = new Historique();
                hist.setReclamation(r);
                hist.setUtilisateur(assignedSupervisor);
                hist.setAction("ESCALADE_SLA");
                hist.setAncienneValeur(agent != null ? agent.getNom() + " " + agent.getPrenom() : null);
                hist.setNouvelleValeur(assignedSupervisor.getNom() + " " + assignedSupervisor.getPrenom());
                hist.setDateAction(LocalDateTime.now());
                hist.setCommentaire("Escalade automatique suite à un dépassement de SLA");
                historiqueService.addHistorique(hist);
                // Notify all supervisors (PUSH + EMAIL)
                for (User supervisor : supervisors) {
                    Notification notifSup = new Notification();
                    notifSup.setDestinataire(supervisor);
                    notifSup.setType(Notification.Type.PUSH);
                    notifSup.setSujet("SLA Breach: Complaint auto-escalated");
                    notifSup.setContenu("The complaint '" + r.getTitre() + "' has been auto-escalated to supervisor due to SLA breach.");
                    notifSup.setReclamation(r);
                    notificationService.sendNotification(notifSup);
                    Notification notifSupEmail = new Notification();
                    notifSupEmail.setDestinataire(supervisor);
                    notifSupEmail.setType(Notification.Type.EMAIL);
                    notifSupEmail.setSujet("[GIRA] Complaint auto-escalated to supervisor");
                    notifSupEmail.setContenu("Bonjour " + supervisor.getPrenom() + ",\n\nLa réclamation '" + r.getTitre() + "' a été automatiquement escaladée à un superviseur suite à un dépassement de SLA.\nMerci de la traiter en urgence.\n\nCordialement,\nGIRA");
                    notifSupEmail.setReclamation(r);
                    notificationService.sendNotification(notifSupEmail);
                }
                // Notify previous agent (EMAIL)
                if (agent != null) {
                    Notification notifAgentEmail = new Notification();
                    notifAgentEmail.setDestinataire(agent);
                    notifAgentEmail.setType(Notification.Type.EMAIL);
                    notifAgentEmail.setSujet("[GIRA] Complaint auto-escalated to supervisor");
                    notifAgentEmail.setContenu("Bonjour " + agent.getPrenom() + ",\n\nLa réclamation '" + r.getTitre() + "' a été automatiquement escaladée à un superviseur suite à un dépassement de SLA.\n\nCordialement,\nGIRA");
                    notifAgentEmail.setReclamation(r);
                    notificationService.sendNotification(notifAgentEmail);
                }
            } else if (agent != null) {
                // No supervisor: just notify agent (PUSH + EMAIL)
                Notification notif = new Notification();
                notif.setDestinataire(agent);
                notif.setType(Notification.Type.PUSH);
                notif.setSujet("SLA Breach: Complaint overdue");
                notif.setContenu("The complaint '" + r.getTitre() + "' has breached its SLA deadline and requires urgent attention.");
                notif.setReclamation(r);
                notificationService.sendNotification(notif);
                Notification notifEmail = new Notification();
                notifEmail.setDestinataire(agent);
                notifEmail.setType(Notification.Type.EMAIL);
                notifEmail.setSujet("[GIRA] Complaint overdue (SLA breach)");
                notifEmail.setContenu("Bonjour " + agent.getPrenom() + ",\n\nLa réclamation '" + r.getTitre() + "' a dépassé son délai SLA et nécessite une attention urgente.\n\nCordialement,\nGIRA");
                notifEmail.setReclamation(r);
                notificationService.sendNotification(notifEmail);
            }
            reclamationRepository.save(r);
        }
    }
} 