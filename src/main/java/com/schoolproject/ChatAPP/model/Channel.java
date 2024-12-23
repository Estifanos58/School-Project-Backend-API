package com.schoolproject.ChatAPP.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "channels") // MongoDB collection name
public class Channel {

    @Id
    private String id; // MongoDB ID

    private String name; // Channel name (required)

    @DBRef
    private List<User> members; // Members of the channel (required)

    @DBRef
    private User admin; // Admin of the channel (optional)

    @DBRef
    private List<Message> messages; // Messages in the channel (optional)

    private Date createdAt; // Creation date

    @LastModifiedDate
    private Date updatedAt; // Last modified date

    // Constructor
    public Channel() {
        this.createdAt = new Date(); // Set creation date
        this.updatedAt = new Date(); // Set initial update date
    }

    public Channel(String name, List<User> members, User admin, List<Message> messages) {
        this.name = name;
        this.members = members;
        this.admin = admin;
        this.messages = messages;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
