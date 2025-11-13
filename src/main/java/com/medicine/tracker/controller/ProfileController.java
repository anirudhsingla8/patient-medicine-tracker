package com.medicine.tracker.controller;

import com.medicine.tracker.model.dto.request.ProfileRequest;
import com.medicine.tracker.model.dto.response.ProfileResponse;
import com.medicine.tracker.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for profile management endpoints
 * Handles CRUD operations for user profiles (family members)
 */
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {
    
    private final ProfileService profileService;
    
    /**
     * Create a new profile for the authenticated user
     * @param profileRequest The request containing profile details
     * @return Created profile response
     */
    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody ProfileRequest profileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        ProfileResponse response = profileService.createProfile(userId, profileRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all profiles for the authenticated user
     * @return List of profiles belonging to the user
     */
    @GetMapping
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        List<ProfileResponse> profiles = profileService.getAllProfiles(userId);
        return ResponseEntity.ok(profiles);
    }
    
    /**
     * Get a specific profile by ID
     * @param profileId The ID of the profile to retrieve
     * @return The requested profile response
     */
    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable UUID profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        ProfileResponse profile = profileService.getProfileById(profileId, userId);
        return ResponseEntity.ok(profile);
    }
    
    /**
     * Update an existing profile
     * @param profileId The ID of the profile to update
     * @param profileRequest The request containing updated profile details
     * @return Updated profile response
     */
    @PutMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable UUID profileId, 
            @Valid @RequestBody ProfileRequest profileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        ProfileResponse profile = profileService.updateProfile(profileId, userId, profileRequest);
        return ResponseEntity.ok(profile);
    }
    
    /**
     * Delete a profile by ID
     * @param profileId The ID of the profile to delete
     * @return Empty response with 204 status
     */
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        profileService.deleteProfile(profileId, userId);
        return ResponseEntity.noContent().build();
    }
}