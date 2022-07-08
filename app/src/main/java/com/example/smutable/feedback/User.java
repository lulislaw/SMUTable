package com.example.smutable.feedback;

public class User {
    public String id, contact, message;

    public User() {
    }

    public User(String id, String contact, String message) {
        this.id = id;
        this.contact = contact;
        this.message = message;
    }
}