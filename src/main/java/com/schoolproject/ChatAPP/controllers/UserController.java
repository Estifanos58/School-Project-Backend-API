package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.model.User;
import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.responses.UserInfoResponse;
import com.schoolproject.ChatAPP.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@CookieValue(name = "jwt", required = false) String jwtToken) {
        try {
            if (jwtToken == null || jwtToken.isEmpty()) {
                return ResponseEntity.status(401).body("Missing or invalid JWT token");
            }

            // Decode the JWT token using the same SECRET_KEY
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getSecretKey()) // Use the SECRET_KEY from JwtUtil
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            // Extract the userId from the claims
            String userId = claims.get("userId", String.class);

            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.status(400).body("Invalid user ID in token");
            }

            // Fetch user details from the database
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User with the given ID not found");
            }

            User user = userOptional.get();
            return ResponseEntity.ok(new UserInfoResponse(
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
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
