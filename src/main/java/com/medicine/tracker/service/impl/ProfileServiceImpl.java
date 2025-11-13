package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.dto.request.ProfileRequest;
import com.medicine.tracker.model.dto.response.ProfileResponse;
import com.medicine.tracker.model.entity.Profile;
import com.medicine.tracker.repository.ProfileRepository;
import com.medicine.tracker.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of ProfileService for profile management operations
 * Handles CRUD operations for user profiles with proper validation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    
    private final ProfileRepository profileRepository;
    
    /**
     * Create a new profile for a user
     * @param userId The ID of the user creating the profile
     * @param profileRequest The request containing profile details
     * @return Created profile response
     */
    @Override
    public ProfileResponse createProfile(UUID userId, ProfileRequest profileRequest) {
        log.info("Creating profile for user {}", userId);
        
        Profile profile = Profile.builder()
                .userId(userId)
                .name(profileRequest.getName())
                .build();
        
        Profile savedProfile = profileRepository.save(profile);
        log.info("Profile created successfully with ID: {}", savedProfile.getId());
        
        return mapToProfileResponse(savedProfile);
    }
    
    /**
     * Get all profiles for a user
     * @param userId The ID of the user to retrieve profiles for
     * @return List of profiles belonging to the user
     */
    @Override
    public List<ProfileResponse> getAllProfiles(UUID userId) {
        log.info("Retrieving all profiles for user {}", userId);
        
        List<Profile> profiles = profileRepository.findByUserId(userId);
        return profiles.stream()
                .map(this::mapToProfileResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific profile by ID
     * @param profileId The ID of the profile to retrieve
     * @param userId The ID of the user requesting the profile
     * @return The requested profile response
     */
    @Override
    public ProfileResponse getProfileById(UUID profileId, UUID userId) {
        log.info("Retrieving profile {} for user {}", profileId, userId);
        
        Profile profile = profileRepository.findById(profileId)
                .filter(p -> p.getUserId().equals(userId))
                .orElseThrow(() -> {
                    log.warn("Profile not found or does not belong to user: {} for user {}", profileId, userId);
                    return new RuntimeException("Profile not found or does not belong to user");
                });
        
        return mapToProfileResponse(profile);
    }
    
    /**
     * Update an existing profile
     * @param profileId The ID of the profile to update
     * @param userId The ID of the user updating the profile
     * @param profileRequest The request containing updated profile details
     * @return Updated profile response
     */
    @Override
    public ProfileResponse updateProfile(UUID profileId, UUID userId, ProfileRequest profileRequest) {
        log.info("Updating profile {} for user {}", profileId, userId);
        
        Profile profile = profileRepository.findById(profileId)
                .filter(p -> p.getUserId().equals(userId))
                .orElseThrow(() -> {
                    log.warn("Profile not found or does not belong to user: {} for user {}", profileId, userId);
                    return new RuntimeException("Profile not found or does not belong to user");
                });
        
        profile.setName(profileRequest.getName());
        
        Profile updatedProfile = profileRepository.save(profile);
        log.info("Profile updated successfully with ID: {}", updatedProfile.getId());
        
        return mapToProfileResponse(updatedProfile);
    }
    
    /**
     * Delete a profile by ID
     * @param profileId The ID of the profile to delete
     * @param userId The ID of the user deleting the profile
     */
    @Override
    public void deleteProfile(UUID profileId, UUID userId) {
        log.info("Deleting profile {} for user {}", profileId, userId);
        
        Profile profile = profileRepository.findById(profileId)
                .filter(p -> p.getUserId().equals(userId))
                .orElseThrow(() -> {
                    log.warn("Profile not found or does not belong to user: {} for user {}", profileId, userId);
                    return new RuntimeException("Profile not found or does not belong to user");
                });
        
        profileRepository.delete(profile);
        log.info("Profile deleted successfully with ID: {}", profileId);
    }
    
    /**
     * Check if a profile exists for a user
     * @param profileId The ID of the profile to check
     * @param userId The ID of the user to check against
     * @return true if profile exists and belongs to user, false otherwise
     */
    @Override
    public boolean profileExistsForUser(UUID profileId, UUID userId) {
        return profileRepository.existsByUserIdAndId(userId, profileId);
    }
    
    /**
     * Maps a Profile entity to a ProfileResponse DTO
     * @param profile The profile entity to map
     * @return Mapped profile response
     */
    private ProfileResponse mapToProfileResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .name(profile.getName())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}