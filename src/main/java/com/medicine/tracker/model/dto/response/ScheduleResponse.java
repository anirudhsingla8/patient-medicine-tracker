package com.medicine.tracker.model.dto.response;

import com.medicine.tracker.model.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for schedule responses
 * Contains schedule information returned from API calls
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    
    private UUID id;
    private UUID medicineId;
    private UUID profileId;
    private UUID userId;
    private LocalTime timeOfDay;
    private Schedule.Frequency frequency;
    private Boolean isActive;
    private LocalDateTime createdAt;
}