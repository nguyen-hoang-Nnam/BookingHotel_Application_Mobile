package com.fpt.hotelbooking.model;


public class User {
    private int id;
    private String password;
    private String email;
    private String phoneNumber;
    private String username;

    public User(int id, String password, String email, String phoneNumber) {
    }

    public User() {
    }

    ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
