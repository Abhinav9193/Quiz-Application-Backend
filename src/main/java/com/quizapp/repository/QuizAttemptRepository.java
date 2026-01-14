package com.quizapp.repository;

import com.quizapp.entity.QuizAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for QuizAttempt entity
 */
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    
    /**
     * Find all quiz attempts by user
     * @param userId user ID
     * @param pageable pagination parameters
     * @return Page of quiz attempts
     */
    Page<QuizAttempt> findByUserIdOrderByAttemptedAtDesc(Long userId, Pageable pageable);
    
    /**
     * Find all quiz attempts by user
     * @param userId user ID
     * @return List of quiz attempts
     */
    List<QuizAttempt> findByUserIdOrderByAttemptedAtDesc(Long userId);
    
    /**
     * Calculate average score across all quiz attempts
     * @return average percentage
     */
    @Query("SELECT AVG(qa.percentage) FROM QuizAttempt qa")
    Double findAveragePercentage();
}


