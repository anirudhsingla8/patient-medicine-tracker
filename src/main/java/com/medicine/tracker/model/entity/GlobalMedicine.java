package com.medicine.tracker.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing global medicine information in the medicine tracker application
 * Contains comprehensive information about medicines that can be shared across users
 */
@Entity
@Table(name = "global_medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalMedicine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "brand_name")
    private String brandName;
    
    @Column(name = "generic_name")
    private String genericName;
    
    @Column(name = "dosage_form")
    private String dosageForm;
    
    private String strength;
    
    private String manufacturer;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "global_medicine_indications", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "indication")
    private List<String> indications;
    
    @ElementCollection
    @CollectionTable(name = "global_medicine_contraindications", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "contraindication")
    private List<String> contraindications;
    
    @ElementCollection
    @CollectionTable(name = "global_medicine_side_effects", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "side_effect")
    private List<String> sideEffects;
    
    @ElementCollection
    @CollectionTable(name = "global_medicine_warnings", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "warning")
    private List<String> warnings;
    
    @ElementCollection
    @CollectionTable(name = "global_medicine_interactions", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "interaction")
    private List<String> interactions;
    
    @Column(name = "storage_instructions")
    private String storageInstructions;
    
    private String category;
    
    @Column(name = "atc_code")
    private String atcCode;
    
    @Column(name = "fda_approval_date")
    private LocalDate fdaApprovalDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Default constructor to initialize created_at
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    // Update updated_at on update
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}