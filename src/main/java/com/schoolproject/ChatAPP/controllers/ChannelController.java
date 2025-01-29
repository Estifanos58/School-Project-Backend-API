package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.model.Channel;
import com.schoolproject.ChatAPP.model.User;
import com.schoolproject.ChatAPP.model.Message;
import com.schoolproject.ChatAPP.repository.ChannelRepository;
import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.requests.ChannelRequest;
import com.schoolproject.ChatAPP.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/channels")
//@CrossOrigin(origins = "http://localhost:5173") // Adjust frontend URL
public class ChannelController {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Create a new channel
    @PostMapping("/create-channel")
    public ResponseEntity<?> createChannel(@RequestBody ChannelRequest request, HttpServletRequest httpRequest) {
        try {
            System.out.println("Create Channel endpoint hit.");

            // Extract userId from JWT token
            String token = jwtUtil.getJwtFromRequest(httpRequest);
            String userId = jwtUtil.extractUserId(token);

            // Validate admin existence
            Optional<User> admin = userRepository.findById(userId);
            if (admin.isEmpty()) {
                return ResponseEntity.badRequest().body("Admin user not found");
            }

            // Validate channel name
            if (request.getName() == null || request.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Channel name is required");
            }

            // Validate members
            List<User> validMembers = userRepository.findAllById(request.getMembers());
            if (validMembers.size() != request.getMembers().size()) {
                return ResponseEntity.badRequest().body("Some members are not valid users.");
            }

            // Create new channel
            Channel newChannel = new Channel();
            newChannel.setName(request.getName());
            newChannel.setMembers(validMembers);
            newChannel.setAdmin(admin.get());

            channelRepository.save(newChannel);
            return ResponseEntity.status(201).body(newChannel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    // Get channels for a specific user
    @GetMapping("/user-channels")
    public ResponseEntity<?> getUserChannels(HttpServletRequest httpRequest) {
        try {
            // Extract userId from JWT token
            String token = jwtUtil.getJwtFromRequest(httpRequest);
            String userId = jwtUtil.extractUserId(token);

            // userid =1234567
            // all chale

            // Fetch channels where the user is either an admin or a member
            List<Channel> channels = channelRepository.findAll()
                    .stream()
                    .filter(channel ->
                            channel.getAdmin().getId().equals(userId) ||
                                    channel.getMembers().stream().anyMatch(member -> member.getId().equals(userId))
                    )
                    .collect(Collectors.toList());

            return ResponseEntity.ok(channels);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    // Get messages for a specific channel
    @GetMapping("/get-channel-messages/{channelId}")
    public ResponseEntity<?> getChannelMessages(@PathVariable String channelId) {
        try {
            // Validate channel existence
            Optional<Channel> channel = channelRepository.findById(channelId);
            if (channel.isEmpty()) {
                return ResponseEntity.status(404).body("Channel not found");
            }

            List<Message> messages = channel.get().getMessages();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
