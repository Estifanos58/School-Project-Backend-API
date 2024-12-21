package com.schoolproject.ChatAPP.responses;

public class SignUpResponse {
    public String id;
    public String email;
    public boolean profileStatus;

    public SignUpResponse(String id, String email, boolean profileStatus) {
        this.id = id;
        this.email = email;
        this.profileStatus = profileStatus;
    }
}
