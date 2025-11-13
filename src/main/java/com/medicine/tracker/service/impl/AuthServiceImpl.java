package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.dto.request.LoginRequest;
import com.medicine.tracker.model.dto.request.RegisterRequest;
import com.medicine.tracker.model.dto.request.ForgotPasswordRequest;
import com.medicine.tracker.model.dto.response.AuthResponse;
import com.medicine.tracker.model.entity.User;
import com.medicine.tracker.repository.UserRepository;
import com.medicine.tracker.service.AuthService;
import com.medicine.tracker.service.JwtService;
import com.medicine.tracker.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of AuthService for authentication operations
 * Handles user registration, login, and password management with security features
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;
    
    /**
     * Register a new user
     * @param registerRequest The registration request containing user details
     * @return Authentication response with JWT token
     */
    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Registering new user with email: {}", registerRequest.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registration failed: User with email already exists: {}", registerRequest.getEmail());
            throw new RuntimeException("User with email already exists");
        }
        
        // Create new user
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .passwordLastChanged(LocalDateTime.now())
                .build();
        
        // Save user to database
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        // Generate JWT token
        String token = jwtService.generateToken(savedUser);
        
        return AuthResponse.builder()
                .token(token)
                .userEmail(savedUser.getEmail())
                .userId(savedUser.getId())
                .build();
    }
    
    /**
     * Authenticate a user and return JWT token
     * @param loginRequest The login request containing credentials
     * @return Authentication response with JWT token
     */
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("User login attempt with email: {}", loginRequest.getEmail());
        
        // Authenticate user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        
        // Get user from authentication
        User user = (User) authentication.getPrincipal();
        log.info("User authenticated successfully with ID: {}", user.getId());
        
        // Generate JWT token
        String token = jwtService.generateToken(user);
        
        return AuthResponse.builder()
                .token(token)
                .userEmail(user.getEmail())
                .userId(user.getId())
                .build();
    }
    
    /**
     * Handle forgot password request - change user's password
     * This method also invalidates all existing tokens for the user
     * @param forgotPasswordRequest The request containing email and new password
     * @return Authentication response with new JWT token
     */
    @Override
    public AuthResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        log.info("Processing forgot password request for email: {}", forgotPasswordRequest.getEmail());
        
        // Find user by email
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> {
                    log.warn("Forgot password failed: User not found with email: {}", forgotPasswordRequest.getEmail());
                    return new RuntimeException("User not found with email: " + forgotPasswordRequest.getEmail());
                });
        
        // Update password
        user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getNewPassword()));
        // Update password last changed timestamp to invalidate existing tokens
        user.setPasswordLastChanged(LocalDateTime.now());
        
        // Save updated user
        User updatedUser = userRepository.save(user);
        log.info("Password updated successfully for user ID: {}", updatedUser.getId());
        
        // Blacklist all existing tokens for this user by updating password last changed
        // This will make all previous tokens invalid since they were issued before the password change
        tokenBlacklistService.blacklistAllUserTokens(user.getId());
        
        // Generate new JWT token
        String token = jwtService.generateToken(updatedUser);
        
        return AuthResponse.builder()
                .token(token)
                .userEmail(updatedUser.getEmail())
                .userId(updatedUser.getId())
                .build();
    }
}