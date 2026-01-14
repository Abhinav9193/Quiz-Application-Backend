package com.quizapp.repository;

import com.quizapp.entity.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Notes entity
 */
@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {
    
    /**
     * Find all notes by category
     * @param categoryId category ID
     * @param pageable pagination parameters
     * @return Page of notes
     */
    Page<Notes> findByCategoryIdOrderByUploadedAtDesc(Long categoryId, Pageable pageable);
    
    /**
     * Find all notes by category
     * @param categoryId category ID
     * @return List of notes
     */
    List<Notes> findByCategoryIdOrderByUploadedAtDesc(Long categoryId);
    
    /**
     * Find all notes ordered by upload date
     * @param pageable pagination parameters
     * @return Page of notes
     */
    Page<Notes> findAllByOrderByUploadedAtDesc(Pageable pageable);
}


