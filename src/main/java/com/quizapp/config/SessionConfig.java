package com.quizapp.config;

import org.springframework.context.annotation.Configuration;

/**
 * Session Configuration
 * HttpSession is configured via application.properties
 * Session timeout: 30 minutes (configured in application.properties)
 */
@Configuration
public class SessionConfig {
    // Session configuration is handled by application.properties
    // No additional beans needed for standard HttpSession
}
