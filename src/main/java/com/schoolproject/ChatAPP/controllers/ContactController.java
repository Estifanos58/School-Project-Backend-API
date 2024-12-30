package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.model.Message;
import com.schoolproject.ChatAPP.model.User;
import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.repository.MessageRepository;
import com.schoolproject.ChatAPP.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.schoolproject.ChatAPP.requests.SearchRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:5173") // Adjust frontend URL
public class ContactController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Search contacts
    @PostMapping("/search")
    public ResponseEntity<?> searchContacts(@RequestBody SearchRequest request, HttpServletRequest httpRequest) {
        try {
            // Get JWT token
            String token = jwtUtil.getJwtFromRequest(httpRequest);
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

            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    // Get all contacts
    @GetMapping("/get-all-contacts")
    public ResponseEntity<?> getAllContacts(HttpServletRequest httpRequest) {
        try {
            // Get JWT token
            String token = jwtUtil.getJwtFromRequest(httpRequest);
            String userId = jwtUtil.extractUserId(token);

            // Fetch all contacts excluding the current user
            List<User> contacts = userRepository.findByIdNot(userId);

            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    // Get contacts for DM list
    @GetMapping("/dm-list")
    public ResponseEntity<?> getContactsForDMList(HttpServletRequest httpRequest) {
        try {
            // Get JWT token
            String token = jwtUtil.getJwtFromRequest(httpRequest);
            String userId = jwtUtil.extractUserId(token);

            // Fetch all messages involving the user
            List<Message> allMessages = messageRepository.findBySenderOrRecipientOrderByTimestampsDesc(userId, userId);

            // Group by contactId and fetch latest messages
            Map<String, Message> lastMessagesMap = new HashMap<>();
            for (Message message : allMessages) {
                String contactId = message.getSender().equals(userId) ? message.getRecipient() : message.getSender();
                if (!lastMessagesMap.containsKey(contactId) ||
                        lastMessagesMap.get(contactId).getTimestamps().before(message.getTimestamps())) {
                    lastMessagesMap.put(contactId, message);
                }
            }

            // Fetch contact details
            List<String> contactIds = new ArrayList<>(lastMessagesMap.keySet());
            List<User> contacts = userRepository.findAllById(contactIds);

            // Combine contact info with last message time
            List<Map<String, Object>> response = contacts.stream().map(contact -> {
                        Message lastMessage = lastMessagesMap.get(contact.getId());
                        Map<String, Object> contactInfo = new HashMap<>();
                        contactInfo.put("id", contact.getId());
                        contactInfo.put("email", contact.getEmail());
                        contactInfo.put("firstName", contact.getFirstname());
                        contactInfo.put("lastName", contact.getLastname());
                        contactInfo.put("image", contact.getImage());
                        contactInfo.put("color", contact.getColor());
                        contactInfo.put("lastMessageTime", lastMessage.getTimestamps());
                        return contactInfo;
                    }).sorted(Comparator.comparing(c -> ((Date) c.get("lastMessageTime")), Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }
}
