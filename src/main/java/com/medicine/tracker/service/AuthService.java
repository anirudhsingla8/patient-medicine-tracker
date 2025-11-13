package com.medicine.tracker.service;

import com.medicine.tracker.model.dto.request.LoginRequest;
import com.medicine.tracker.model.dto.request.RegisterRequest;
import com.medicine.tracker.model.dto.request.ForgotPasswordRequest;
import com.medicine.tracker.model.dto.response.AuthResponse;

/**
 * Service interface for authentication operations
 * Handles user registration, login, and password management
 */
public interface AuthService {
    
    /**
     * Register a new user
     * @param registerRequest The registration request containing user details
     * @return Authentication response with JWT token
     */
    AuthResponse register(RegisterRequest registerRequest);
    
    /**
     * Authenticate a user and return JWT token
     * @param loginRequest The login request containing credentials
     * @return Authentication response with JWT token
     */
    AuthResponse login(LoginRequest loginRequest);
    
    /**
     * Handle forgot password request - change user's password
     * This method also invalidates all existing tokens for the user
     * @param forgotPasswordRequest The request containing email and new password
     * @return Authentication response with new JWT token
     */
    AuthResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
}