package com.schoolproject.ChatAPP.responses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class LoginResponse {
    private String id;
    private String email;
    private boolean profileSetup;
    private String firstname;
    private String lastname;
    private String image;
    private int color;


    public LoginResponse(String id, String email, boolean profileSetup, String firstname, String lastname, String image, int color) {
        this.id = id;
        this.email = email;
        this.profileSetup = profileSetup;
        this.firstname = firstname;
        this.lastname = lastname;
        this.image = image;
        this.color = color;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isProfileSetup() {
        return profileSetup;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getImage() {
        return image;
    }

    public int getColor() {
        return color;
    }
}


