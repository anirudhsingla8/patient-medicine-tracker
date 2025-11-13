package com.medicine.tracker.service;

import com.medicine.tracker.model.dto.request.FcmTokenRequest;
import com.medicine.tracker.model.entity.User;

import java.util.UUID;

/**
 * Service interface for user management operations
 * Handles user-specific operations like FCM token management
 */
public interface UserService {
    
    /**
     * Update the FCM token for a user
     * @param userId The ID of the user to update
     * @param fcmTokenRequest The request containing the FCM token
     */
    void updateFcmToken(UUID userId, FcmTokenRequest fcmTokenRequest);
    
    /**
     * Get a user by ID
     * @param userId The ID of the user to retrieve
     * @return The requested user
     */
    User getUserById(UUID userId);
}