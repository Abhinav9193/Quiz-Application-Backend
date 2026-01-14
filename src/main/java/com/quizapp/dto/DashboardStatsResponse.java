package com.quizapp.dto;

/**
 * DTO for admin dashboard statistics
 */
public class DashboardStatsResponse {

    private Long totalUsers;
    private Long totalQuestions;
    private Long totalQuizAttempts;
    private Double averageScore;

    // Constructors
    public DashboardStatsResponse() {
    }

    public DashboardStatsResponse(Long totalUsers, Long totalQuestions, 
                                  Long totalQuizAttempts, Double averageScore) {
        this.totalUsers = totalUsers;
        this.totalQuestions = totalQuestions;
        this.totalQuizAttempts = totalQuizAttempts;
        this.averageScore = averageScore;
    }

    // Getters and Setters
    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Long getTotalQuizAttempts() {
        return totalQuizAttempts;
    }

    public void setTotalQuizAttempts(Long totalQuizAttempts) {
        this.totalQuizAttempts = totalQuizAttempts;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }
}


