package com.quizapp.util;

import com.quizapp.exception.FileUploadException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Utility class for file handling operations
 */
public class FileUtil {

    private static final String UPLOAD_DIR = "uploads/notes/";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String PDF_EXTENSION = ".pdf";

    /**
     * Validate PDF file
     * @param file MultipartFile to validate
     * @throws FileUploadException if validation fails
     */
    public static void validatePdfFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is required");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException("File size exceeds 10MB limit");
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals(PDF_MIME_TYPE)) {
            throw new FileUploadException("Only PDF files are allowed");
        }

        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(PDF_EXTENSION)) {
            throw new FileUploadException("File must have .pdf extension");
        }
    }

    /**
     * Save PDF file to disk
     * @param file MultipartFile to save
     * @return file path relative to upload directory
     * @throws FileUploadException if save fails
     */
    public static String savePdfFile(MultipartFile file) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : PDF_EXTENSION;
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadPath.resolve(uniqueFilename);

            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return UPLOAD_DIR + uniqueFilename;
        } catch (IOException e) {
            throw new FileUploadException("Failed to save file: " + e.getMessage(), e);
        }
    }

    /**
     * Get file path for reading
     * @param filePath relative file path from database
     * @return Path object
     */
    public static Path getFilePath(String filePath) {
        return Paths.get(filePath);
    }

    /**
     * Delete file from disk
     * @param filePath relative file path
     * @throws FileUploadException if deletion fails
     */
    public static void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new FileUploadException("Failed to delete file: " + e.getMessage(), e);
        }
    }
}


