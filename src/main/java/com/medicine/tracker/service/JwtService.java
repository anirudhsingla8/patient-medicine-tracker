package com.medicine.tracker.service;

import com.medicine.tracker.model.entity.User;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

/**
 * Service interface for JWT token operations
 * Handles token generation, validation, and extraction of claims
 */
public interface JwtService {
    
    /**
     * Generate a JWT token for a user
     * @param user The user to generate the token for
     * @return Generated JWT token
     */
    String generateToken(User user);
    
    /**
     * Generate a JWT token with extra claims
     * @param user The user to generate the token for
     * @param claims Additional claims to include in the token
     * @return Generated JWT token
     */
    String generateToken(User user, Claims claims);
    
    /**
     * Validate a JWT token against a user
     * @param token The token to validate
     * @param user The user to validate the token against
     * @return true if token is valid, false otherwise
     */
    boolean isTokenValid(String token, User user);
    
    /**
     * Extract the username from a JWT token
     * @param token The token to extract the username from
     * @return The username extracted from the token
     */
    String extractUsername(String token);
    
    /**
     * Extract the expiration date from a JWT token
     * @param token The token to extract the expiration date from
     * @return The expiration date extracted from the token
     */
    Date extractExpiration(String token);
    
    /**
     * Extract a specific claim from a JWT token
     * @param token The token to extract the claim from
     * @param claimsResolver Function to resolve the claim
     * @param <T> The type of the claim
     * @return The extracted claim
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    
    /**
     * Extract all claims from a JWT token
     * @param token The token to extract claims from
     * @return All claims extracted from the token
     */
    Claims extractAllClaims(String token);
}