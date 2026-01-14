package com.quizapp.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * DTO for quiz submission request
 * Maps question ID to selected answer (A, B, C, or D)
 */
public class QuizSubmissionRequest {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Answers are required")
    private Map<Long, String> answers; // questionId -> selectedOption

    @NotNull(message = "Time taken is required")
    private Integer timeTakenInSeconds;

    // Constructors
    public QuizSubmissionRequest() {
    }

    public QuizSubmissionRequest(Long categoryId, Map<Long, String> answers, Integer timeTakenInSeconds) {
        this.categoryId = categoryId;
        this.answers = answers;
        this.timeTakenInSeconds = timeTakenInSeconds;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Map<Long, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, String> answers) {
        this.answers = answers;
    }

    public Integer getTimeTakenInSeconds() {
        return timeTakenInSeconds;
    }

    public void setTimeTakenInSeconds(Integer timeTakenInSeconds) {
        this.timeTakenInSeconds = timeTakenInSeconds;
    }
}


