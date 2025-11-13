package com.medicine.tracker.repository;

import com.medicine.tracker.model.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for TokenBlacklist entity operations
 * Provides CRUD operations and custom queries for token blacklist management
 */
@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, UUID> {
    
    /**
     * Check if a token exists in the blacklist
     * @param token The token to check
     * @return true if token is blacklisted, false otherwise
     */
    boolean existsByToken(String token);
    
    /**
     * Find a blacklisted token by its value
     * @param token The token to search for
     * @return Optional containing the blacklisted token if found
     */
    Optional<TokenBlacklist> findByToken(String token);
    
    /**
     * Find all expired tokens in the blacklist
     * @param currentTime The current time to compare against
     * @return List of expired blacklisted tokens
     */
    List<TokenBlacklist> findByExpiresAtBefore(LocalDateTime currentTime);
    
    /**
     * Find all tokens for a specific user in the blacklist
     * @param userId The user ID to filter by
     * @return List of blacklisted tokens for the user
     */
    List<TokenBlacklist> findByUserId(UUID userId);
}