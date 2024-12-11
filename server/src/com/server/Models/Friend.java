package com.server.Models;

import java.util.ArrayList;

public class Friend {
    public boolean is_friend = false;
    public ArrayList<Message> message;
    public User user;

    public Friend(User user) {
        message = new ArrayList<Message>();
        this.user = user;
    }

    /**
     * Toggle is_friend to true
     */
    public void friendify() {
        is_friend = true;
    }

    public boolean is_friend() {
        return is_friend;
    }
}
