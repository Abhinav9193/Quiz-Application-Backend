package com.quizapp.service;

import com.quizapp.entity.Category;
import com.quizapp.entity.Notes;
import com.quizapp.exception.InvalidInputException;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.CategoryRepository;
import com.quizapp.repository.NotesRepository;
import com.quizapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.List;

/**
 * Service for notes operations
 */
@Service
@Transactional
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Upload PDF notes
     * @param file PDF file
     * @param title notes title
     * @param categoryId category ID
     * @return created Notes entity
     */
    public Notes uploadNotes(MultipartFile file, String title, Long categoryId) {
        // Validate file
        FileUtil.validatePdfFile(file);

        // Get category
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        if (!category.getActive()) {
            throw new InvalidInputException("Cannot upload notes to inactive category");
        }

        // Save file to disk
        String filePath = FileUtil.savePdfFile(file);
        String fileName = file.getOriginalFilename();

        // Create notes entity
        Notes notes = new Notes(title, fileName, filePath, category);
        return notesRepository.save(notes);
    }

    /**
     * Get notes by category with pagination
     * @param categoryId category ID
     * @param pageable pagination parameters
     * @return Page of notes
     */
    @Transactional(readOnly = true)
    public Page<Notes> getNotesByCategory(Long categoryId, Pageable pageable) {
        return notesRepository.findByCategoryIdOrderByUploadedAtDesc(categoryId, pageable);
    }

    /**
     * Get all notes with pagination
     * @param pageable pagination parameters
     * @return Page of notes
     */
    @Transactional(readOnly = true)
    public Page<Notes> getAllNotes(Pageable pageable) {
        return notesRepository.findAllByOrderByUploadedAtDesc(pageable);
    }

    /**
     * Get notes by ID
     * @param id notes ID
     * @return Notes entity
     */
    @Transactional(readOnly = true)
    public Notes getNotesById(Long id) {
        return notesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notes not found with id: " + id));
    }

    /**
     * Get file path for download
     * @param id notes ID
     * @return Path to file
     */
    @Transactional(readOnly = true)
    public Path getNotesFilePath(Long id) {
        Notes notes = getNotesById(id);
        return FileUtil.getFilePath(notes.getFilePath());
    }
}


