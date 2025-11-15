package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.dto.request.ProfileRequest;
import com.medicine.tracker.model.dto.response.MedicineResponse;
import com.medicine.tracker.model.dto.response.ProfileResponse;
import com.medicine.tracker.model.dto.response.ScheduleResponse;
import com.medicine.tracker.model.entity.Profile;
import com.medicine.tracker.repository.ProfileRepository;
import com.medicine.tracker.service.MedicineService;
import com.medicine.tracker.service.ProfileService;
import com.medicine.tracker.service.ScheduleService;
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
    private final MedicineService medicineService;
    private final ScheduleService scheduleService;
    
    /**
     * Create a new profile for a user
     * @param userId The ID of the user creating the profile
     * @param profileRequest The request containing profile details
     * @return Created profile response
     */
    @Override
    public ProfileResponse createProfile(UUID userId, ProfileRequest profileRequest) {
        log.info("Creating profile for user {}", userId);
        
        // Check if a profile with the same name already exists for this user
        if (profileRepository.existsByUserIdAndName(userId, profileRequest.getName())) {
            log.warn("Profile with name {} already exists for user {}", profileRequest.getName(), userId);
            throw new RuntimeException("A profile with this name already exists for this user");
        }
        
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
        
        // Check if a profile with the same name already exists for this user (excluding the current profile)
        if (profileRepository.existsByUserIdAndNameAndIdNot(userId, profileRequest.getName(), profileId)) {
            log.warn("Profile with name {} already exists for user {}", profileRequest.getName(), userId);
            throw new RuntimeException("A profile with this name already exists for this user");
        }
        
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
        
        // Delete all schedules associated with this profile
        // Note: We need to get all schedules for the profile first and then delete them
        // Since there's no direct method in ScheduleService to delete by profileId,
        // we'll need to get the schedules and delete them one by one
        try {
            List<ScheduleResponse> schedules = scheduleService.getSchedulesForProfile(userId, profileId);
            for (ScheduleResponse schedule : schedules) {
                scheduleService.deleteSchedule(schedule.getId(), userId);
            }
            log.info("Deleted {} schedules associated with profile {}", schedules.size(), profileId);
        } catch (Exception e) {
            log.error("Error deleting schedules for profile {}: {}", profileId, e.getMessage());
            throw e;
        }
        
        // Delete all medicines associated with this profile
        // Since there's no direct method in MedicineService to delete by profileId,
        // we'll need to get the medicines and delete them one by one
        try {
            List<MedicineResponse> medicines = medicineService.getAllMedicinesForProfile(userId, profileId);
            for (MedicineResponse medicine : medicines) {
                // Use the medicineId and profileId to delete the medicine
                // The deleteMedicine method requires profileId and userId
                medicineService.deleteMedicine(medicine.getId(), userId, profileId);
            }
            log.info("Deleted {} medicines associated with profile {}", medicines.size(), profileId);
        } catch (Exception e) {
            log.error("Error deleting medicines for profile {}: {}", profileId, e.getMessage());
            throw e;
        }
        
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