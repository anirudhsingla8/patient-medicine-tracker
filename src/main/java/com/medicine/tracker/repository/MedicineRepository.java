package com.medicine.tracker.repository;

import com.medicine.tracker.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Medicine entity operations
 * Provides CRUD operations and custom queries for medicine management
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    
    /**
     * Find all active medicines for a specific user
     * @param userId The user ID to filter medicines by
     * @return List of active medicines belonging to the user
     */
    List<Medicine> findByUserIdAndStatus(UUID userId, Medicine.MedicineStatus status);
    
    /**
     * Find all active medicines for a specific profile
     * @param profileId The profile ID to filter medicines by
     * @return List of active medicines belonging to the profile
     */
    List<Medicine> findByProfileIdAndStatus(UUID profileId, Medicine.MedicineStatus status);
    
    /**
     * Find all medicines for a specific profile regardless of status
     * @param profileId The profile ID to filter medicines by
     * @return List of medicines belonging to the profile
     */
    List<Medicine> findByProfileId(UUID profileId);
    
    /**
     * Find all active medicines for a specific user and profile
     * @param userId The user ID to filter medicines by
     * @param profileId The profile ID to filter medicines by
     * @param status The status to filter by (ACTIVE)
     * @return List of active medicines matching the criteria
     */
    List<Medicine> findByUserIdAndProfileIdAndStatus(UUID userId, UUID profileId, Medicine.MedicineStatus status);
    
    /**
     * Check if a medicine exists for a specific user
     * @param userId The user ID to check
     * @param id The medicine ID to check
     * @return true if medicine exists for the user, false otherwise
     */
    boolean existsByUserIdAndId(UUID userId, UUID id);
    
    /**
     * Find medicines with expiry dates approaching (within 30 days)
     * @return List of medicines with expiry dates within 30 days
     */
    @Query(value = "SELECT * FROM user_medicines m WHERE m.expiry_date <= CURRENT_DATE + INTERVAL '30 day' AND m.status = 'ACTIVE'", nativeQuery = true)
    List<Medicine> findExpiringMedicines();
}