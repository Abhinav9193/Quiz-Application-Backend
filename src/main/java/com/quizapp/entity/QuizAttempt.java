package com.quizapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * QuizAttempt Entity - Stores quiz results and performance data
 * Tracks user quiz attempts with scores and timing
 */
@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category is required")
    private Category category;

    @Column(nullable = false)
    @NotNull(message = "Score is required")
    private Integer score;

    @Column(name = "total_questions", nullable = false)
    @NotNull(message = "Total questions is required")
    private Integer totalQuestions;

    @Column(nullable = false)
    @NotNull(message = "Percentage is required")
    private Double percentage;

    @Column(name = "time_taken_in_seconds", nullable = false)
    @NotNull(message = "Time taken is required")
    private Integer timeTakenInSeconds;

    @Column(name = "attempted_at", nullable = false, updatable = false)
    private LocalDateTime attemptedAt;

    @PrePersist
    protected void onCreate() {
        attemptedAt = LocalDateTime.now();
    }

    // Constructors
    public QuizAttempt() {
    }

    public QuizAttempt(User user, Category category, Integer score, 
                      Integer totalQuestions, Double percentage, Integer timeTakenInSeconds) {
        this.user = user;
        this.category = category;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.timeTakenInSeconds = timeTakenInSeconds;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(LocalDateTime attemptedAt) {
        this.attemptedAt = attemptedAt;
    }
}


