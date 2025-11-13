package com.medicine.tracker.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for global medicine requests
 * Contains global medicine information for creating or updating global medicines
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalMedicineRequest {
    
    @NotBlank(message = "Medicine name is required")
    private String name;
    
    private String brandName;
    
    private String genericName;
    
    private String dosageForm;
    
    private String strength;
    
    private String manufacturer;
    
    private String description;
    
    private List<String> indications;
    
    private List<String> contraindications;
    
    private List<String> sideEffects;
    
    private List<String> warnings;
    
    private List<String> interactions;
    
    private String storageInstructions;
    
    private String category;
    
    private String atcCode;
    
    private LocalDate fdaApprovalDate;
}