package com.medicine.tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Configuration class for logging capabilities
 * Sets up request logging for debugging and monitoring
 */
@Configuration
public class LoggingConfig {
    
    /**
     * Create a request logging filter to log incoming requests
     * @return CommonsRequestLoggingFilter configured for request logging
     */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(1000);
        return loggingFilter;
    }
}