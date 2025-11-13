package com.medicine.tracker.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Service interface for image upload operations
 * Handles uploading images to Cloudinary and managing image URLs
 */
public interface ImageUploadService {
    
    /**
     * Upload an image file to Cloudinary
     * @param imageFile The image file to upload
     * @return URL of the uploaded image
     * @throws IOException If an error occurs during upload
     */
    String uploadImage(MultipartFile imageFile) throws IOException;
    
    /**
     * Delete an image from Cloudinary
     * @param imageUrl The URL of the image to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteImage(String imageUrl);
}