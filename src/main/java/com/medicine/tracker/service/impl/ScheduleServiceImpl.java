package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.dto.request.ScheduleRequest;
import com.medicine.tracker.model.dto.response.ScheduleResponse;
import com.medicine.tracker.model.entity.Medicine;
import com.medicine.tracker.model.entity.Schedule;
import com.medicine.tracker.repository.MedicineRepository;
import com.medicine.tracker.repository.ScheduleRepository;
import com.medicine.tracker.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of ScheduleService for schedule management operations
 * Handles CRUD operations for medicine dosage schedules with proper validation
 */
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    
    private final ScheduleRepository scheduleRepository;
    private final MedicineRepository medicineRepository;
    
    /**
     * Create a new schedule for a medicine
     * @param userId The ID of the user creating the schedule
     * @param medicineId The ID of the medicine to schedule
     * @param scheduleRequest The request containing schedule details
     * @return Created schedule response
     */
    @Override
    public ScheduleResponse createSchedule(UUID userId, UUID medicineId, ScheduleRequest scheduleRequest) {
        // Verify that the medicine belongs to the user
        Medicine medicine = medicineRepository.findById(medicineId)
                .filter(m -> m.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Medicine not found or does not belong to user"));
        
        // Check if a schedule with the same medicine, time of day, and frequency already exists
        Schedule.Frequency frequency = scheduleRequest.getFrequency() != null ? scheduleRequest.getFrequency() : Schedule.Frequency.DAILY;
        if (scheduleRepository.existsByMedicineIdAndTimeOfDayAndFrequencyAndIsActiveTrue(medicineId, scheduleRequest.getTimeOfDay(), frequency)) {
            throw new RuntimeException("A schedule already exists for this medicine with the same time and frequency");
        }
        
        Schedule schedule = Schedule.builder()
                .medicineId(medicineId)
                .profileId(medicine.getProfileId())
                .userId(userId)
                .timeOfDay(scheduleRequest.getTimeOfDay())
                .frequency(scheduleRequest.getFrequency())
                .isActive(scheduleRequest.getIsActive())
                .build();
        
        Schedule savedSchedule = scheduleRepository.save(schedule);
        
        return mapToScheduleResponse(savedSchedule);
    }
    
    /**
     * Get all schedules for a specific medicine
     * @param userId The ID of the user requesting schedules
     * @param medicineId The ID of the medicine to retrieve schedules for
     * @return List of schedules for the medicine
     */
    @Override
    public List<ScheduleResponse> getSchedulesForMedicine(UUID userId, UUID medicineId) {
        // Verify that the medicine belongs to the user
        if (!medicineRepository.existsByUserIdAndId(userId, medicineId)) {
            throw new RuntimeException("Medicine not found or does not belong to user");
        }
        
        List<Schedule> schedules = scheduleRepository.findByMedicineIdAndIsActiveTrue(medicineId);
        
        return schedules.stream()
                .map(this::mapToScheduleResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all schedules for a specific profile
     * @param userId The ID of the user requesting schedules
     * @param profileId The ID of the profile to retrieve schedules for
     * @return List of schedules for the profile
     */
    @Override
    public List<ScheduleResponse> getSchedulesForProfile(UUID userId, UUID profileId) {
        List<Schedule> schedules = scheduleRepository.findByUserIdAndProfileIdAndIsActiveTrue(userId, profileId);
        
        return schedules.stream()
                .map(this::mapToScheduleResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all schedules for a user
     * @param userId The ID of the user to retrieve schedules for
     * @return List of all schedules for the user
     */
    @Override
    public List<ScheduleResponse> getSchedulesForUser(UUID userId) {
        List<Schedule> schedules = scheduleRepository.findByUserIdAndIsActiveTrue(userId);
        
        return schedules.stream()
                .map(this::mapToScheduleResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific schedule by ID
     * @param scheduleId The ID of the schedule to retrieve
     * @param userId The ID of the user requesting the schedule
     * @return The requested schedule response
     */
    @Override
    public ScheduleResponse getScheduleById(UUID scheduleId, UUID userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .filter(s -> s.getUserId().equals(userId) && s.getIsActive())
                .orElseThrow(() -> new RuntimeException("Schedule not found or does not belong to user"));
        
        return mapToScheduleResponse(schedule);
    }
    
    /**
     * Update an existing schedule
     * @param scheduleId The ID of the schedule to update
     * @param userId The ID of the user updating the schedule
     * @param scheduleRequest The request containing updated schedule details
     * @return Updated schedule response
     */
    @Override
    public ScheduleResponse updateSchedule(UUID scheduleId, UUID userId, ScheduleRequest scheduleRequest) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .filter(s -> s.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Schedule not found or does not belong to user"));
        
        // Check if a schedule with the same medicine, time of day, and frequency already exists (excluding the current schedule being updated)
        Schedule.Frequency frequency = scheduleRequest.getFrequency() != null ? scheduleRequest.getFrequency() : Schedule.Frequency.DAILY;
        if (scheduleRepository.existsByMedicineIdAndTimeOfDayAndFrequencyAndIsActiveTrue(schedule.getMedicineId(), scheduleRequest.getTimeOfDay(), frequency)) {
            // Find the existing schedule that matches the new values
            List<Schedule> existingSchedules = scheduleRepository.findByMedicineIdAndTimeOfDayAndFrequencyAndIsActiveTrue(schedule.getMedicineId(), scheduleRequest.getTimeOfDay(), frequency);
            // Only throw exception if the matching schedule is not the one we're updating
            if (existingSchedules.stream().anyMatch(s -> !s.getId().equals(scheduleId))) {
                throw new RuntimeException("A schedule already exists for this medicine with the same time and frequency");
            }
        }
        
        // Update schedule properties
        schedule.setTimeOfDay(scheduleRequest.getTimeOfDay());
        schedule.setFrequency(scheduleRequest.getFrequency());
        schedule.setIsActive(scheduleRequest.getIsActive());
        
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        
        return mapToScheduleResponse(updatedSchedule);
    }
    
    /**
     * Delete a schedule by ID
     * @param scheduleId The ID of the schedule to delete
     * @param userId The ID of the user deleting the schedule
     */
    @Override
    public void deleteSchedule(UUID scheduleId, UUID userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .filter(s -> s.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Schedule not found or does not belong to user"));
        
        // Instead of hard delete, we can set isActive to false if needed
        // For now, we'll hard delete as schedules are not as critical as medicines
        scheduleRepository.delete(schedule);
    }
    
    /**
     * Check if a schedule exists for a user
     * @param scheduleId The ID of the schedule to check
     * @param userId The ID of the user to check against
     * @return true if schedule exists and belongs to user, false otherwise
     */
    @Override
    public boolean scheduleExistsForUser(UUID scheduleId, UUID userId) {
        return scheduleRepository.existsByUserIdAndId(userId, scheduleId);
    }
    
    /**
     * Maps a Schedule entity to a ScheduleResponse DTO
     * @param schedule The schedule entity to map
     * @return Mapped schedule response
     */
    private ScheduleResponse mapToScheduleResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .medicineId(schedule.getMedicineId())
                .profileId(schedule.getProfileId())
                .userId(schedule.getUserId())
                .timeOfDay(schedule.getTimeOfDay())
                .frequency(schedule.getFrequency())
                .isActive(schedule.getIsActive())
                .createdAt(schedule.getCreatedAt())
                .build();
    }
}