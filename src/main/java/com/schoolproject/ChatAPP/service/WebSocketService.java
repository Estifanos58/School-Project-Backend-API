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
        try {
            // Log message details before saving
            System.out.println("Saving Message: " + message);

            message.setTimestamps(LocalDateTime.now());
            Message savedMessage = messageRepository.save(message);

            System.out.println("Saved Message ID: " + savedMessage.getId());

            // Resolve sender and recipient session IDs
            String senderId = message.getSender();
            String recipientId = message.getRecipient();

            String senderSessionId = WebSocketSessionManager.getSessionId(senderId);
            String recipientSessionId = WebSocketSessionManager.getSessionId(recipientId);

            // Send message to recipient
            if (recipientSessionId != null) {
                System.out.println("TRYING TO SEND THE MESSAGE TO RECEIVER");
                messagingTemplate.convertAndSend("/topic/public", savedMessage);

            }

            // Send message to sender
            if (senderSessionId != null) {
                System.out.println("TRYING TO SEND THE MESSAGE TO SENDER");
                messagingTemplate.convertAndSend("/topic/public", savedMessage);
                System.out.println("MESSAGE SENT TO USER AT"+ message.getSender());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendChannelMessage(Message message, String channelId) {
        System.out.println("TRYING TO SEND TO CHANNEL ");
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
            String memberSessionId = WebSocketSessionManager.getSessionId(member.getId()); // Fixed
            if (memberSessionId != null) {
                messagingTemplate.convertAndSendToUser(memberSessionId, "/queue/channel-messages", savedMessage);
            }
        });

        // Notify the admin
        String adminSessionId = WebSocketSessionManager.getSessionId(channel.getAdmin().getId()); // Fixed
        if (adminSessionId != null) {
            messagingTemplate.convertAndSendToUser(adminSessionId, "/queue/channel-messages", savedMessage);
        }
    }
}

