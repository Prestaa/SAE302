package com.server.DAO;

public class User {
    private String username, password;
    private int friend_number = 0;
    private User[] friends;
    
    public int friend_request_number = 0;
    public User[] friend_requests;

    public User(String username, String password) {
        this.friend_requests = new User[10];
        this.friends = new User[10];

        this.username = username;
        this.password = password;
    }

    public boolean add_friend(User friend) {
        if(friend_number >= 10) return false;
        
        this.friends[friend_number++] = friend;
        return true;
    }

    public boolean add_friend_request(User friend) {
        // On a atteint le quota max d'ami
        if(this.friend_request_number >= 10) return false;
        
        // L'utilisateur essaye de s'ajouter en ami
        if(friend.get_username().equals(this.get_username())) return false;

        // On va chercher si on a déjà cet ami
        for(int i = 0; i <= this.friend_number; i++)
            // Si on a déjà ce user en ami on retourne false
            if(this.friends[i] != null && this.friends[i].get_username().equals(friend.get_username())) 
                return false;
        
        friend_requests[friend_request_number++] = friend;
        return true;
    }


    public String get_username() {
        return this.username;
    }

    public boolean login(String username, String password) {
        return username.equals(this.username) && password.equals(password); 
    }

    public void show() { 
        System.out.println("> GENERAL INFORMATIONS" +
                            "\nUser:" + this.username + 
                            "\nPass:" + this.password +
                            "\n\n> FRIEND REQUESTS"                    
        ); 

        for(int i = 0; i < this.friend_request_number; i++) {
            if(friend_requests[i] != null)
                System.out.println("Friend request from " + friend_requests[i].get_username());
        }
        
        System.out.println("\n> FRIENDS");

        for(int i = 0; i < this.friend_number; i++) {
            if(friend_requests[i] != null)
                System.out.println("Friend: " + friend_requests[i].get_username());
        }
    }
}
