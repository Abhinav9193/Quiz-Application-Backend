package com.quizapp.exception;

/**
 * Exception thrown when user tries to access resources without proper authorization
 */
public class UnauthorizedAccessException extends RuntimeException {
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}


