package com.fpt.hotelbooking.model.response;

public class RegisterRequest {
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;

    public RegisterRequest(String userName, String email, String phoneNumber, String password) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
