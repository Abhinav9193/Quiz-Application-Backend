package com.quizapp.exception;

/**
 * Exception thrown when input validation fails
 */
public class InvalidInputException extends RuntimeException {
    
    public InvalidInputException(String message) {
        super(message);
    }
}


