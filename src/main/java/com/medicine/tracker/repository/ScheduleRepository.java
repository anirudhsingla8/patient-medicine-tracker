package com.medicine.tracker.repository;

import com.medicine.tracker.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Schedule entity operations
 * Provides CRUD operations and custom queries for schedule management
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    
    /**
     * Find all active schedules for a specific medicine
     * @param medicineId The medicine ID to filter schedules by
     * @return List of active schedules for the medicine
     */
    List<Schedule> findByMedicineIdAndIsActiveTrue(UUID medicineId);
    
    /**
     * Find all active schedules for a specific profile
     * @param profileId The profile ID to filter schedules by
     * @return List of active schedules for the profile
     */
    List<Schedule> findByProfileIdAndIsActiveTrue(UUID profileId);
    
    /**
     * Find all active schedules for a specific user
     * @param userId The user ID to filter schedules by
     * @return List of active schedules for the user
     */
    List<Schedule> findByUserIdAndIsActiveTrue(UUID userId);
    
    /**
     * Find all active schedules for a specific user and profile
     * @param userId The user ID to filter schedules by
     * @param profileId The profile ID to filter schedules by
     * @return List of active schedules matching the criteria
     */
    List<Schedule> findByUserIdAndProfileIdAndIsActiveTrue(UUID userId, UUID profileId);
    
    /**
     * Check if a schedule exists for a specific user
     * @param userId The user ID to check
     * @param id The schedule ID to check
     * @return true if schedule exists for the user, false otherwise
     */
    boolean existsByUserIdAndId(UUID userId, UUID id);
    
    /**
     * Find all active schedules matching a specific time of day
     * @param timeOfDay The time of day to match
     * @return List of active schedules matching the time
     */
    List<Schedule> findByTimeOfDayAndIsActiveTrue(LocalTime timeOfDay);
    
    /**
     * Check if a schedule exists with the same medicine, time of day, and frequency
     * @param medicineId The medicine ID to check
     * @param timeOfDay The time of day to check
     * @param frequency The frequency to check
     * @param isActive The active status to check
     * @return true if a matching schedule exists, false otherwise
     */
    boolean existsByMedicineIdAndTimeOfDayAndFrequencyAndIsActiveTrue(UUID medicineId, LocalTime timeOfDay, com.medicine.tracker.model.entity.Schedule.Frequency frequency);
    
    /**
     * Find schedules with the same medicine, time of day, and frequency
     * @param medicineId The medicine ID to check
     * @param timeOfDay The time of day to check
     * @param frequency The frequency to check
     * @param isActive The active status to check
     * @return List of matching schedules
     */
    List<Schedule> findByMedicineIdAndTimeOfDayAndFrequencyAndIsActiveTrue(UUID medicineId, LocalTime timeOfDay, com.medicine.tracker.model.entity.Schedule.Frequency frequency);
}