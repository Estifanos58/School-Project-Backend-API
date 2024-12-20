package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.util.JwtUtil;
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

    @GetMapping("/hello")
    public String login (){
        return "You are loged in";
    }
}


//package com.schoolproject.ChatAPP.controllers;
//
//import com.schoolproject.ChatAPP.model.User;
//import com.schoolproject.ChatAPP.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody User user) {
//        // Save the user and return response
//        userRepository.save(user);
//        return ResponseEntity.ok("User registered successfully!");
//    }
//
//    @GetMapping("/test")
//    public ResponseEntity<String> test() {
//        return ResponseEntity.ok("Test endpoint is accessible!");
//    }
//}