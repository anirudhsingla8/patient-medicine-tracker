package com.medicine.tracker.controller;

import com.medicine.tracker.model.dto.request.GlobalMedicineRequest;
import com.medicine.tracker.model.dto.response.GlobalMedicineResponse;
import com.medicine.tracker.service.GlobalMedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for global medicine management endpoints
 * Handles CRUD operations for global medicine database
 */
@RestController
@RequestMapping("/api/global-medicines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GlobalMedicineController {
    
    private final GlobalMedicineService globalMedicineService;
    
    /**
     * Create a new global medicine
     * @param globalMedicineRequest The request containing global medicine details
     * @return Created global medicine response
     */
    @PostMapping
    public ResponseEntity<GlobalMedicineResponse> createGlobalMedicine(
            @Valid @RequestBody GlobalMedicineRequest globalMedicineRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        GlobalMedicineResponse response = globalMedicineService.createGlobalMedicine(globalMedicineRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all global medicines
     * @return List of all global medicines
     */
    @GetMapping
    public ResponseEntity<List<GlobalMedicineResponse>> getAllGlobalMedicines() {
        List<GlobalMedicineResponse> globalMedicines = globalMedicineService.getAllGlobalMedicines();
        return ResponseEntity.ok(globalMedicines);
    }
    
    /**
     * Get a specific global medicine by ID
     * @param id The ID of the global medicine to retrieve
     * @return The requested global medicine response
     */
    @GetMapping("/{id}")
    public ResponseEntity<GlobalMedicineResponse> getGlobalMedicineById(@PathVariable UUID id) {
        GlobalMedicineResponse globalMedicine = globalMedicineService.getGlobalMedicineById(id);
        return ResponseEntity.ok(globalMedicine);
    }
    
    /**
     * Search global medicines by name (partial match)
     * @param name The name to search for
     * @return List of global medicines matching the search
     */
    @GetMapping("/search")
    public ResponseEntity<List<GlobalMedicineResponse>> searchGlobalMedicinesByName(@RequestParam String name) {
        List<GlobalMedicineResponse> globalMedicines = globalMedicineService.searchGlobalMedicinesByName(name);
        return ResponseEntity.ok(globalMedicines);
    }
    
    /**
     * Search global medicines by category
     * @param category The category to search for
     * @return List of global medicines in the category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<GlobalMedicineResponse>> getGlobalMedicinesByCategory(@PathVariable String category) {
        List<GlobalMedicineResponse> globalMedicines = globalMedicineService.getGlobalMedicinesByCategory(category);
        return ResponseEntity.ok(globalMedicines);
    }
    
    /**
     * Update an existing global medicine
     * @param id The ID of the global medicine to update
     * @param globalMedicineRequest The request containing updated global medicine details
     * @return Updated global medicine response
     */
    @PutMapping("/{id}")
    public ResponseEntity<GlobalMedicineResponse> updateGlobalMedicine(
            @PathVariable UUID id,
            @Valid @RequestBody GlobalMedicineRequest globalMedicineRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        GlobalMedicineResponse globalMedicine = globalMedicineService.updateGlobalMedicine(id, globalMedicineRequest);
        return ResponseEntity.ok(globalMedicine);
    }
    
    /**
     * Delete a global medicine by ID
     * @param id The ID of the global medicine to delete
     * @return Empty response with 204 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGlobalMedicine(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        globalMedicineService.deleteGlobalMedicine(id);
        return ResponseEntity.noContent().build();
    }
}