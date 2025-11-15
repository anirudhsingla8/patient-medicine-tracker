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
 * DTO for medicine responses with profile information
 * Contains medicine information along with profile details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineWithProfileResponse {
    
    private UUID id;
    private UUID userId;
    private UUID profileId;
    private String profileName;
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
}