package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.requests.LogOutRequest;
import com.schoolproject.ChatAPP.requests.LoginRequest;
import com.schoolproject.ChatAPP.responses.LoginResponse;
import com.schoolproject.ChatAPP.responses.SignUpResponse;
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
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User userRequest, HttpServletResponse response) {
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

            // Set JWT as a cookie
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
//            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 1 day
            response.addCookie(jwtCookie);

            // Response
            return ResponseEntity.status(201).body(
                    new SignUpResponse(savedUser.getId(), savedUser.getEmail(), savedUser.isProfileSetup())
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

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@RequestBody LogOutRequest logoutRequest, HttpServletResponse response) {
        try {
            Optional<User> userOptional = userRepository.findById(logoutRequest.getUserId());

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            // Clear the JWT cookie
            Cookie jwtCookie = new Cookie("jwt", "");
            jwtCookie.setMaxAge(1); // Cookie expires immediately
            jwtCookie.setSecure(true); // Use secure cookies
            jwtCookie.setHttpOnly(true); // HttpOnly for security
            jwtCookie.setPath("/"); // Cookie path
            response.addCookie(jwtCookie);

            return ResponseEntity.ok("Logout Successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
