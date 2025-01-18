package com.schoolproject.ChatAPP.responses;

import java.util.Arrays;

public class UpdateProfileResponse {
    private String id;
    private String email;
    private boolean profileSetup;
    private String firstname;
    private String lastname;
    private String image;
    private int color;

    // Constructor, getters, and setters

    public UpdateProfileResponse(String id, String email, boolean profileSetup, String firstname, String lastname, String image, int color) {
        this.id = id;
        this.color = color;
        this.image = image;
        this.lastname = lastname;
        this.firstname = firstname;
        this.profileSetup = profileSetup;
        this.email = email;
    }



    public String getId() {
        return id;
    }

    public boolean isProfileSetup() {
        return profileSetup;
    }

    public String getEmail() {
        return email;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileSetup(boolean profileSetup) {
        this.profileSetup = profileSetup;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

