package com.medicine.tracker.model.dto.request;

import com.medicine.tracker.model.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * DTO for schedule requests
 * Contains schedule information for creating or updating schedules
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {
    
    @NotNull(message = "Time of day is required")
    private LocalTime timeOfDay;
    
    private Schedule.Frequency frequency;
    
    private Boolean isActive;
}