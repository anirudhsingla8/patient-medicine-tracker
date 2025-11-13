package com.medicine.tracker.service;

import com.medicine.tracker.model.entity.TokenBlacklist;

import java.util.UUID;

/**
 * Service interface for token blacklist operations
 * Handles blacklisting of JWT tokens for security purposes
 */
public interface TokenBlacklistService {
    
    /**
     * Blacklist a specific token
     * @param token The token to blacklist
     * @param userId The ID of the user who owns the token
     * @param expirationDate The expiration date of the token
     */
    void blacklistToken(String token, UUID userId, java.time.LocalDateTime expirationDate);
    
    /**
     * Check if a token is blacklisted
     * @param token The token to check
     * @return true if token is blacklisted, false otherwise
     */
    boolean isTokenBlacklisted(String token);
    
    /**
     * Blacklist all tokens for a specific user
     * This is typically called when a user changes their password
     * @param userId The ID of the user whose tokens should be blacklisted
     */
    void blacklistAllUserTokens(UUID userId);
    
    /**
     * Remove expired tokens from the blacklist
     */
    void removeExpiredTokens();
}