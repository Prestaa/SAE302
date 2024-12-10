package com.server.Models;

public class Message {
    public boolean read = false;
    public User sender;
    public User receiver;
    public String title;
    public String body;

    public Message(User sender, User receiver, String title, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
    }
}
