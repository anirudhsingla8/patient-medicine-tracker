package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.entity.User;
import com.medicine.tracker.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation of JwtService for JWT token operations
 * Handles token generation, validation, and extraction of claims with security features
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    /**
     * Generate a JWT token for a user
     * @param user The user to generate the token for
     * @return Generated JWT token
     */
    @Override
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }
    
    /**
     * Generate a JWT token with extra claims
     * @param user The user to generate the token for
     * @param claims Additional claims to include in the token
     * @return Generated JWT token
     */
    @Override
    public String generateToken(User user, Claims claims) {
        return generateToken(claims, user);
    }
    
    /**
     * Generate a JWT token with claims
     * @param extraClaims Additional claims to include in the token
     * @param user The user to generate the token for
     * @return Generated JWT token
     */
    private String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("userId", user.getId())
                .claim("passwordLastChanged", convertToDate(user.getPasswordLastChanged()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Validate a JWT token against a user
     * @param token The token to validate
     * @param user The user to validate the token against
     * @return true if token is valid, false otherwise
     */
    @Override
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        final Date expiration = extractExpiration(token);
        
        // Check if token is expired
        if (expiration.before(new Date())) {
            return false;
        }
        
        // Check if token was issued before the last password change
        Claims claims = extractAllClaims(token);
        Date tokenPasswordLastChanged = claims.get("passwordLastChanged", Date.class);
        if (tokenPasswordLastChanged != null && user.getPasswordLastChanged() != null) {
            // Convert user's passwordLastChanged to Date for comparison
            Date userPasswordLastChanged = convertToDate(user.getPasswordLastChanged());
            if (tokenPasswordLastChanged.before(userPasswordLastChanged)) {
                return false;
            }
        }
        
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }
    
    /**
     * Check if a token is expired
     * @param token The token to check
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Extract the username from a JWT token
     * @param token The token to extract the username from
     * @return The username extracted from the token
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extract the expiration date from a JWT token
     * @param token The token to extract the expiration date from
     * @return The expiration date extracted from the token
     */
    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extract a specific claim from a JWT token
     * @param token The token to extract the claim from
     * @param claimsResolver Function to resolve the claim
     * @param <T> The type of the claim
     * @return The extracted claim
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extract all claims from a JWT token
     * @param token The token to extract claims from
     * @return All claims extracted from the token
     */
    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Get the signing key for JWT tokens
     * @return The signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Convert LocalDateTime to Date
     * @param localDateTime The LocalDateTime to convert
     * @return The converted Date
     */
    private Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}