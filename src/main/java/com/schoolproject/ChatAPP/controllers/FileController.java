package com.schoolproject.ChatAPP.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/files/";

    @PostMapping("/upload-file")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (file == null || file.isEmpty()) {
                response.put("message", "File is required");
                return response;
            }

            // Create directory with timestamp
            String dateDir = String.valueOf(System.currentTimeMillis());
            File fileDir = new File(UPLOAD_DIR + dateDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            // Save file to the server
            String filePath = fileDir.getAbsolutePath() + "/" + file.getOriginalFilename();
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(file.getBytes());
            }

            // Prepare response
            response.put("filePath", filePath);
            response.put("message", "File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            response.put("message", "Internal Server Error");
        }

        return response;
    }
}
