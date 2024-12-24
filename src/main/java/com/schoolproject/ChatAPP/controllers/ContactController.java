package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.model.User;
import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.schoolproject.ChatAPP.requests.SearchRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:5173") // Adjust frontend URL
public class ContactController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/search")
    public ResponseEntity<?> searchContacts(@RequestBody SearchRequest request, HttpServletRequest httpRequest) {
        try {
            // Get JWT token from the cookie
            String token = jwtUtil.getJwtFromRequest(httpRequest);

            // Extract userId from JWT
            String userId = jwtUtil.extractUserId(token);

            // Validate search term
            String searchTerm = request.getSearchTerm();
            if (searchTerm == null || searchTerm.isEmpty()) {
                return ResponseEntity.badRequest().body("Search term is required.");
            }

            // Escape special characters for regex
            String sanitizedSearchTerm = searchTerm.replaceAll("[.*+?^${}()|\\[\\]\\\\]", "\\\\$&");
            Pattern regex = Pattern.compile(sanitizedSearchTerm, Pattern.CASE_INSENSITIVE);

            // Fetch contacts excluding the current user
            List<User> contacts = userRepository.findByIdNotAndFirstnameRegexOrLastnameRegexOrEmailRegex(
                    userId, regex.pattern(), regex.pattern(), regex.pattern());
            System.out.println(contacts);

            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllContacts(HttpServletRequest httpRequest) {
        try {
            // Get JWT token from the cookie
            String token = jwtUtil.getJwtFromRequest(httpRequest);

            // Extract userId from JWT
            String userId = jwtUtil.extractUserId(token);

            // Fetch all contacts excluding the current user
            List<User> contacts = userRepository.findByIdNot(userId);

            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }
}

