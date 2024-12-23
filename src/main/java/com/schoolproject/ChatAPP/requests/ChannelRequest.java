package com.schoolproject.ChatAPP.requests;

import java.util.List;

public class ChannelRequest {
    private String name;
    private List<String> members;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}