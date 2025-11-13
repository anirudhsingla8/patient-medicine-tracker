package com.medicine.tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for health check endpoints
 * Provides server status and basic information
 */
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class HealthCheckController {
    
    /**
     * Get server status and basic information
     * @return Server status response with timestamp
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getServerStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Medicine Tracker Backend");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
}