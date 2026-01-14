package com.quizapp.service;

import com.quizapp.dto.QuestionRequest;
import com.quizapp.entity.Category;
import com.quizapp.entity.Question;
import com.quizapp.exception.InvalidInputException;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.CategoryRepository;
import com.quizapp.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service for question operations
 */
@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Create a new question
     * @param request question request
     * @return created Question
     */
    public Question createQuestion(QuestionRequest request) {
        // Validate correct option
        String correctOption = request.getCorrectOption().toUpperCase().trim();
        if (!correctOption.matches("[ABCD]")) {
            throw new InvalidInputException("Correct option must be A, B, C, or D");
        }

        // Get category
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        if (!category.getActive()) {
            throw new InvalidInputException("Cannot add questions to inactive category");
        }

        // Create question
        Question question = new Question(
            request.getQuestionText(),
            request.getOptionA(),
            request.getOptionB(),
            request.getOptionC(),
            request.getOptionD(),
            correctOption,
            category
        );

        return questionRepository.save(question);
    }

    /**
     * Get questions by category with pagination
     * @param categoryId category ID
     * @param pageable pagination parameters
     * @return Page of questions
     */
    @Transactional(readOnly = true)
    public Page<Question> getQuestionsByCategory(Long categoryId, Pageable pageable) {
        return questionRepository.findByCategoryIdAndActiveTrue(categoryId, pageable);
    }

    /**
     * Get random questions for quiz
     * @param categoryId category ID
     * @param limit number of questions
     * @return List of random questions
     */
    @Transactional(readOnly = true)
    public List<Question> getRandomQuestionsForQuiz(Long categoryId, int limit) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        if (!category.getActive()) {
            throw new InvalidInputException("Category is not active");
        }

        List<Question> questions = questionRepository.findRandomActiveQuestionsByCategory(categoryId, limit);
        
        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("No questions available for this category");
        }

        return questions;
    }

    /**
     * Disable question (soft delete)
     * @param id question ID
     */
    public void disableQuestion(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        
        question.setActive(false);
        questionRepository.save(question);
    }

    /**
     * Get question by ID
     * @param id question ID
     * @return Question
     */
    @Transactional(readOnly = true)
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
    }
}


