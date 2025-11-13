package com.medicine.tracker.controller;

import com.medicine.tracker.model.dto.request.FcmTokenRequest;
import com.medicine.tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for user management endpoints
 * Handles user-specific operations like FCM token management
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Update the FCM token for the authenticated user
     * @param fcmTokenRequest The request containing the FCM token
     * @return Empty response with 200 status
     */
    @PostMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(@Valid @RequestBody FcmTokenRequest fcmTokenRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        userService.updateFcmToken(userId, fcmTokenRequest);
        return ResponseEntity.ok().build();
    }
}