package com.schoolproject.ChatAPP.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    // Map to track user IDs and their session IDs
    private static final ConcurrentHashMap<String, String> userSessionMap = new ConcurrentHashMap<>();

    /**
     * Adds a session for a user.
     * @param userId    The ID of the user.
     * @param sessionId The WebSocket session ID.
     */
    public static void addUserSession(String userId, String sessionId) {
        if (userId == null || sessionId == null) {
            throw new IllegalArgumentException("User ID and Session ID cannot be null");
        }
        userSessionMap.put(userId, sessionId);
        System.out.println("Session Added: " + userId + " -> " + sessionId);
    }

    /**
     * Removes a session based on the session ID.
     * @param sessionId The WebSocket session ID to be removed.
     */
    public static void removeUserSession(String sessionId) {
        if (sessionId == null) {
            System.out.println("Cannot remove a null session ID");
            return;
        }
        userSessionMap.values().remove(sessionId);
        System.out.println("Session Removed: " + sessionId);
    }

    /**
     * Retrieves the session ID for a given user ID.
     * @param userId The ID of the user.
     * @return The session ID associated with the user, or null if not found.
     */
    public static String getSessionId(String userId) {
        if (userId == null) {
            System.out.println("User ID is null. Cannot get session ID.");
            return null;
        }
        String sessionId = userSessionMap.get(userId);
        if (sessionId == null) {
            System.out.println("No session found for User ID: " + userId);
        }
        return sessionId;
    }

    /**
     * Debug method to print all sessions.
     */
    public static void printAllSessions() {
        System.out.println("Current WebSocket Sessions:");
        userSessionMap.forEach((userId, sessionId) ->
                System.out.println("User: " + userId + " -> Session: " + sessionId)
        );
    }
}
