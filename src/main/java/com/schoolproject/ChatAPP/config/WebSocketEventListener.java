package com.schoolproject.ChatAPP.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = headerAccessor.getFirstNativeHeader("username"); // Get username from headers

        if (userId == null || userId.isEmpty()) {
            userId = "Anonymous"; // Fallback to anonymous
        }

        String sessionId = Objects.requireNonNull(headerAccessor.getSessionId());
        WebSocketSessionManager.addUserSession(userId, sessionId);
        System.out.println("User Connected: " + userId);
        // Assuming user ID is in the session
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        WebSocketSessionManager.removeUserSession(sessionId);
        System.out.println("User Disconnected: " + sessionId);
    }
}

