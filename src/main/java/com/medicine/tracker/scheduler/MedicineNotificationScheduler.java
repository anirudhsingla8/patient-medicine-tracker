package com.medicine.tracker.scheduler;

import com.medicine.tracker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled component for automated medicine notifications
 * Runs scheduled tasks for dosage reminders and expiry alerts
 */
@Component
@RequiredArgsConstructor
public class MedicineNotificationScheduler {
    
    private final NotificationService notificationService;
    
    /**
     * Scheduled task to send dosage reminders
     * Runs every minute to check for due schedules
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    public void sendDosageReminders() {
        notificationService.sendDosageReminders();
    }
    
    /**
     * Scheduled task to send expiry notifications
     * Runs daily at 9:00 AM to check for expiring medicines
     */
    @Scheduled(cron = "0 0 9 * * *") // Run daily at 9:00 AM
    public void sendExpiryNotifications() {
        notificationService.sendExpiryNotifications();
    }
}