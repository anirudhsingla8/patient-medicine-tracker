package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.dto.request.MedicineRequest;
import com.medicine.tracker.model.dto.response.MedicineResponse;
import com.medicine.tracker.model.dto.response.MedicineWithProfileResponse;
import com.medicine.tracker.model.entity.Medicine;
import com.medicine.tracker.model.entity.Profile;
import com.medicine.tracker.repository.MedicineRepository;
import com.medicine.tracker.repository.ProfileRepository;
import com.medicine.tracker.service.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of MedicineService for medicine management operations
 * Handles CRUD operations for user medicines with profile-based organization
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {
    
    private final MedicineRepository medicineRepository;
    private final ProfileRepository profileRepository;
    
    /**
     * Create a new medicine for a profile
     * @param userId The ID of the user creating the medicine
     * @param profileId The ID of the profile the medicine belongs to
     * @param medicineRequest The request containing medicine details
     * @return Created medicine response
     */
    @Override
    public MedicineResponse createMedicine(UUID userId, UUID profileId, MedicineRequest medicineRequest) {
        log.info("Creating medicine for user {} and profile {}", userId, profileId);
        
        // Verify that the profile belongs to the user
        if (!profileRepository.existsByUserIdAndId(userId, profileId)) {
            log.warn("Profile {} does not exist or does not belong to user {}", profileId, userId);
            throw new RuntimeException("Profile does not exist or does not belong to user");
        }
        
        Medicine medicine = Medicine.builder()
                .userId(userId)
                .profileId(profileId)
                .name(medicineRequest.getName())
                .imageUrl(medicineRequest.getImageUrl())
                .dosage(medicineRequest.getDosage())
                .quantity(medicineRequest.getQuantity())
                .expiryDate(medicineRequest.getExpiryDate())
                .category(medicineRequest.getCategory())
                .notes(medicineRequest.getNotes())
                .composition(medicineRequest.getComposition())
                .form(medicineRequest.getForm())
                .status(Medicine.MedicineStatus.ACTIVE)
                .build();
        
        Medicine savedMedicine = medicineRepository.save(medicine);
        log.info("Medicine created successfully with ID: {}", savedMedicine.getId());
        
        return mapToMedicineResponse(savedMedicine);
    }
    
    /**
     * Get all medicines for a specific profile
     * @param userId The ID of the user requesting medicines
     * @param profileId The ID of the profile to retrieve medicines for
     * @return List of medicines belonging to the profile
     */
    @Override
    public List<MedicineResponse> getAllMedicinesForProfile(UUID userId, UUID profileId) {
        log.info("Retrieving all medicines for user {} and profile {}", userId, profileId);
        
        // Verify that the profile belongs to the user
        if (!profileRepository.existsByUserIdAndId(userId, profileId)) {
            log.warn("Profile {} does not exist or does not belong to user {}", profileId, userId);
            throw new RuntimeException("Profile does not exist or does not belong to user");
        }
        
        List<Medicine> medicines = medicineRepository.findByProfileIdAndStatus(
                profileId, 
                Medicine.MedicineStatus.ACTIVE
        );
        
        log.info("Retrieved {} medicines for profile {}", medicines.size(), profileId);
        return medicines.stream()
                .map(this::mapToMedicineResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all medicines for a user across all profiles
     * @param userId The ID of the user to retrieve medicines for
     * @return List of all medicines belonging to the user
     */
    @Override
    public List<MedicineResponse> getAllMedicinesForUser(UUID userId) {
        log.info("Retrieving all medicines for user {}", userId);
        
        List<Medicine> medicines = medicineRepository.findByUserIdAndStatus(
                userId, 
                Medicine.MedicineStatus.ACTIVE
        );
        
        log.info("Retrieved {} medicines for user {}", medicines.size(), userId);
        return medicines.stream()
                .map(this::mapToMedicineResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific medicine by ID
     * @param medicineId The ID of the medicine to retrieve
     * @param userId The ID of the user requesting the medicine
     * @return The requested medicine response
     */
    @Override
    public MedicineResponse getMedicineById(UUID medicineId, UUID userId) {
        log.info("Retrieving medicine {} for user {}", medicineId, userId);
        
        Medicine medicine = medicineRepository.findById(medicineId)
                .filter(m -> m.getUserId().equals(userId) && m.getStatus() == Medicine.MedicineStatus.ACTIVE)
                .orElseThrow(() -> {
                    log.warn("Medicine {} not found or does not belong to user {}", medicineId, userId);
                    return new RuntimeException("Medicine not found or does not belong to user");
                });
        
        log.info("Medicine {} retrieved successfully", medicineId);
        return mapToMedicineResponse(medicine);
    }
    
    /**
     * Update an existing medicine
     * @param medicineId The ID of the medicine to update
     * @param userId The ID of the user updating the medicine
     * @param profileId The ID of the profile the medicine belongs to
     * @param medicineRequest The request containing updated medicine details
     * @return Updated medicine response
     */
    @Override
    public MedicineResponse updateMedicine(UUID medicineId, UUID userId, UUID profileId, MedicineRequest medicineRequest) {
        log.info("Updating medicine {} for user {} and profile {}", medicineId, userId, profileId);
        
        // Verify that the profile belongs to the user
        if (!profileRepository.existsByUserIdAndId(userId, profileId)) {
            log.warn("Profile {} does not exist or does not belong to user {}", profileId, userId);
            throw new RuntimeException("Profile does not exist or does not belong to user");
        }
        
        Medicine medicine = medicineRepository.findById(medicineId)
                .filter(m -> m.getUserId().equals(userId) && m.getProfileId().equals(profileId))
                .orElseThrow(() -> {
                    log.warn("Medicine {} not found or does not belong to user {} or profile {}", medicineId, userId, profileId);
                    return new RuntimeException("Medicine not found or does not belong to user or profile");
                });
        
        // Update medicine properties
        medicine.setName(medicineRequest.getName());
        medicine.setImageUrl(medicineRequest.getImageUrl());
        medicine.setDosage(medicineRequest.getDosage());
        medicine.setQuantity(medicineRequest.getQuantity());
        medicine.setExpiryDate(medicineRequest.getExpiryDate());
        medicine.setCategory(medicineRequest.getCategory());
        medicine.setNotes(medicineRequest.getNotes());
        medicine.setComposition(medicineRequest.getComposition());
        medicine.setForm(medicineRequest.getForm());
        
        Medicine updatedMedicine = medicineRepository.save(medicine);
        log.info("Medicine {} updated successfully", medicineId);
        
        return mapToMedicineResponse(updatedMedicine);
    }
    
    /**
     * Hard delete a medicine by ID
     * @param medicineId The ID of the medicine to delete
     * @param userId The ID of the user deleting the medicine
     * @param profileId The ID of the profile the medicine belongs to
     */
    @Override
    public void deleteMedicine(UUID medicineId, UUID userId, UUID profileId) {
        log.info("Hard deleting medicine {} for user {} and profile {}", medicineId, userId, profileId);
        
        // Verify that the profile belongs to the user
        if (!profileRepository.existsByUserIdAndId(userId, profileId)) {
            log.warn("Profile {} does not exist or does not belong to user {}", profileId, userId);
            throw new RuntimeException("Profile does not exist or does not belong to user");
        }
        
        Medicine medicine = medicineRepository.findById(medicineId)
                .filter(m -> m.getUserId().equals(userId) && m.getProfileId().equals(profileId))
                .orElseThrow(() -> {
                    log.warn("Medicine {} not found or does not belong to user {} or profile {}", medicineId, userId, profileId);
                    return new RuntimeException("Medicine not found or does not belong to user or profile");
                });
        
        // Hard delete the medicine
        medicineRepository.delete(medicine);
        log.info("Medicine {} hard deleted successfully", medicineId);
    }
    
    /**
     * Take a dose of a medicine (decrement quantity by 1)
     * @param medicineId The ID of the medicine to take a dose from
     * @param userId The ID of the user taking the dose
     * @param profileId The ID of the profile the medicine belongs to
     * @return Updated medicine response after taking the dose
     */
    @Override
    public MedicineResponse takeDose(UUID medicineId, UUID userId, UUID profileId) {
        log.info("Taking dose from medicine {} for user {} and profile {}", medicineId, userId, profileId);
        
        // Verify that the profile belongs to the user
        if (!profileRepository.existsByUserIdAndId(userId, profileId)) {
            log.warn("Profile {} does not exist or does not belong to user {}", profileId, userId);
            throw new RuntimeException("Profile does not exist or does not belong to user");
        }
        
        Medicine medicine = medicineRepository.findById(medicineId)
                .filter(m -> m.getUserId().equals(userId) && m.getProfileId().equals(profileId) && m.getStatus() == Medicine.MedicineStatus.ACTIVE)
                .orElseThrow(() -> {
                    log.warn("Medicine {} not found or does not belong to user {} or profile {}", medicineId, userId, profileId);
                    return new RuntimeException("Medicine not found or does not belong to user or profile");
                });
        
        if (medicine.getQuantity() <= 0) {
            log.warn("Cannot take dose: Medicine {} quantity is already 0", medicineId);
            throw new RuntimeException("Medicine quantity is already 0");
        }
        
        // Decrement quantity by 1
        medicine.setQuantity(medicine.getQuantity() - 1);
        
        Medicine updatedMedicine = medicineRepository.save(medicine);
        log.info("Dose taken from medicine {}, new quantity: {}", medicineId, updatedMedicine.getQuantity());
        
        return mapToMedicineResponse(updatedMedicine);
    }
    
    /**
     * Check if a medicine exists for a user
     * @param medicineId The ID of the medicine to check
     * @param userId The ID of the user to check against
     * @return true if medicine exists and belongs to user, false otherwise
     */
    @Override
    public boolean medicineExistsForUser(UUID medicineId, UUID userId) {
        boolean exists = medicineRepository.existsByUserIdAndId(userId, medicineId);
        log.debug("Medicine {} exists for user {}: {}", medicineId, userId, exists);
        return exists;
    }
    
    /**
     * Maps a Medicine entity to a MedicineResponse DTO
     * @param medicine The medicine entity to map
     * @return Mapped medicine response
     */
    private MedicineResponse mapToMedicineResponse(Medicine medicine) {
        return MedicineResponse.builder()
                .id(medicine.getId())
                .userId(medicine.getUserId())
                .profileId(medicine.getProfileId())
                .name(medicine.getName())
                .imageUrl(medicine.getImageUrl())
                .dosage(medicine.getDosage())
                .quantity(medicine.getQuantity())
                .expiryDate(medicine.getExpiryDate())
                .category(medicine.getCategory())
                .notes(medicine.getNotes())
                .composition(medicine.getComposition())
                .form(medicine.getForm())
                .status(medicine.getStatus())
                .createdAt(medicine.getCreatedAt())
                .updatedAt(medicine.getUpdatedAt())
                .build();
    }
    
    /**
     * Get all medicines for a user with profile information
     * @param userId The ID of the user to retrieve medicines for
     * @return List of medicines with profile information
     */
    @Override
    public List<MedicineWithProfileResponse> getAllMedicinesWithProfileInfo(UUID userId) {
        log.info("Retrieving all medicines with profile info for user {}", userId);
        
        List<Medicine> medicines = medicineRepository.findByUserIdAndStatus(
                userId,
                Medicine.MedicineStatus.ACTIVE
        );
        
        List<MedicineWithProfileResponse> medicinesWithProfile = medicines.stream()
                .map(medicine -> {
                    // Get profile information for the medicine
                    Profile profile = profileRepository.findById(medicine.getProfileId()).orElse(null);
                    String profileName = profile != null ? profile.getName() : "Unknown Profile";
                    
                    return MedicineWithProfileResponse.builder()
                            .id(medicine.getId())
                            .userId(medicine.getUserId())
                            .profileId(medicine.getProfileId())
                            .profileName(profileName)
                            .name(medicine.getName())
                            .imageUrl(medicine.getImageUrl())
                            .dosage(medicine.getDosage())
                            .quantity(medicine.getQuantity())
                            .expiryDate(medicine.getExpiryDate())
                            .category(medicine.getCategory())
                            .notes(medicine.getNotes())
                            .composition(medicine.getComposition())
                            .form(medicine.getForm())
                            .status(medicine.getStatus())
                            .createdAt(medicine.getCreatedAt())
                            .updatedAt(medicine.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
        
        log.info("Retrieved {} medicines with profile info for user {}", medicinesWithProfile.size(), userId);
        return medicinesWithProfile;
    }
}