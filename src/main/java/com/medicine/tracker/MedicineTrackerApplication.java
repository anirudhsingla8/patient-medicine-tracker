package com.medicine.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // <-- Added import
import org.springframework.web.reactive.function.client.WebClient; // <-- Added import

/**
 * Main application class for the Medicine Tracker backend
 * This Spring Boot application provides APIs for managing medicines, profiles, and schedules
 */
@SpringBootApplication
public class MedicineTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicineTrackerApplication.class, args);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}