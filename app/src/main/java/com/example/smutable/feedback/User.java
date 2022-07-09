package com.example.smutable.feedback;

public class User {
    public String contact, message, dateTime;

    public User() {
    }

    public User(String contact, String message, String dateTime) {
        this.contact = contact;
        this.message = message;
        this.dateTime = dateTime;
    }
}