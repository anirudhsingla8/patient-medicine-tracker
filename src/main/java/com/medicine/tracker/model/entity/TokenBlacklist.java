package com.medicine.tracker.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing blacklisted JWT tokens in the medicine tracker application
 * Used for security purposes, particularly for invalidating tokens on password change
 */
@Entity
@Table(name = "token_blacklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String token;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "blacklisted_at")
    private LocalDateTime blacklistedAt;
    
    // Default constructor to initialize blacklisted_at
    @PrePersist
    protected void onCreate() {
        blacklistedAt = LocalDateTime.now();
    }
}