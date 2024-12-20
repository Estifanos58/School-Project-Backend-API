package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.requests.LoginRequest;
import com.schoolproject.ChatAPP.responses.LoginResponse;
import com.schoolproject.ChatAPP.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.schoolproject.ChatAPP.model.User;

import java.util.Optional;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/auth")
@Validated

public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User userRequest) {
        try {
            // Check for required fields
            if (userRequest.getEmail() == null || userRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body("Email and Password are required.");
            }

            // Check for duplicate email
            Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body("Email already exists.");
            }

            // Hash the password
            String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
            User newUser = new User();
            newUser.setEmail(userRequest.getEmail());
            newUser.setPassword(hashedPassword);

            // Save user to MongoDB
            User savedUser = userRepository.save(newUser);

            // Generate JWT token
            String jwtToken = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());

            // Response
            return ResponseEntity.status(201).body(
                    new AuthResponse(savedUser.getId(), savedUser.getEmail(), jwtToken)
            );
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            // Check for required fields
            if (email == null || password == null) {
                return ResponseEntity.badRequest().body("Email and Password are required.");
            }

            // Find the user by email
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found.");
            }

            User user = userOptional.get();

            // Verify password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body("Password is incorrect.");
            }

            // Generate JWT token
            String jwtToken = jwtUtil.generateToken(email, user.getId());

            // Set JWT as a cookie
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 1 day
            response.addCookie(jwtCookie);

            // Return user details
            return ResponseEntity.ok(new LoginResponse(
                    user.getId(),
                    user.getEmail(),
                    user.isProfileSetup(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getImage(),
                    user.getColor()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }


    // Inner class for response structure
    static class AuthResponse {
        public String id;
        public String email;
        public String jwt;

        public AuthResponse(String id, String email, String jwt) {
            this.id = id;
            this.email = email;
            this.jwt = jwt;
        }
    }

}
