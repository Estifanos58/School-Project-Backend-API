package com.schoolproject.ChatAPP.controllers;

import com.schoolproject.ChatAPP.model.Message;
import com.schoolproject.ChatAPP.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService; // Inject the service

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        // Use WebSocketService to handle saving and broadcasting the message
        System.out.println("Received Message: " + message); // Log input
        webSocketService.sendMessage(message);
    }

    @MessageMapping("/chat.sendChannelMessage")
    public void sendChannelMessage(@Payload Message message, String channelId) {
        // Handle channel-based messaging
        webSocketService.sendChannelMessage(message, channelId);
    }
}
