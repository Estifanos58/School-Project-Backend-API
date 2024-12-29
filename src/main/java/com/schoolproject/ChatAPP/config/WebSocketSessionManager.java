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
        System.out.println("Session Added: " + userId + " -> " + sessionId);
    }

    public static void removeUserSession(String sessionId) {
        userSessionMap.values().remove(sessionId);
        System.out.println("Session Removed: " + sessionId);
    }

    public static String getSessionId(String userId) {
        System.out.println("Getting Session ID for User: " + userId);
        return userSessionMap.get(userId);
    }


}
