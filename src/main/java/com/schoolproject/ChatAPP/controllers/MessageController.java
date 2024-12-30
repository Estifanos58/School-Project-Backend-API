package com.schoolproject.ChatAPP.controllers;


import com.schoolproject.ChatAPP.model.Message;
import com.schoolproject.ChatAPP.repository.MessageRepository;
import com.schoolproject.ChatAPP.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173") // Adjust frontend URL
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/get-messages")
    public ResponseEntity<?> getMessages(@RequestBody MessageRequest request, HttpServletRequest httpRequest) {
        try {
            // Extract user ID from JWT token
            String token = jwtUtil.getJwtFromRequest(httpRequest);
            String user1 = jwtUtil.extractUserId(token);
            String user2 = request.getId();

            // Validate user IDs
            if (user1 == null || user2 == null || user1.isEmpty() || user2.isEmpty()) {
                return ResponseEntity.badRequest().body("Both user IDs are required.");
            }

            // Fetch messages between the two users, sorted by timestamp
            List<Message> messages = messageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByTimestamps(
                    user1, user2, user1, user2
            );

            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    // Request DTO
    public static class MessageRequest {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

