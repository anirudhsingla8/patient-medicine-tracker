package com.medicine.tracker.repository;

import com.medicine.tracker.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Profile entity operations
 * Provides CRUD operations and custom queries for profile management
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    
    /**
     * Find all profiles associated with a specific user
     * @param userId The user ID to filter profiles by
     * @return List of profiles belonging to the user
     */
    List<Profile> findByUserId(UUID userId);
    
    /**
     * Check if a profile exists for a specific user
     * @param userId The user ID to check
     * @param id The profile ID to check
     * @return true if profile exists for the user, false otherwise
     */
    boolean existsByUserIdAndId(UUID userId, UUID id);
}