package com.schoolproject.ChatAPP.service;

import com.schoolproject.ChatAPP.model.Channel;
import com.schoolproject.ChatAPP.model.Message;
import com.schoolproject.ChatAPP.model.User;
import com.schoolproject.ChatAPP.repository.ChannelRepository;
import com.schoolproject.ChatAPP.repository.MessageRepository;
import com.schoolproject.ChatAPP.config.WebSocketSessionManager;
import com.schoolproject.ChatAPP.repository.UserRepository;
import com.schoolproject.ChatAPP.requests.ChannelMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Autowired
    private UserRepository userRepository;

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


    public void sendChannelMessage(ChannelMessage channelMessage) {
        try {
            // Extract message details
            System.out.println("THIS CHANNEL MESSAGE"+ channelMessage);
            String sender = channelMessage.getSender();
            String channelId = channelMessage.getChannelId();
            String content = channelMessage.getContent();
            String messageType = channelMessage.getMessageType();
            String fileUrl = channelMessage.getFileUrl();
            User Sender = userRepository.findById(sender)
                    .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + sender));
            String senderName = Sender.getFirstname();

            // Save the message
            Message newMessage = new Message();
            newMessage.setSender(sender);
            newMessage.setRecipient(null);
            newMessage.setContent(content);
            newMessage.setMessageType(messageType);
            newMessage.setTimestamps(LocalDateTime.now());
            newMessage.setFileUrl(fileUrl);
            newMessage.setChannelId(channelId);
            newMessage.setSenderName(senderName);

            Message savedMessage = messageRepository.save(newMessage);

            // Update channel with new message
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

            channel.getMessages().add(savedMessage);
            channelRepository.save(channel);

            // Notify all channel members
            channel.getMembers().forEach(member -> {
                String memberSessionId = WebSocketSessionManager.getSessionId(member.getId());
                if (memberSessionId != null) {
                    messagingTemplate.convertAndSend( "/queue/channel-messages", savedMessage);
                }
            });

            // Notify the admin
            String adminSessionId = WebSocketSessionManager.getSessionId(channel.getAdmin().getId());
            if (adminSessionId != null) {
                messagingTemplate.convertAndSend( "/queue/channel-messages", savedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

