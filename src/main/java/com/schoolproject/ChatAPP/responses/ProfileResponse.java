package com.schoolproject.ChatAPP.responses;

// Custom Response class for better readability
public class ProfileResponse {
    private String message;
    private int statusCode;
    private String filePath;

    public ProfileResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public ProfileResponse(String message, int statusCode, String filePath) {
        this.message = message;
        this.statusCode = statusCode;
        this.filePath = filePath;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
