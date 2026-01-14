package com.quizapp.service;

import com.quizapp.dto.CategoryRequest;
import com.quizapp.entity.Category;
import com.quizapp.exception.InvalidInputException;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service for category operations
 */
@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Create a new category
     * @param request category request
     * @return created Category
     */
    public Category createCategory(CategoryRequest request) {
        // Check if category name already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new InvalidInputException("Category name already exists");
        }

        Category category = new Category(request.getName());
        return categoryRepository.save(category);
    }

    /**
     * Get all active categories
     * @return List of active categories
     */
    @Transactional(readOnly = true)
    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByActiveTrue();
    }

    /**
     * Get category by ID
     * @param id category ID
     * @return Category
     */
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    /**
     * Toggle category active status
     * @param id category ID
     * @return updated Category
     */
    public Category toggleCategoryStatus(Long id) {
        Category category = getCategoryById(id);
        category.setActive(!category.getActive());
        return categoryRepository.save(category);
    }
}


