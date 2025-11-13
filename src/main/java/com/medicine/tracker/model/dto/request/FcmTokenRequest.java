package com.medicine.tracker.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for FCM token requests
 * Contains FCM token information for updating user's notification token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmTokenRequest {
    
    @NotBlank(message = "FCM token is required")
    private String fcmToken;
    
    public String getFcmToken() {
        return fcmToken;
    }
}