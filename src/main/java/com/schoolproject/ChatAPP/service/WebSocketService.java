package com.schoolproject.ChatAPP.service;

import com.schoolproject.ChatAPP.model.Channel;
import com.schoolproject.ChatAPP.model.Message;
import com.schoolproject.ChatAPP.repository.ChannelRepository;
import com.schoolproject.ChatAPP.repository.MessageRepository;
import com.schoolproject.ChatAPP.config.WebSocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private WebSocketSessionManager sessionManager;

    public void sendMessage(Message message) {
        // Save message to MongoDB
        message.setTimestamps(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        // Resolve sender and recipient session IDs
        String senderId = message.getSender().getId();  // Extract sender ID
        String recipientId = message.getRecipient().getId(); // Extract recipient ID

        String senderSessionId = sessionManager.getSessionId(senderId); // Fixed
        String recipientSessionId = sessionManager.getSessionId(recipientId); // Fixed

        // Send message to recipient
        if (recipientSessionId != null) {
            messagingTemplate.convertAndSendToUser(recipientSessionId, "/queue/messages", savedMessage);
        }

        // Send message to sender
        if (senderSessionId != null) {
            messagingTemplate.convertAndSendToUser(senderSessionId, "/queue/messages", savedMessage);
        }
    }

    public void sendChannelMessage(Message message, String channelId) {
        // Save message to MongoDB
        message.setTimestamps(LocalDateTime.now());
        message.setRecipient(null); // No specific recipient for channels
        Message savedMessage = messageRepository.save(message);

        // Add message to channel
        Channel channel = channelRepository.findById(channelId).orElseThrow();
        channel.getMessages().add(savedMessage);
        channelRepository.save(channel);

        // Send message to all channel members
        channel.getMembers().forEach(member -> {
            String memberSessionId = sessionManager.getSessionId(member.getId()); // Fixed
            if (memberSessionId != null) {
                messagingTemplate.convertAndSendToUser(memberSessionId, "/queue/channel-messages", savedMessage);
            }
        });

        // Notify the admin
        String adminSessionId = sessionManager.getSessionId(channel.getAdmin().getId()); // Fixed
        if (adminSessionId != null) {
            messagingTemplate.convertAndSendToUser(adminSessionId, "/queue/channel-messages", savedMessage);
        }
    }
}

