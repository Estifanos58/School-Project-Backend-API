package com.schoolproject.ChatAPP.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String firstname;

    private String lastname;
    private int color;
    private String image;
    private boolean profileSetup = false;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getColor() {
        return color;
    }

    public String getImage() {
        return image;
    }

    public boolean isProfileSetup() {
        return profileSetup;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileSetup(boolean profileSetup) {
        this.profileSetup = profileSetup;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", color=" + color +
                ", image='" + image + '\'' +
                ", profileSetup=" + profileSetup +
                '}';
    }

    public String getId() {
        return id;
    }
}
