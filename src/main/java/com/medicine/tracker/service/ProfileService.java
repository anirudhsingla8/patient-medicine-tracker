package com.medicine.tracker.service;

import com.medicine.tracker.model.dto.request.ProfileRequest;
import com.medicine.tracker.model.dto.response.ProfileResponse;
import com.medicine.tracker.model.entity.Profile;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for profile management operations
 * Handles CRUD operations for user profiles (family members)
 */
public interface ProfileService {
    
    /**
     * Create a new profile for a user
     * @param userId The ID of the user creating the profile
     * @param profileRequest The request containing profile details
     * @return Created profile response
     */
    ProfileResponse createProfile(UUID userId, ProfileRequest profileRequest);
    
    /**
     * Get all profiles for a user
     * @param userId The ID of the user to retrieve profiles for
     * @return List of profiles belonging to the user
     */
    List<ProfileResponse> getAllProfiles(UUID userId);
    
    /**
     * Get a specific profile by ID
     * @param profileId The ID of the profile to retrieve
     * @param userId The ID of the user requesting the profile
     * @return The requested profile response
     */
    ProfileResponse getProfileById(UUID profileId, UUID userId);
    
    /**
     * Update an existing profile
     * @param profileId The ID of the profile to update
     * @param userId The ID of the user updating the profile
     * @param profileRequest The request containing updated profile details
     * @return Updated profile response
     */
    ProfileResponse updateProfile(UUID profileId, UUID userId, ProfileRequest profileRequest);
    
    /**
     * Delete a profile by ID
     * @param profileId The ID of the profile to delete
     * @param userId The ID of the user deleting the profile
     */
    void deleteProfile(UUID profileId, UUID userId);
    
    /**
     * Check if a profile exists for a user
     * @param profileId The ID of the profile to check
     * @param userId The ID of the user to check against
     * @return true if profile exists and belongs to user, false otherwise
     */
    boolean profileExistsForUser(UUID profileId, UUID userId);
}