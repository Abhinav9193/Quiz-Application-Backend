package com.quizapp.dto;

/**
 * DTO for quiz result response
 */
public class QuizResultResponse {

    private boolean success;
    private String message;
    private Integer score;
    private Integer totalQuestions;
    private Double percentage;
    private Integer timeTakenInSeconds;

    // Constructors
    public QuizResultResponse() {
    }

    public QuizResultResponse(boolean success, String message, Integer score, 
                             Integer totalQuestions, Double percentage, Integer timeTakenInSeconds) {
        this.success = success;
        this.message = message;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.timeTakenInSeconds = timeTakenInSeconds;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Integer getTimeTakenInSeconds() {
        return timeTakenInSeconds;
    }

    public void setTimeTakenInSeconds(Integer timeTakenInSeconds) {
        this.timeTakenInSeconds = timeTakenInSeconds;
    }
}


