package com.medicine.tracker.exception;

/**
 * Exception thrown when a user is not authorized to perform an action
 * Used for 401/403 errors when access is denied
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}