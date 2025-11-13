package com.medicine.tracker.exception;

/**
 * Exception thrown when a requested resource is not found
 * Used for 404 errors when entities don't exist
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}