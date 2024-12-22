package com.schoolproject.ChatAPP.model;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;


@Document(collection = "messages") // MongoDB Collection name
public class Message {

    @Id
    private String id; // MongoDB ObjectId

    @DBRef
    private User sender; // Reference to User (Sender)

    @DBRef
    private User recipient; // Reference to User (Recipient, optional)

    private String messageType; // Type of message (text or file)

    private String content; // Content for text messages

    private String fileUrl; // URL for file messages

    @CreatedDate
    private LocalDateTime timestamps; // Created timestamp

    // Custom constructor for validation
    public Message(User sender, User recipient, String messageType, String content, String fileUrl) {
        this.sender = sender;
        this.recipient = recipient;
        this.messageType = messageType;

        if ("text".equals(messageType)) {
            this.content = content;
            this.fileUrl = null; // Clear file URL if it's a text message
        } else if ("file".equals(messageType)) {
            this.fileUrl = fileUrl;
            this.content = null; // Clear content if it's a file message
        } else {
            throw new IllegalArgumentException("Invalid message type");
        }

        this.timestamps = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public LocalDateTime getTimestamps() {
        return timestamps;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setTimestamps(LocalDateTime timestamps) {
        this.timestamps = timestamps;
    }
}
