package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.dto.request.FcmTokenRequest;
import com.medicine.tracker.model.entity.User;
import com.medicine.tracker.repository.UserRepository;
import com.medicine.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of UserService for user management operations
 * Handles user-specific operations like FCM token management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Update the FCM token for a user
     * @param userId The ID of the user to update
     * @param fcmTokenRequest The request containing the FCM token
     */
    @Override
    public void updateFcmToken(UUID userId, FcmTokenRequest fcmTokenRequest) {
        log.info("Updating FCM token for user {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with ID: " + userId);
                });
        
        user.setFcmToken(fcmTokenRequest.getFcmToken());
        
        userRepository.save(user);
        log.info("FCM token updated successfully for user {}", userId);
    }
    
    /**
     * Get a user by ID
     * @param userId The ID of the user to retrieve
     * @return The requested user
     */
    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with ID: " + userId);
                });
    }
}