package com.quizapp.controller;

import com.quizapp.dto.AuthResponse;
import com.quizapp.dto.LoginRequest;
import com.quizapp.dto.UserRegisterRequest;
import com.quizapp.entity.User;
import com.quizapp.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for ADMIN authentication endpoints
 */
@RestController
@RequestMapping("/api/admin/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminAuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new ADMIN (requires admin code)
     * POST /api/admin/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody UserRegisterRequest request,
            @RequestParam("adminCode") String adminCode
    ) {
        AuthResponse response = authService.registerAdmin(request, adminCode);

        Map<String, Object> result = new HashMap<>();
        result.put("success", response.isSuccess());
        result.put("message", response.getMessage());
        result.put("admin", response.getUser());

        return ResponseEntity.ok(result);
    }

    /**
     * Login as ADMIN
     * POST /api/admin/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        AuthResponse response = authService.login(request, User.Role.ADMIN);

        // ✅ Admin-specific session keys
        session.setAttribute("ADMIN_ID", response.getUser().getId());
        session.setAttribute("ADMIN_EMAIL", response.getUser().getEmail());
        session.setAttribute("ADMIN_NAME", response.getUser().getName());
        session.setAttribute("ADMIN_ROLE", response.getUser().getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("success", response.isSuccess());
        result.put("message", response.getMessage());
        result.put("admin", response.getUser());

        return ResponseEntity.ok(result);
    }

    /**
     * Logout ADMIN
     * POST /api/admin/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Admin logout successful");

        return ResponseEntity.ok(result);
    }
}



