package com.quizapp.repository;

import com.quizapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Category entity
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find category by name
     * @param name category name
     * @return Optional Category
     */
    Optional<Category> findByName(String name);
    
    /**
     * Find all active categories
     * @return List of active categories
     */
    List<Category> findByActiveTrue();
    
    /**
     * Check if category exists by name
     * @param name category name
     * @return true if exists
     */
    boolean existsByName(String name);
}


