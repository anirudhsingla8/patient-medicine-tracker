package com.medicine.tracker.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for profile responses
 * Contains profile information returned from API calls
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    
    private UUID id;
    private UUID userId;
    private String name;
    private LocalDateTime createdAt;
}