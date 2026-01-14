package com.quizapp.service;

import com.quizapp.dto.AuthResponse;
import com.quizapp.dto.LoginRequest;
import com.quizapp.dto.UserRegisterRequest;
import com.quizapp.entity.User;
import com.quizapp.exception.InvalidInputException;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.exception.UnauthorizedAccessException;
import com.quizapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication operations
 * Handles USER and ADMIN registration and login
 * (Plain-text password as per current design)
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Admin secret code from application.properties
    @Value("${admin.registration.code}")
    private String adminRegistrationCode;

    /**
     * Register a normal USER
     */
    public AuthResponse registerUser(UserRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidInputException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // plain text (current design)
        user.setRole(User.Role.USER);

        user = userRepository.save(user);

        return buildAuthResponse(user, "User registration successful");
    }

    /**
     * ✅ Register an ADMIN (requires admin code)
     */
    public AuthResponse registerAdmin(UserRegisterRequest request, String adminCode) {

        if (!adminRegistrationCode.equals(adminCode)) {
            throw new UnauthorizedAccessException("Invalid admin registration code");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidInputException("Email already registered");
        }

        User admin = new User();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(request.getPassword());
        admin.setRole(User.Role.ADMIN);

        admin = userRepository.save(admin);

        return buildAuthResponse(admin, "Admin registration successful");
    }

    /**
     * Login USER or ADMIN
     */
    public AuthResponse login(LoginRequest request, User.Role expectedRole) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        if (user.getRole() != expectedRole) {
            throw new UnauthorizedAccessException("Access denied for this role");
        }

        return buildAuthResponse(user, "Login successful");
    }

    /**
     * Helper to build AuthResponse
     */
    private AuthResponse buildAuthResponse(User user, String message) {

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().toString()
        );

        return new AuthResponse(true, message, userInfo);
    }

    public AuthResponse register(@Valid UserRegisterRequest request, User.Role role) {
        return null;
    }
}
