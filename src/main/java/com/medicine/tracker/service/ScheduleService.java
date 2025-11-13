package com.medicine.tracker.service;

import com.medicine.tracker.model.dto.request.ScheduleRequest;
import com.medicine.tracker.model.dto.response.ScheduleResponse;
import com.medicine.tracker.model.entity.Schedule;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for schedule management operations
 * Handles CRUD operations for medicine dosage schedules
 */
public interface ScheduleService {
    
    /**
     * Create a new schedule for a medicine
     * @param userId The ID of the user creating the schedule
     * @param medicineId The ID of the medicine to schedule
     * @param scheduleRequest The request containing schedule details
     * @return Created schedule response
     */
    ScheduleResponse createSchedule(UUID userId, UUID medicineId, ScheduleRequest scheduleRequest);
    
    /**
     * Get all schedules for a specific medicine
     * @param userId The ID of the user requesting schedules
     * @param medicineId The ID of the medicine to retrieve schedules for
     * @return List of schedules for the medicine
     */
    List<ScheduleResponse> getSchedulesForMedicine(UUID userId, UUID medicineId);
    
    /**
     * Get all schedules for a specific profile
     * @param userId The ID of the user requesting schedules
     * @param profileId The ID of the profile to retrieve schedules for
     * @return List of schedules for the profile
     */
    List<ScheduleResponse> getSchedulesForProfile(UUID userId, UUID profileId);
    
    /**
     * Get all schedules for a user
     * @param userId The ID of the user to retrieve schedules for
     * @return List of all schedules for the user
     */
    List<ScheduleResponse> getSchedulesForUser(UUID userId);
    
    /**
     * Get a specific schedule by ID
     * @param scheduleId The ID of the schedule to retrieve
     * @param userId The ID of the user requesting the schedule
     * @return The requested schedule response
     */
    ScheduleResponse getScheduleById(UUID scheduleId, UUID userId);
    
    /**
     * Update an existing schedule
     * @param scheduleId The ID of the schedule to update
     * @param userId The ID of the user updating the schedule
     * @param scheduleRequest The request containing updated schedule details
     * @return Updated schedule response
     */
    ScheduleResponse updateSchedule(UUID scheduleId, UUID userId, ScheduleRequest scheduleRequest);
    
    /**
     * Delete a schedule by ID
     * @param scheduleId The ID of the schedule to delete
     * @param userId The ID of the user deleting the schedule
     */
    void deleteSchedule(UUID scheduleId, UUID userId);
    
    /**
     * Check if a schedule exists for a user
     * @param scheduleId The ID of the schedule to check
     * @param userId The ID of the user to check against
     * @return true if schedule exists and belongs to user, false otherwise
     */
    boolean scheduleExistsForUser(UUID scheduleId, UUID userId);
}