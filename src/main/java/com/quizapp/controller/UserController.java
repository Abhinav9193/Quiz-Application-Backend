package com.quizapp.controller;

import com.quizapp.dto.QuizResultResponse;
import com.quizapp.dto.QuizSubmissionRequest;
import com.quizapp.entity.Category;
import com.quizapp.entity.Question;
import com.quizapp.entity.QuizAttempt;
import com.quizapp.entity.User;
import com.quizapp.exception.UnauthorizedAccessException;
import com.quizapp.repository.UserRepository;
import com.quizapp.service.CategoryService;
import com.quizapp.service.NotesService;
import com.quizapp.service.QuestionService;
import com.quizapp.service.QuizService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for USER operations
 * All endpoints require USER role
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private NotesService notesService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Check if user is authenticated as USER
     */
    private void checkUserAuth(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (role == null || !role.equals("USER")) {
            throw new UnauthorizedAccessException("User access required");
        }
    }

    /**
     * Get current user ID from session
     */
    private Long getCurrentUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedAccessException("User not authenticated");
        }
        return userId;
    }

    /**
     * Get all active categories
     * GET /api/user/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getAllCategories(HttpSession session) {
        checkUserAuth(session);
        
        List<Category> categories = categoryService.getAllActiveCategories();
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", categories);
        
        return ResponseEntity.ok(result);
    }

    /**
     * Get quiz questions by category
     * GET /api/user/quiz/questions?categoryId={id}&limit={limit}
     */
    @GetMapping("/quiz/questions")
    public ResponseEntity<Map<String, Object>> getQuizQuestions(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session) {
        checkUserAuth(session);
        
        List<Question> questions = questionService.getRandomQuestionsForQuiz(categoryId, limit);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", questions);
        result.put("totalQuestions", questions.size());
        
        return ResponseEntity.ok(result);
    }

    /**
     * Submit quiz answers
     * POST /api/user/quiz/submit
     */
    @PostMapping("/quiz/submit")
    public ResponseEntity<Map<String, Object>> submitQuiz(@Valid @RequestBody QuizSubmissionRequest request, HttpSession session) {
        checkUserAuth(session);
        
        Long userId = getCurrentUserId(session);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UnauthorizedAccessException("User not found"));
        
        QuizResultResponse response = quizService.submitQuiz(request, user);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", response.isSuccess());
        result.put("message", response.getMessage());
        result.put("score", response.getScore());
        result.put("totalQuestions", response.getTotalQuestions());
        result.put("percentage", response.getPercentage());
        result.put("timeTakenInSeconds", response.getTimeTakenInSeconds());
        
        return ResponseEntity.ok(result);
    }

    /**
     * Get quiz history with pagination
     * GET /api/user/quiz/history?page={page}&size={size}
     */
    @GetMapping("/quiz/history")
    public ResponseEntity<Map<String, Object>> getQuizHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        checkUserAuth(session);
        
        Long userId = getCurrentUserId(session);
        Pageable pageable = PageRequest.of(page, size, Sort.by("attemptedAt").descending());
        Page<QuizAttempt> attempts = quizService.getQuizHistory(userId, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", attempts.getContent());
        result.put("totalElements", attempts.getTotalElements());
        result.put("totalPages", attempts.getTotalPages());
        result.put("currentPage", page);
        
        return ResponseEntity.ok(result);
    }

    /**
     * Get all notes by category
     * GET /api/user/notes?categoryId={id}&page={page}&size={size}
     */
    @GetMapping("/notes")
    public ResponseEntity<Map<String, Object>> getNotes(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        checkUserAuth(session);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").descending());
        Page<com.quizapp.entity.Notes> notes = notesService.getNotesByCategory(categoryId, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", notes.getContent());
        result.put("totalElements", notes.getTotalElements());
        result.put("totalPages", notes.getTotalPages());
        result.put("currentPage", page);
        
        return ResponseEntity.ok(result);
    }

    /**
     * Download notes PDF
     * GET /api/user/notes/{id}/download
     */
    @GetMapping("/notes/{id}/download")
    public ResponseEntity<Resource> downloadNotes(@PathVariable Long id, HttpSession session) {
        checkUserAuth(session);
        
        try {
            Path filePath = notesService.getNotesFilePath(id);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            } else {
                throw new RuntimeException("File not found or not readable");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }
}

