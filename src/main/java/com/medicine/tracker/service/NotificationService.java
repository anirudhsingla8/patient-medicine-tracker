package com.medicine.tracker.service;

import com.medicine.tracker.model.entity.Schedule;

import java.util.List;

/**
 * Service interface for notification operations
 * Handles automated notifications for dosage reminders and alerts
 */
public interface NotificationService {
    
    /**
     * Send dosage reminder notifications for schedules that are due
     */
    void sendDosageReminders();
    
    /**
     * Send notifications for medicines that are expiring soon
     */
    void sendExpiryNotifications();
    
    /**
     * Send a notification to a user via FCM
     * @param userId The ID of the user to send the notification to
     * @param title The title of the notification
     * @param body The body of the notification
     */
    void sendNotification(String userId, String title, String body);
    
    /**
     * Get schedules that are due for dosage reminders
     * @return List of schedules that are due
     */
    List<Schedule> getDueSchedules();
}