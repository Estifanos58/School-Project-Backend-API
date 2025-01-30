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
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User userRequest, HttpServletResponse response) {
        try {
            if (userRequest.getEmail() == null || userRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body("Email and Password are required.");
            }

            Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body("Email already exists.");
            }

            String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
            User newUser = new User();
            newUser.setEmail(userRequest.getEmail());
            newUser.setPassword(hashedPassword);

            User savedUser = userRepository.save(newUser);

            String jwtToken = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());

            // Set JWT Cookie with SameSite=None
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400); // 1 day
            jwtCookie.setAttribute("SameSite", "None"); // Required for cross-origin cookies

            response.addCookie(jwtCookie);

            return ResponseEntity.status(201).body(
                    new SignUpResponse(savedUser.getId(), savedUser.getEmail(), savedUser.isProfileSetup())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }
//this is for tthe login route
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
// looking for email
            if (email == null || password == null) {
                return ResponseEntity.badRequest().body("Email and Password are required.");
            }

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found.");
            }

            User user = userOptional.get();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body("Password is incorrect.");
            }

            String jwtToken = jwtUtil.generateToken(email, user.getId());


            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400);
            jwtCookie.setAttribute("SameSite", "None");

            response.addCookie(jwtCookie);

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


            Cookie jwtCookie = new Cookie("jwt", "");
            jwtCookie.setMaxAge(0); // Expires immediately
            jwtCookie.setSecure(true);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setAttribute("SameSite", "None");

            response.addCookie(jwtCookie);

            return ResponseEntity.ok("Logout Successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
