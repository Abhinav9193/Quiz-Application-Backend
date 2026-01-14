package com.quizapp.controller;

import com.quizapp.dto.CategoryRequest;
import com.quizapp.dto.DashboardStatsResponse;
import com.quizapp.dto.QuestionRequest;
import com.quizapp.entity.Category;
import com.quizapp.entity.Question;
import com.quizapp.exception.UnauthorizedAccessException;
import com.quizapp.service.CategoryService;
import com.quizapp.service.DashboardService;
import com.quizapp.service.NotesService;
import com.quizapp.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for ADMIN operations
 * All endpoints require ADMIN role
 */
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotesService notesService;

    @Autowired
    private DashboardService dashboardService;

    /**
     * Check if user is authenticated as ADMIN
     */
    private void checkAdminAuth(HttpSession session) {
        Object roleObj = session.getAttribute("userRole");

        if (roleObj == null) {
            throw new UnauthorizedAccessException("No active session. Please login again.");
        }

        String role = roleObj.toString();
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedAccessException("Admin access required");
        }
    }

    /**
     * Get dashboard statistics
     */
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(HttpSession session) {
        checkAdminAuth(session);

        DashboardStatsResponse stats = dashboardService.getDashboardStats();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", stats);

        return ResponseEntity.ok(result);
    }

    /**
     * Create a new category
     */
    @PostMapping("/categories")
    public ResponseEntity<Map<String, Object>> createCategory(
            @Valid @RequestBody CategoryRequest request,
            HttpSession session
    ) {
        checkAdminAuth(session);

        Category category = categoryService.createCategory(request);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Category created successfully");
        result.put("data", category);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Get all active categories
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getAllCategories(HttpSession session) {
        checkAdminAuth(session);

        List<Category> categories = categoryService.getAllActiveCategories();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", categories);

        return ResponseEntity.ok(result);
    }

    /**
     * Toggle category status
     */
    @PutMapping("/categories/{id}/toggle")
    public ResponseEntity<Map<String, Object>> toggleCategoryStatus(
            @PathVariable Long id,
            HttpSession session
    ) {
        checkAdminAuth(session);

        Category category = categoryService.toggleCategoryStatus(id);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Category status updated");
        result.put("data", category);

        return ResponseEntity.ok(result);
    }

    /**
     * Create a new question
     */
    @PostMapping("/questions")
    public ResponseEntity<Map<String, Object>> createQuestion(
            @Valid @RequestBody QuestionRequest request,
            HttpSession session
    ) {
        checkAdminAuth(session);

        Question question = questionService.createQuestion(request);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Question created successfully");
        result.put("data", question);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Get questions by category with pagination
     */
    @GetMapping("/questions")
    public ResponseEntity<Map<String, Object>> getQuestions(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session
    ) {
        checkAdminAuth(session);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Question> questions = questionService.getQuestionsByCategory(categoryId, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", questions.getContent());
        result.put("totalElements", questions.getTotalElements());
        result.put("totalPages", questions.getTotalPages());
        result.put("currentPage", page);

        return ResponseEntity.ok(result);
    }

    /**
     * Disable question
     */
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Map<String, Object>> disableQuestion(
            @PathVariable Long id,
            HttpSession session
    ) {
        checkAdminAuth(session);

        questionService.disableQuestion(id);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Question disabled successfully");

        return ResponseEntity.ok(result);
    }

    /**
     * Upload PDF notes
     */
    @PostMapping("/notes")
    public ResponseEntity<Map<String, Object>> uploadNotes(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("categoryId") Long categoryId,
            HttpSession session
    ) {
        checkAdminAuth(session);

        var notes = notesService.uploadNotes(file, title, categoryId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Notes uploaded successfully");
        result.put("data", notes);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Get all notes with pagination
     */
    @GetMapping("/notes")
    public ResponseEntity<Map<String, Object>> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session
    ) {
        checkAdminAuth(session);

        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").descending());
        Page<com.quizapp.entity.Notes> notes = notesService.getAllNotes(pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", notes.getContent());
        result.put("totalElements", notes.getTotalElements());
        result.put("totalPages", notes.getTotalPages());
        result.put("currentPage", page);

        return ResponseEntity.ok(result);
    }
}



