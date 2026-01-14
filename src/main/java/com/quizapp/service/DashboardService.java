package com.quizapp.service;

import com.quizapp.dto.DashboardStatsResponse;
import com.quizapp.repository.QuizAttemptRepository;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for admin dashboard statistics
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    /**
     * Get dashboard statistics
     * @return DashboardStatsResponse
     */
    public DashboardStatsResponse getDashboardStats() {
        Long totalUsers = userRepository.count();
        Long totalQuestions = questionRepository.count();
        Long totalQuizAttempts = quizAttemptRepository.count();
        Double averageScore = quizAttemptRepository.findAveragePercentage();

        // Handle null average score
        if (averageScore == null) {
            averageScore = 0.0;
        }

        return new DashboardStatsResponse(
            totalUsers,
            totalQuestions,
            totalQuizAttempts,
            averageScore
        );
    }
}


