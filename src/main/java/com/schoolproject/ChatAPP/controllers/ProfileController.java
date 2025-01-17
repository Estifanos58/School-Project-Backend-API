package com.schoolproject.ChatAPP.controllers;


import com.schoolproject.ChatAPP.model.User;
import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.responses.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;



@RestController
@RequestMapping("/api/user")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint to upload a profile image.
     *
     * @param file   Multipart file to be uploaded.
     * @param userId The ID of the user whose profile image is being updated.
     * @return A response containing the file path of the uploaded image.
     *
     */

    private static final String UPLOAD_DIR = "uploads/profiles/";

    @PutMapping("/add-profile-image")
    public ProfileResponse uploadProfileImage(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
        if (file == null || file.isEmpty()) {
            return new ProfileResponse("File is required", 400);
        }

        try {
            // Static base directory for uploads
            String baseDir = "C:/uploads/profiles/";
            String dateDir = String.valueOf(System.currentTimeMillis());
            File directory = new File(baseDir + dateDir);

            // Create directory if it doesn't exist
            if (!directory.exists()) {
                boolean dirsCreated = directory.mkdirs();
                if (!dirsCreated) {
                    return new ProfileResponse("Failed to create upload directory", 500);
                }
            }

            // Generate unique file path
            String fileName = dateDir + "_" + file.getOriginalFilename();
            File destinationFile = new File(directory, fileName);

            // Save file to the created directory
            file.transferTo(destinationFile);

            // Update user's profile image in the database
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return new ProfileResponse("User not found", 404);
            }

            User user = optionalUser.get();
            user.setImage(destinationFile.getAbsolutePath()); // Save absolute file path
            userRepository.save(user);

            // Return the file path in response
            return new ProfileResponse("Profile image updated successfully", 200, destinationFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            return new ProfileResponse("Internal Server Error: " + e.getMessage(), 500);
        }
    }

}