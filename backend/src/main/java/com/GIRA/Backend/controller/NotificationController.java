package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.response.NotificationResponse;
import com.GIRA.Backend.Entities.Notification;
import com.GIRA.Backend.Entities.User;
import com.GIRA.Backend.service.interfaces.NotificationService;
import com.GIRA.Backend.service.interfaces.UserService;
import com.GIRA.Backend.DTO.common.ApiResponse;
import com.GIRA.Backend.security.UserPrincipal;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for managing notifications.
 * Provides endpoints for listing, marking as read, and deleting notifications for the current user.
 *
 * @author Mohamed Yahya Jabrane
 * @since 1.0
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * Lists notifications for the current user (paginated).
     *
     * @param pageable pagination parameters
     * @return page of notification responses
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> listNotificationsForCurrentUser(
            @PageableDefault(size = 10, sort = "dateCreation", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
    ) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());
        Page<Notification> notifications = notificationService.findByDestinataire(user, pageable);
        Page<NotificationResponse> responsePage = notifications.map(n -> {
            NotificationResponse resp = new NotificationResponse();
            resp.setId(n.getId());
            resp.setDestinataireId(n.getDestinataire() != null ? n.getDestinataire().getId() : null);
            resp.setType(n.getType() != null ? n.getType().name() : null);
            resp.setSujet(n.getSujet());
            resp.setContenu(n.getContenu());
            resp.setDateCreation(n.getDateCreation());
            resp.setDateEnvoi(n.getDateEnvoi());
            resp.setDateLecture(n.getDateLecture());
            resp.setStatut(n.getStatut() != null ? n.getStatut().name() : null);
            resp.setReclamationId(n.getReclamation() != null ? n.getReclamation().getId() : null);
            resp.setMetadonnees(n.getMetadonnees());
            return resp;
        });
        return ResponseEntity.ok(ApiResponse.success("Liste des notifications récupérée", responsePage));
    }

    /**
     * Marks a notification as read (current user only).
     *
     * @param id the notification UUID
     * @return success message
     */
    @PostMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable UUID id) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Notification notification = notificationService.getNotificationById(id);
        if (notification == null || !notification.getDestinataire().getId().equals(userPrincipal.getId())) {
            return ResponseEntity.status(403).body(ApiResponse.error("Vous n'êtes pas autorisé à marquer cette notification comme lue."));
        }
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marquée comme lue", null));
    }

    /**
     * Deletes a notification (current user only).
     *
     * @param id the notification UUID
     * @return success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PASSAGER', 'AGENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable UUID id) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Notification notification = notificationService.getNotificationById(id);
        if (notification == null || !notification.getDestinataire().getId().equals(userPrincipal.getId())) {
            return ResponseEntity.status(403).body(ApiResponse.error("Vous n'êtes pas autorisé à supprimer cette notification."));
        }
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.success("Notification supprimée", null));
    }
} 