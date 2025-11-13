package com.medicine.tracker.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Entity representing a user's medicine in the medicine tracker application
 * Contains medicine details, dosage information, and scheduling data
 */
@Entity
@Table(name = "user_medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "profile_id", nullable = false)
    private UUID profileId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    private String dosage;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;
    
    private String category;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSONB", name = "composition")
    private List<Composition> composition;
    
    @Column(length = 20)
    private String form;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MedicineStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Default constructor to initialize created_at and status
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = MedicineStatus.ACTIVE;
        }
    }
    
    // Update updated_at on update
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Inner class to represent medicine composition details
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Composition {
        private String name;
        private BigDecimal strengthValue;
        private String strengthUnit;
        
        public String getName() {
            return name;
        }
        
        public BigDecimal getStrengthValue() {
            return strengthValue;
        }
        
        public String getStrengthUnit() {
            return strengthUnit;
        }
    }
    
    /**
     * Enum for medicine status (active/inactive for soft deletion)
     */
    public enum MedicineStatus {
        ACTIVE, INACTIVE
    }
    
}