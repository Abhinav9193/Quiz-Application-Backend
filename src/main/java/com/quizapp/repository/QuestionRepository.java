package com.quizapp.repository;

import com.quizapp.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Question entity
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    /**
     * Find all active questions by category
     * @param categoryId category ID
     * @return List of active questions
     */
    List<Question> findByCategoryIdAndActiveTrue(Long categoryId);
    
    /**
     * Find all active questions by category with pagination
     * @param categoryId category ID
     * @param pageable pagination parameters
     * @return Page of active questions
     */
    Page<Question> findByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);
    
    /**
     * Find random active questions by category (for quiz)
     * @param categoryId category ID
     * @param limit number of questions to fetch
     * @return List of random questions
     */
    @Query(value = "SELECT * FROM questions WHERE category_id = :categoryId AND active = true ORDER BY RAND() LIMIT :limit", 
           nativeQuery = true)
    List<Question> findRandomActiveQuestionsByCategory(@Param("categoryId") Long categoryId, @Param("limit") int limit);
    
    /**
     * Count active questions by category
     * @param categoryId category ID
     * @return count of active questions
     */
    long countByCategoryIdAndActiveTrue(Long categoryId);
}


