package com.server.Models;

public class Message {
    public boolean read = false;
    public User sender;
    public User receiver;
    public String body;

    public Message(User sender, User receiver, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
    }
}
