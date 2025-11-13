package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.entity.TokenBlacklist;
import com.medicine.tracker.repository.TokenBlacklistRepository;
import com.medicine.tracker.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of TokenBlacklistService for token blacklist operations
 * Handles blacklisting of JWT tokens for security purposes
 */
@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    
    private final TokenBlacklistRepository tokenBlacklistRepository;
    
    /**
     * Blacklist a specific token
     * @param token The token to blacklist
     * @param userId The ID of the user who owns the token
     * @param expirationDate The expiration date of the token
     */
    @Override
    public void blacklistToken(String token, UUID userId, LocalDateTime expirationDate) {
        // Check if token is already blacklisted
        if (tokenBlacklistRepository.existsByToken(token)) {
            return;
        }
        
        TokenBlacklist tokenBlacklist = TokenBlacklist.builder()
                .token(token)
                .userId(userId)
                .expiresAt(expirationDate)
                .build();
        
        tokenBlacklistRepository.save(tokenBlacklist);
    }
    
    /**
     * Check if a token is blacklisted
     * @param token The token to check
     * @return true if token is blacklisted, false otherwise
     */
    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
    
    /**
     * Blacklist all tokens for a specific user
     * This is typically called when a user changes their password
     * @param userId The ID of the user whose tokens should be blacklisted
     */
    @Override
    public void blacklistAllUserTokens(UUID userId) {
        // In this implementation, we're relying on the password_last_changed field
        // in the user table to invalidate tokens issued before that time
        // Rather than maintaining an actual blacklist of all tokens
        // The JWT validation will check if the token was issued before the password was changed
    }
    
    /**
     * Remove expired tokens from the blacklist
     */
    @Override
    public void removeExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenBlacklistRepository.findByExpiresAtBefore(now)
                .forEach(tokenBlacklistRepository::delete);
    }
}