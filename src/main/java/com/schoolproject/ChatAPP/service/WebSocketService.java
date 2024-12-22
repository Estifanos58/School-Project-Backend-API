package com.schoolproject.ChatAPP.service;


//import com.example.websocket.model.Message;
//import com.example.websocket.model.Channel;
//import com.example.websocket.repository.MessageRepository;
//import com.example.websocket.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    public void sendMessage(Message message) {
        // Save message to MongoDB
        message.setTimestamp(new Date());
        Message savedMessage = messageRepository.save(message);

        // Send message to recipient and sender
        messagingTemplate.convertAndSendToUser(
                message.getRecipient(), "/queue/messages", savedMessage);
        messagingTemplate.convertAndSendToUser(
                message.getSender(), "/queue/messages", savedMessage);
    }

    public void sendChannelMessage(Message message, String channelId) {
        // Save message to MongoDB
        message.setTimestamp(new Date());
        message.setRecipient(null); // No specific recipient for channels
        Message savedMessage = messageRepository.save(message);

        // Add message to channel
        Channel channel = channelRepository.findById(channelId).orElseThrow();
        channel.getMessages().add(savedMessage);
        channelRepository.save(channel);

        // Send message to all channel members
        channel.getMembers().forEach(member -> {
            messagingTemplate.convertAndSendToUser(
                    member.getId(), "/queue/channel-messages", savedMessage);
        });

        // Notify the admin
        messagingTemplate.convertAndSendToUser(
                channel.getAdmin().getId(), "/queue/channel-messages", savedMessage);
    }
}
