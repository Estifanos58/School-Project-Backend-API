package com.schoolproject.ChatAPP.requests;

public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    private int color;

    // Getters and setters

    public int getColor() {
        return color;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}

