package com.medicine.tracker.model.dto.response;

import com.medicine.tracker.model.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for medicine responses
 * Contains medicine information returned from API calls
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineResponse {
    
    private UUID id;
    private UUID userId;
    private UUID profileId;
    private String name;
    private String imageUrl;
    private String dosage;
    private Integer quantity;
    private LocalDate expiryDate;
    private String category;
    private String notes;
    private List<Medicine.Composition> composition;
    private String form;
    private Medicine.MedicineStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public UUID getId() {
        return id;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public UUID getProfileId() {
        return profileId;
    }
    
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
    
    public Medicine.MedicineStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}