package com.medicine.tracker.service;

import com.medicine.tracker.model.dto.request.GlobalMedicineRequest;
import com.medicine.tracker.model.dto.response.GlobalMedicineResponse;
import com.medicine.tracker.model.entity.GlobalMedicine;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for global medicine management operations
 * Handles CRUD operations for global medicine database
 */
public interface GlobalMedicineService {
    
    /**
     * Create a new global medicine
     * @param globalMedicineRequest The request containing global medicine details
     * @return Created global medicine response
     */
    GlobalMedicineResponse createGlobalMedicine(GlobalMedicineRequest globalMedicineRequest);
    
    /**
     * Get all global medicines
     * @return List of all global medicines
     */
    List<GlobalMedicineResponse> getAllGlobalMedicines();
    
    /**
     * Get a specific global medicine by ID
     * @param id The ID of the global medicine to retrieve
     * @return The requested global medicine response
     */
    GlobalMedicineResponse getGlobalMedicineById(UUID id);
    
    /**
     * Search global medicines by name (partial match)
     * @param name The name to search for
     * @return List of global medicines matching the search
     */
    List<GlobalMedicineResponse> searchGlobalMedicinesByName(String name);
    
    /**
     * Search global medicines by category
     * @param category The category to search for
     * @return List of global medicines in the category
     */
    List<GlobalMedicineResponse> getGlobalMedicinesByCategory(String category);
    
    /**
     * Update an existing global medicine
     * @param id The ID of the global medicine to update
     * @param globalMedicineRequest The request containing updated global medicine details
     * @return Updated global medicine response
     */
    GlobalMedicineResponse updateGlobalMedicine(UUID id, GlobalMedicineRequest globalMedicineRequest);
    
    /**
     * Delete a global medicine by ID
     * @param id The ID of the global medicine to delete
     */
    void deleteGlobalMedicine(UUID id);
}