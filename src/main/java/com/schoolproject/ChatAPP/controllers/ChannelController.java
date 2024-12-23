package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.repository.ChannelRepository;
import com.schoolproject.ChatAPP.model.User;
import com.schoolproject.ChatAPP.model.Channel;
import com.schoolproject.ChatAPP.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createChannel(@RequestBody ChannelRequest request, @RequestAttribute("userId") String userId) {
        try {
            Optional<User> admin = userRepository.findById(userId);
            if (admin.isEmpty()) {
                return ResponseEntity.badRequest().body("Admin user not found");
            }

            List<User> validMembers = userRepository.findAllById(request.getMembers());
            if (validMembers.size() != request.getMembers().size()) {
                return ResponseEntity.badRequest().body("Some members are not valid users.");
            }

            Channel newChannel = new Channel();
            newChannel.setName(request.getName());
            newChannel.setMembers(request.getMembers());
            newChannel.setAdmin(userId);

            channelRepository.save(newChannel);
            return ResponseEntity.status(201).body(newChannel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/user-channels")
    public ResponseEntity<?> getUserChannels(@RequestAttribute("userId") String userId) {
        try {
            List<Channel> channels = channelRepository.findByAdminOrMembersContaining(userId, userId);
            return ResponseEntity.ok(channels);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/{channelId}/messages")
    public ResponseEntity<?> getChannelMessages(@PathVariable String channelId) {
        try {
            Optional<Channel> channel = channelRepository.findById(channelId);
            if (channel.isEmpty()) {
                return ResponseEntity.status(404).body("Channel not found");
            }

            List<Message> messages = channel.get().getMessages();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}