package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.model.User;
import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.responses.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable String userId) {
        try {
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
