package com.medicine.tracker.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.medicine.tracker.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Implementation of ImageUploadService for handling image uploads to Cloudinary
 * Provides methods for uploading and deleting images
 */
@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {
    
    private final Cloudinary cloudinary;
    
    /**
     * Upload an image file to Cloudinary
     * @param imageFile The image file to upload
     * @return URL of the uploaded image
     * @throws IOException If an error occurs during upload
     */
    @Override
    public String uploadImage(MultipartFile imageFile) throws IOException {
        // Validate file
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be null or empty");
        }
        
        // Check file type
        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }
        
        try {
            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            
            // Return the URL of the uploaded image
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new IOException("Failed to upload image to Cloudinary", e);
        }
    }
    
    /**
     * Delete an image from Cloudinary
     * @param imageUrl The URL of the image to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return false;
        }
        
        try {
            // Extract public ID from URL (last part after the last slash, before file extension)
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId == null) {
                return false;
            }
            
            // Delete from Cloudinary
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extract the public ID from a Cloudinary image URL
     * @param imageUrl The Cloudinary image URL
     * @return The public ID extracted from the URL, or null if not found
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null) {
            return null;
        }
        
        // Example URL: https://res.cloudinary.com/.../image/upload/.../public_id.jpg
        // We need to extract the public_id part
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        int lastDotIndex = imageUrl.lastIndexOf('.');
        
        if (lastSlashIndex == -1) {
            return null;
        }
        
        if (lastDotIndex > lastSlashIndex) {
            // Remove file extension
            return imageUrl.substring(lastSlashIndex + 1, lastDotIndex);
        } else {
            // No file extension
            return imageUrl.substring(lastSlashIndex + 1);
        }
    }
}