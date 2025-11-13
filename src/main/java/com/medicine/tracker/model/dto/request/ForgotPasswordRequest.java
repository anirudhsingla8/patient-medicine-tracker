package com.medicine.tracker.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for forgot password requests
 * Contains user email and new password for password reset
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "New password should be at least 6 characters")
    private String newPassword;
    
    public String getEmail() {
        return email;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
}