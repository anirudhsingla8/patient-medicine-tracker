package com.medicine.tracker.service.impl;

import com.medicine.tracker.model.entity.Medicine;
import com.medicine.tracker.model.entity.Schedule;
import com.medicine.tracker.model.entity.User;
import com.medicine.tracker.repository.MedicineRepository;
import com.medicine.tracker.repository.ScheduleRepository;
import com.medicine.tracker.repository.UserRepository;
import com.medicine.tracker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Implementation of NotificationService for automated notification operations
 * Handles dosage reminders and expiry notifications using scheduled jobs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    
    private final ScheduleRepository scheduleRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    
    /**
     * Send dosage reminder notifications for schedules that are due
     */
    @Override
    public void sendDosageReminders() {
        log.info("Starting dosage reminder notifications");
        List<Schedule> dueSchedules = getDueSchedules();
        log.info("Found {} schedules due for dosage reminders", dueSchedules.size());
        
        for (Schedule schedule : dueSchedules) {
            // Get the user for this schedule
            User user = userRepository.findById(schedule.getUserId()).orElse(null);
            if (user != null && user.getFcmToken() != null) {
                // Send notification to user's FCM token
                String title = "Medicine Reminder";
                String body = "Time to take your medicine: " + getMedicineName(schedule.getMedicineId());
                sendNotification(user.getId().toString(), title, body);
                log.info("Sent dosage reminder for user {} and medicine {}", 
                        user.getId(), schedule.getMedicineId());
            } else {
                if (user == null) {
                    log.warn("User not found for schedule ID: {}", schedule.getId());
                } else {
                    log.warn("No FCM token found for user ID: {}", user.getId());
                }
            }
        }
        log.info("Completed dosage reminder notifications");
    }
    
    /**
     * Send notifications for medicines that are expiring soon
     */
    @Override
    public void sendExpiryNotifications() {
        log.info("Starting expiry notifications");
        // Find medicines expiring within 7 days
        List<Medicine> expiringMedicines = medicineRepository.findExpiringMedicines();
        log.info("Found {} medicines expiring soon", expiringMedicines.size());
        
        for (Medicine medicine : expiringMedicines) {
            User user = userRepository.findById(medicine.getUserId()).orElse(null);
            if (user != null && user.getFcmToken() != null) {
                String title = "Medicine Expiry Alert";
                String body = "Your medicine '" + medicine.getName() + "' is expiring on " + 
                             medicine.getExpiryDate().toString();
                sendNotification(user.getId().toString(), title, body);
                log.info("Sent expiry notification for user {} and medicine {}", 
                        user.getId(), medicine.getId());
            } else {
                if (user == null) {
                    log.warn("User not found for medicine ID: {}", medicine.getId());
                } else {
                    log.warn("No FCM token found for user ID: {}", user.getId());
                }
            }
        }
        log.info("Completed expiry notifications");
    }
    
    /**
     * Send a notification to a user via FCM
     * @param userId The ID of the user to send the notification to
     * @param title The title of the notification
     * @param body The body of the notification
     */
    @Override
    public void sendNotification(String userId, String title, String body) {
        // In a real implementation, this would send an FCM notification
        // For now, we'll just log the notification
        log.info("Notification sent to user {}: {} - {}", userId, title, body);
    }
    
    /**
     * Get schedules that are due for dosage reminders
     * @return List of schedules that are due
     */
    @Override
    public List<Schedule> getDueSchedules() {
        // Get current time
        LocalTime currentTime = LocalTime.now();
        // Get current day of week for weekly schedules
        int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        
        // For simplicity, return all active schedules that match the current time
        // In a real implementation, you'd need more complex logic to handle
        // different frequencies (daily, weekly, etc.)
        List<Schedule> dueSchedules = scheduleRepository.findByTimeOfDayAndIsActiveTrue(currentTime);
        log.debug("Found {} schedules due at current time: {}", dueSchedules.size(), currentTime);
        return dueSchedules;
    }
    
    /**
     * Helper method to get medicine name by ID
     * @param medicineId The ID of the medicine
     * @return The name of the medicine, or "Unknown" if not found
     */
    private String getMedicineName(java.util.UUID medicineId) {
        return medicineRepository.findById(medicineId)
                .map(Medicine::getName)
                .orElse("Unknown Medicine");
    }
}