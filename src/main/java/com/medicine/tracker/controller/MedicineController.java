package com.medicine.tracker.controller;

import com.medicine.tracker.model.dto.request.MedicineRequest;
import com.medicine.tracker.model.dto.response.MedicineResponse;
import com.medicine.tracker.service.MedicineService;
import com.medicine.tracker.service.ImageUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for medicine management endpoints
 * Handles CRUD operations for user medicines with profile-based organization
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicineController {
    
    private final MedicineService medicineService;
    private final ImageUploadService imageUploadService;
    
    /**
     * Create a new medicine for a profile
     * @param profileId The ID of the profile the medicine belongs to
     * @param medicineRequest The request containing medicine details
     * @return Created medicine response
     */
    @PostMapping("/profiles/{profileId}/medicines")
    public ResponseEntity<MedicineResponse> createMedicine(
            @PathVariable UUID profileId,
            @Valid @RequestBody MedicineRequest medicineRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        MedicineResponse response = medicineService.createMedicine(userId, profileId, medicineRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all medicines for a specific profile
     * @param profileId The ID of the profile to retrieve medicines for
     * @return List of medicines belonging to the profile
     */
    @GetMapping("/profiles/{profileId}/medicines")
    public ResponseEntity<List<MedicineResponse>> getAllMedicinesForProfile(@PathVariable UUID profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        List<MedicineResponse> medicines = medicineService.getAllMedicinesForProfile(userId, profileId);
        return ResponseEntity.ok(medicines);
    }
    
    /**
     * Get all medicines for the authenticated user across all profiles
     * @return List of all medicines belonging to the user
     */
    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAllMedicinesForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        List<MedicineResponse> medicines = medicineService.getAllMedicinesForUser(userId);
        return ResponseEntity.ok(medicines);
    }
    
    /**
     * Get a specific medicine by ID
     * @param medicineId The ID of the medicine to retrieve
     * @return The requested medicine response
     */
    @GetMapping("/{medicineId}")
    public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable UUID medicineId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user = 
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        MedicineResponse medicine = medicineService.getMedicineById(medicineId, userId);
        return ResponseEntity.ok(medicine);
    }
    
    /**
     * Update an existing medicine
     * @param profileId The ID of the profile the medicine belongs to
     * @param medicineId The ID of the medicine to update
     * @param medicineRequest The request containing updated medicine details
     * @return Updated medicine response
     */
    @PutMapping("/profiles/{profileId}/medicines/{medicineId}")
    public ResponseEntity<MedicineResponse> updateMedicine(
            @PathVariable UUID profileId,
            @PathVariable UUID medicineId,
            @Valid @RequestBody MedicineRequest medicineRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        MedicineResponse medicine = medicineService.updateMedicine(medicineId, userId, profileId, medicineRequest);
        return ResponseEntity.ok(medicine);
    }
    
    /**
     * Soft delete a medicine by ID (set status to INACTIVE)
     * @param profileId The ID of the profile the medicine belongs to
     * @param medicineId The ID of the medicine to delete
     * @return Empty response with 204 status
     */
    @DeleteMapping("/profiles/{profileId}/medicines/{medicineId}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable UUID profileId, @PathVariable UUID medicineId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        medicineService.deleteMedicine(medicineId, userId, profileId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Take a dose of a medicine (decrement quantity by 1)
     * @param profileId The ID of the profile the medicine belongs to
     * @param medicineId The ID of the medicine to take a dose from
     * @return Updated medicine response after taking the dose
     */
    @PostMapping("/profiles/{profileId}/medicines/{medicineId}/takedose")
    public ResponseEntity<MedicineResponse> takeDose(@PathVariable UUID profileId, @PathVariable UUID medicineId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
            (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();
        
        MedicineResponse medicine = medicineService.takeDose(medicineId, userId, profileId);
        return ResponseEntity.ok(medicine);
    }
    
    /**
     * Upload a medicine image to Cloudinary
     * @param medicineImage The image file to upload
     * @return URL of the uploaded image
     */
    @PostMapping("/medicines/upload-image")
    public ResponseEntity<String> uploadMedicineImage(@RequestParam("medicineImage") MultipartFile medicineImage) {
        try {
            String imageUrl = imageUploadService.uploadImage(medicineImage);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading image: " + e.getMessage());
        }
    }
}