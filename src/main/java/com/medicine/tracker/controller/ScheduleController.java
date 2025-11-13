package com.medicine.tracker.controller;

import com.medicine.tracker.model.dto.request.ScheduleRequest;
import com.medicine.tracker.model.dto.response.ScheduleResponse;
import com.medicine.tracker.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for schedule management endpoints
 * Handles CRUD operations for medicine dosage schedules
 */
@RestController
@RequestMapping("/api")  // <-- changed from "/api/schedules" to "/api"
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Create a new schedule for a medicine
     * @param medicineId The ID of the medicine to schedule
     * @param scheduleRequest The request containing schedule details
     * @return Created schedule response
     */
    @PostMapping("/medicines/{medicineId}/schedules")
    public ResponseEntity<ScheduleResponse> createSchedule(
            @PathVariable UUID medicineId,
            @Valid @RequestBody ScheduleRequest scheduleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
                (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();

        ScheduleResponse response = scheduleService.createSchedule(userId, medicineId, scheduleRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all schedules for a specific medicine
     * @param medicineId The ID of the medicine to retrieve schedules for
     * @return List of schedules for the medicine
     */
    @GetMapping("/medicines/{medicineId}/schedules")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesForMedicine(@PathVariable UUID medicineId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
                (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();

        List<ScheduleResponse> schedules = scheduleService.getSchedulesForMedicine(userId, medicineId);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Get all schedules for a specific profile
     * @param profileId The ID of the profile to retrieve schedules for
     * @return List of schedules for the profile
     */
    @GetMapping("/profiles/{profileId}/schedules")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesForProfile(@PathVariable UUID profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
                (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();

        List<ScheduleResponse> schedules = scheduleService.getSchedulesForProfile(userId, profileId);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Get all schedules for the authenticated user
     * @return List of all schedules for the user
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
                (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();

        List<ScheduleResponse> schedules = scheduleService.getSchedulesForUser(userId);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Get a specific schedule by ID
     * @param scheduleId The ID of the schedule to retrieve
     * @return The requested schedule response
     */
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable UUID scheduleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
                (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();

        ScheduleResponse schedule = scheduleService.getScheduleById(scheduleId, userId);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Update an existing schedule
     * @param scheduleId The ID of the schedule to update
     * @param scheduleRequest The request containing updated schedule details
     * @return Updated schedule response
     */
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody ScheduleRequest scheduleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
                (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();

        ScheduleResponse schedule = scheduleService.updateSchedule(scheduleId, userId, scheduleRequest);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Delete a schedule by ID
     * @param scheduleId The ID of the schedule to delete
     * @return Empty response with 204 status
     */
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID scheduleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.medicine.tracker.model.entity.User user =
                (com.medicine.tracker.model.entity.User) authentication.getPrincipal();
        UUID userId = user.getId();

        scheduleService.deleteSchedule(scheduleId, userId);
        return ResponseEntity.noContent().build();
    }
}
