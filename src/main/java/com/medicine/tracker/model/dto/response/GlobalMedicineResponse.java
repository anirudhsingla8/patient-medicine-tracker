package com.medicine.tracker.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for global medicine responses
 * Contains global medicine information returned from API calls
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalMedicineResponse {
    
    private UUID id;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}