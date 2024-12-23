package com.schoolproject.ChatAPP.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;


@Component
public class WebSocketSessionManager {

    // Map to track user IDs and their session IDs
    private static ConcurrentHashMap<String, String> userSessionMap = new ConcurrentHashMap<>();

    // Store user session
    public static void addUserSession(String userId, String sessionId) {
        userSessionMap.put(userId, sessionId);
    }

    // Remove user session
    public static void removeUserSession(String userId) {
        userSessionMap.remove(userId);
    }

    // Get session ID by user ID
    public static String getSessionId(String userId) {
        return userSessionMap.get(userId);
    }
}
