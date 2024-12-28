package com.schoolproject.ChatAPP.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages") // MongoDB Collection name
public class Message {

    @Id
    private String id; // MongoDB ObjectId

    private String sender;      // Plain String ID
    private String recipient;   // Plain String ID

    private String messageType; // Type of message (text or file)

    private String content;     // Content for text messages

    private String fileUrl;     // URL for file messages

    private LocalDateTime timestamps = LocalDateTime.now(); // Created timestamp

    // Default constructor for MongoDB
    public Message() {}

    // Custom constructor for manual creation
    public Message(String sender, String recipient, String messageType, String content, String fileUrl) {
        this.sender = sender;
        this.recipient = recipient;
        setMessageType(messageType); // Validates type

        if ("text".equals(messageType)) {
            this.content = content;
            this.fileUrl = null;
        } else if ("file".equals(messageType)) {
            this.fileUrl = fileUrl;
            this.content = null;
        }

        this.timestamps = LocalDateTime.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        if (!"text".equals(messageType) && !"file".equals(messageType)) {
            throw new IllegalArgumentException("Invalid message type");
        }
        this.messageType = messageType;

        if ("text".equals(messageType)) {
            this.fileUrl = null;
        } else if ("file".equals(messageType)) {
            this.content = null;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if ("text".equals(this.messageType)) {
            this.content = content;
        }
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        if ("file".equals(this.messageType)) {
            this.fileUrl = fileUrl;
        }
    }

    public LocalDateTime getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(LocalDateTime timestamps) {
        this.timestamps = timestamps;
    }
}

