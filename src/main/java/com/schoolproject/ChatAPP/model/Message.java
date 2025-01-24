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

    private String channelId = null;

    private String senderName;

    private int color;

    private String image;

    // Default constructor for MongoDB
    public Message() {}

    // Custom constructor for manual creation
    public Message(String sender, String recipient, String messageType, String content, String fileUrl) {
        this.sender = sender;
        this.recipient = recipient;
        this.messageType = messageType; // Directly assign messageType

        // Ensure content or fileUrl is set based on messageType
        if ("text".equals(messageType)) {
            this.content = content;   // Accept the passed content
            this.fileUrl = null;      // Ensure fileUrl is cleared
        } else if ("file".equals(messageType)) {
            this.fileUrl = fileUrl;   // Accept the passed fileUrl
            this.content = null;      // Ensure content is cleared
        } else {
            throw new IllegalArgumentException("Invalid message type");
        }

        this.timestamps = LocalDateTime.now();
    }


    // Setter for messageType
    public void setMessageType(String messageType) {
        this.messageType = messageType; // Directly set type
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    // Setter for content (no checks)
    public void setContent(String content) {
        this.content = content; // Allow setting content directly
    }

    // Setter for fileUrl (no checks)
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl; // Allow setting fileUrl directly
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public void setTimestamps(LocalDateTime timestamps) {
        this.timestamps = timestamps;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", messageType='" + messageType + '\'' +
                ", content='" + content + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", timestamps=" + timestamps +
                '}';
    }

}

