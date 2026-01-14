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
 * Controller for USER authentication endpoints
 */
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/user/auth")
public class UserAuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new USER
     * POST /api/user/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody UserRegisterRequest request
    ) {
        AuthResponse response = authService.register(request, User.Role.USER);

        Map<String, Object> result = new HashMap<>();
        result.put("success", response.isSuccess());
        result.put("message", response.getMessage());
        result.put("user", response.getUser());

        return ResponseEntity.ok(result);
    }

    /**
     * Login as USER
     * POST /api/user/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        AuthResponse response = authService.login(request, User.Role.USER);

        // ✅ USER-specific session keys
        session.setAttribute("USER_ID", response.getUser().getId());
        session.setAttribute("USER_EMAIL", response.getUser().getEmail());
        session.setAttribute("USER_NAME", response.getUser().getName());
        session.setAttribute("USER_ROLE", response.getUser().getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("success", response.isSuccess());
        result.put("message", response.getMessage());
        result.put("user", response.getUser());

        return ResponseEntity.ok(result);
    }

    /**
     * Logout USER
     * POST /api/user/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "User logout successful");

        return ResponseEntity.ok(result);
    }
}



