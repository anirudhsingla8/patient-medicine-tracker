package com.medicine.tracker.service;

import com.medicine.tracker.model.dto.request.MedicineRequest;
import com.medicine.tracker.model.dto.response.MedicineResponse;
import com.medicine.tracker.model.dto.response.MedicineWithProfileResponse;
import com.medicine.tracker.model.entity.Medicine;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for medicine management operations
 * Handles CRUD operations for user medicines with profile-based organization
 */
public interface MedicineService {
    
    /**
     * Create a new medicine for a profile
     * @param userId The ID of the user creating the medicine
     * @param profileId The ID of the profile the medicine belongs to
     * @param medicineRequest The request containing medicine details
     * @return Created medicine response
     */
    MedicineResponse createMedicine(UUID userId, UUID profileId, MedicineRequest medicineRequest);
    
    /**
     * Get all medicines for a specific profile
     * @param userId The ID of the user requesting medicines
     * @param profileId The ID of the profile to retrieve medicines for
     * @return List of medicines belonging to the profile
     */
    List<MedicineResponse> getAllMedicinesForProfile(UUID userId, UUID profileId);
    
    /**
     * Get all medicines for a user across all profiles
     * @param userId The ID of the user to retrieve medicines for
     * @return List of all medicines belonging to the user
     */
    List<MedicineResponse> getAllMedicinesForUser(UUID userId);
    
    /**
     * Get a specific medicine by ID
     * @param medicineId The ID of the medicine to retrieve
     * @param userId The ID of the user requesting the medicine
     * @return The requested medicine response
     */
    MedicineResponse getMedicineById(UUID medicineId, UUID userId);
    
    /**
     * Update an existing medicine
     * @param medicineId The ID of the medicine to update
     * @param userId The ID of the user updating the medicine
     * @param profileId The ID of the profile the medicine belongs to
     * @param medicineRequest The request containing updated medicine details
     * @return Updated medicine response
     */
    MedicineResponse updateMedicine(UUID medicineId, UUID userId, UUID profileId, MedicineRequest medicineRequest);
    
    /**
     * Soft delete a medicine by ID (set status to INACTIVE)
     * @param medicineId The ID of the medicine to delete
     * @param userId The ID of the user deleting the medicine
     * @param profileId The ID of the profile the medicine belongs to
     */
    void deleteMedicine(UUID medicineId, UUID userId, UUID profileId);
    
    /**
     * Take a dose of a medicine (decrement quantity by 1)
     * @param medicineId The ID of the medicine to take a dose from
     * @param userId The ID of the user taking the dose
     * @param profileId The ID of the profile the medicine belongs to
     * @return Updated medicine response after taking the dose
     */
    MedicineResponse takeDose(UUID medicineId, UUID userId, UUID profileId);
    
    /**
     * Check if a medicine exists for a user
     * @param medicineId The ID of the medicine to check
     * @param userId The ID of the user to check against
     * @return true if medicine exists and belongs to user, false otherwise
     */
    boolean medicineExistsForUser(UUID medicineId, UUID userId);
    
    /**
     * Get all medicines for a user with profile information
     * @param userId The ID of the user to retrieve medicines for
     * @return List of medicines with profile information
     */
    List<MedicineWithProfileResponse> getAllMedicinesWithProfileInfo(UUID userId);
}