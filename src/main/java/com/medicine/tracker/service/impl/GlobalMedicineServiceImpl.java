package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.dto.request.GlobalMedicineRequest;
import com.medicine.tracker.model.dto.response.GlobalMedicineResponse;
import com.medicine.tracker.model.entity.GlobalMedicine;
import com.medicine.tracker.repository.GlobalMedicineRepository;
import com.medicine.tracker.service.GlobalMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of GlobalMedicineService for global medicine management operations
 * Handles CRUD operations for global medicine database with search capabilities
 */
@Service
@RequiredArgsConstructor
public class GlobalMedicineServiceImpl implements GlobalMedicineService {
    
    private final GlobalMedicineRepository globalMedicineRepository;
    
    /**
     * Create a new global medicine
     * @param globalMedicineRequest The request containing global medicine details
     * @return Created global medicine response
     */
    @Override
    public GlobalMedicineResponse createGlobalMedicine(GlobalMedicineRequest globalMedicineRequest) {
        GlobalMedicine globalMedicine = GlobalMedicine.builder()
                .name(globalMedicineRequest.getName())
                .brandName(globalMedicineRequest.getBrandName())
                .genericName(globalMedicineRequest.getGenericName())
                .dosageForm(globalMedicineRequest.getDosageForm())
                .strength(globalMedicineRequest.getStrength())
                .manufacturer(globalMedicineRequest.getManufacturer())
                .description(globalMedicineRequest.getDescription())
                .indications(globalMedicineRequest.getIndications())
                .contraindications(globalMedicineRequest.getContraindications())
                .sideEffects(globalMedicineRequest.getSideEffects())
                .warnings(globalMedicineRequest.getWarnings())
                .interactions(globalMedicineRequest.getInteractions())
                .storageInstructions(globalMedicineRequest.getStorageInstructions())
                .category(globalMedicineRequest.getCategory())
                .atcCode(globalMedicineRequest.getAtcCode())
                .fdaApprovalDate(globalMedicineRequest.getFdaApprovalDate())
                .build();
        
        GlobalMedicine savedGlobalMedicine = globalMedicineRepository.save(globalMedicine);
        
        return mapToGlobalMedicineResponse(savedGlobalMedicine);
    }
    
    /**
     * Get all global medicines
     * @return List of all global medicines
     */
    @Override
    public List<GlobalMedicineResponse> getAllGlobalMedicines() {
        List<GlobalMedicine> globalMedicines = globalMedicineRepository.findAll();
        
        return globalMedicines.stream()
                .map(this::mapToGlobalMedicineResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific global medicine by ID
     * @param id The ID of the global medicine to retrieve
     * @return The requested global medicine response
     */
    @Override
    public GlobalMedicineResponse getGlobalMedicineById(UUID id) {
        GlobalMedicine globalMedicine = globalMedicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Global medicine not found with ID: " + id));
        
        return mapToGlobalMedicineResponse(globalMedicine);
    }
    
    /**
     * Search global medicines by name (partial match)
     * @param name The name to search for
     * @return List of global medicines matching the search
     */
    @Override
    public List<GlobalMedicineResponse> searchGlobalMedicinesByName(String name) {
        List<GlobalMedicine> globalMedicines = globalMedicineRepository.findByNameContainingIgnoreCase(name);
        
        return globalMedicines.stream()
                .map(this::mapToGlobalMedicineResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Search global medicines by category
     * @param category The category to search for
     * @return List of global medicines in the category
     */
    @Override
    public List<GlobalMedicineResponse> getGlobalMedicinesByCategory(String category) {
        List<GlobalMedicine> globalMedicines = globalMedicineRepository.findByCategory(category);
        
        return globalMedicines.stream()
                .map(this::mapToGlobalMedicineResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Update an existing global medicine
     * @param id The ID of the global medicine to update
     * @param globalMedicineRequest The request containing updated global medicine details
     * @return Updated global medicine response
     */
    @Override
    public GlobalMedicineResponse updateGlobalMedicine(UUID id, GlobalMedicineRequest globalMedicineRequest) {
        GlobalMedicine globalMedicine = globalMedicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Global medicine not found with ID: " + id));
        
        // Update global medicine properties
        globalMedicine.setName(globalMedicineRequest.getName());
        globalMedicine.setBrandName(globalMedicineRequest.getBrandName());
        globalMedicine.setGenericName(globalMedicineRequest.getGenericName());
        globalMedicine.setDosageForm(globalMedicineRequest.getDosageForm());
        globalMedicine.setStrength(globalMedicineRequest.getStrength());
        globalMedicine.setManufacturer(globalMedicineRequest.getManufacturer());
        globalMedicine.setDescription(globalMedicineRequest.getDescription());
        globalMedicine.setIndications(globalMedicineRequest.getIndications());
        globalMedicine.setContraindications(globalMedicineRequest.getContraindications());
        globalMedicine.setSideEffects(globalMedicineRequest.getSideEffects());
        globalMedicine.setWarnings(globalMedicineRequest.getWarnings());
        globalMedicine.setInteractions(globalMedicineRequest.getInteractions());
        globalMedicine.setStorageInstructions(globalMedicineRequest.getStorageInstructions());
        globalMedicine.setCategory(globalMedicineRequest.getCategory());
        globalMedicine.setAtcCode(globalMedicineRequest.getAtcCode());
        globalMedicine.setFdaApprovalDate(globalMedicineRequest.getFdaApprovalDate());
        
        GlobalMedicine updatedGlobalMedicine = globalMedicineRepository.save(globalMedicine);
        
        return mapToGlobalMedicineResponse(updatedGlobalMedicine);
    }
    
    /**
     * Delete a global medicine by ID
     * @param id The ID of the global medicine to delete
     */
    @Override
    public void deleteGlobalMedicine(UUID id) {
        GlobalMedicine globalMedicine = globalMedicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Global medicine not found with ID: " + id));
        
        globalMedicineRepository.delete(globalMedicine);
    }
    
    /**
     * Maps a GlobalMedicine entity to a GlobalMedicineResponse DTO
     * @param globalMedicine The global medicine entity to map
     * @return Mapped global medicine response
     */
    private GlobalMedicineResponse mapToGlobalMedicineResponse(GlobalMedicine globalMedicine) {
        return GlobalMedicineResponse.builder()
                .id(globalMedicine.getId())
                .name(globalMedicine.getName())
                .brandName(globalMedicine.getBrandName())
                .genericName(globalMedicine.getGenericName())
                .dosageForm(globalMedicine.getDosageForm())
                .strength(globalMedicine.getStrength())
                .manufacturer(globalMedicine.getManufacturer())
                .description(globalMedicine.getDescription())
                .indications(globalMedicine.getIndications())
                .contraindications(globalMedicine.getContraindications())
                .sideEffects(globalMedicine.getSideEffects())
                .warnings(globalMedicine.getWarnings())
                .interactions(globalMedicine.getInteractions())
                .storageInstructions(globalMedicine.getStorageInstructions())
                .category(globalMedicine.getCategory())
                .atcCode(globalMedicine.getAtcCode())
                .fdaApprovalDate(globalMedicine.getFdaApprovalDate())
                .createdAt(globalMedicine.getCreatedAt())
                .updatedAt(globalMedicine.getUpdatedAt())
                .build();
    }
}