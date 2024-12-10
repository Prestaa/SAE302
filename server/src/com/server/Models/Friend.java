package com.server.Models;

public class Friend {
    public boolean is_friend = false;
    public Message[] message;
    public User user;

    public Friend(User user) {
        this.user = user;
    }

    public void friendify() {
        is_friend = true;
    }

    public boolean is_friend() {
        return is_friend;
    }
}
