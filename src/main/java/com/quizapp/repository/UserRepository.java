package com.quizapp.repository;

import com.quizapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email
     * @param email user email
     * @return Optional User
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     * @param email user email
     * @return true if exists
     */
    boolean existsByEmail(String email);
}


