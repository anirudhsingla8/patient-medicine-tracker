package com.medicine.tracker.model.dto.request;

import com.medicine.tracker.model.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Future;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO for medicine requests
 * Contains medicine information for creating or updating medicines
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequest {
    
    @NotBlank(message = "Medicine name is required")
    private String name;
    
    private String imageUrl;
    
    private String dosage;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;
    
    private String category;
    
    private String notes;
    
    private List<Medicine.Composition> composition;
    
    private String form;
    
    private UUID profileId;
    
    public String getName() {
        return name;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public List<Medicine.Composition> getComposition() {
        return composition;
    }
    
    public String getForm() {
        return form;
    }
    
    public UUID getProfileId() {
        return profileId;
    }
}