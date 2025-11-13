package com.medicine.tracker.controller;

import com.medicine.tracker.model.dto.request.LoginRequest;
import com.medicine.tracker.model.dto.request.RegisterRequest;
import com.medicine.tracker.model.dto.request.ForgotPasswordRequest;
import com.medicine.tracker.model.dto.response.AuthResponse;
import com.medicine.tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication endpoints
 * Handles user registration, login, and password management
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Register a new user
     * @param registerRequest The registration request containing user details
     * @return Authentication response with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Authenticate a user and return JWT token
     * @param loginRequest The login request containing credentials
     * @return Authentication response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Handle forgot password request - change user's password
     * This endpoint also invalidates all existing tokens for the user
     * @param forgotPasswordRequest The request containing email and new password
     * @return Authentication response with new JWT token
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        AuthResponse response = authService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok(response);
    }
}