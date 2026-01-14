package com.quizapp.service;

import com.quizapp.dto.QuizResultResponse;
import com.quizapp.dto.QuizSubmissionRequest;
import com.quizapp.entity.Category;
import com.quizapp.entity.Question;
import com.quizapp.entity.QuizAttempt;
import com.quizapp.entity.User;
import com.quizapp.exception.InvalidInputException;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.CategoryRepository;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Service for quiz operations
 */
@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Submit quiz answers and calculate score
     * @param request quiz submission request
     * @param user current user
     * @return QuizResultResponse
     */
    public QuizResultResponse submitQuiz(QuizSubmissionRequest request, User user) {
        // Get category
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        if (!category.getActive()) {
            throw new InvalidInputException("Category is not active");
        }

        // Get all questions for this category
        List<Question> questions = questionRepository.findByCategoryIdAndActiveTrue(request.getCategoryId());
        
        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("No questions available for this category");
        }

        // Calculate score
        int score = 0;
        int totalQuestions = questions.size();

        for (Question question : questions) {
            String userAnswer = request.getAnswers().get(question.getId());
            if (userAnswer != null && userAnswer.toUpperCase().trim().equals(question.getCorrectOption().toUpperCase().trim())) {
                score++;
            }
        }

        // Calculate percentage
        double percentage = (double) score / totalQuestions * 100;

        // Save quiz attempt
        QuizAttempt quizAttempt = new QuizAttempt(
            user,
            category,
            score,
            totalQuestions,
            percentage,
            request.getTimeTakenInSeconds()
        );
        quizAttemptRepository.save(quizAttempt);

        // Build response
        return new QuizResultResponse(
            true,
            "Quiz submitted successfully",
            score,
            totalQuestions,
            percentage,
            request.getTimeTakenInSeconds()
        );
    }

    /**
     * Get quiz history for user
     * @param userId user ID
     * @param pageable pagination parameters
     * @return Page of quiz attempts
     */
    @Transactional(readOnly = true)
    public Page<QuizAttempt> getQuizHistory(Long userId, Pageable pageable) {
        return quizAttemptRepository.findByUserIdOrderByAttemptedAtDesc(userId, pageable);
    }

    /**
     * Get all quiz attempts for user
     * @param userId user ID
     * @return List of quiz attempts
     */
    @Transactional(readOnly = true)
    public List<QuizAttempt> getAllQuizAttempts(Long userId) {
        return quizAttemptRepository.findByUserIdOrderByAttemptedAtDesc(userId);
    }
}


