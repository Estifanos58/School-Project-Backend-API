package com.schoolproject.ChatAPP.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.stereotype.Component;

@Component
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        String userId = event.getUser().getName(); // Assuming user ID is in the session
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        WebSocketSessionManager.addUserSession(userId, sessionId);
        System.out.println("User Connected: " + userId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        WebSocketSessionManager.removeUserSession(sessionId);
        System.out.println("User Disconnected: " + sessionId);
    }
}

