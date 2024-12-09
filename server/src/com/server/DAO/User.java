package com.server.DAO;

public class User {
    private User[] ami;
    private int friend_number;
    private String username, password;
    private boolean is_connected;

    public User(String username, String password) {
        this.ami = new User[10];
        this.friend_number = 0;
        this.username = username;
        this.password = password;
    }

    public void add_friend(User friend) {
        if(friend_number >= 10) {
            return;
        }
        
        ami[friend_number++] = friend;
    }

    public void set_connected(boolean state) {
        this.is_connected = state;
    }

    public boolean is_connected() {
        return is_connected;
    }

    public String get_username() {
        return this.username;
    }

    public boolean password_cmp(String password_cmp) {
        return this.password == password_cmp;
    }
}
