package com.medicine.tracker.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a medicine schedule in the medicine tracker application
 * Contains timing and frequency information for dosage reminders
 */
@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "medicine_id", nullable = false)
    private UUID medicineId;
    
    @Column(name = "profile_id", nullable = false)
    private UUID profileId;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "time_of_day", nullable = false)
    private LocalTime timeOfDay;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Frequency frequency;
    
    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean isActive;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor to initialize created_at and isActive
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
        if (frequency == null) {
            frequency = Frequency.DAILY;
        }
    }
    
    /**
     * Enum for schedule frequency
     */
    public enum Frequency {
        DAILY, WEEKLY, BIWEEKLY, MONTHLY, CUSTOM
    }
}