package com.server.Models;

public class User {
    private String username, password;
    public int friend_number = 0;
    public Friend[] friends;
    
    public boolean connected = false;

    public User(String username, String password) {
        this.friends = new Friend[10];

        this.username = username;
        this.password = password;
    }

    

    public boolean add_friend_request(User friend_user) {
        // On a atteint le quota max d'ami
        if(this.friend_number >= 10) return false;
        
        // L'utilisateur essaye de s'ajouter en ami
        if(friend_user.get_username().equals(this.get_username())) return false;

        // On va chercher si on a déjà cet ami
        for(int i = 0; i <= this.friend_number; i++)
            // Si on a déjà ce user en ami on retourne false
            if(this.friends[i] != null) {
                Friend to_test_friend = this.friends[i];
                User to_test_user = to_test_friend.user;

                if(to_test_user.get_username().equals(friend_user.get_username()) && 
                   to_test_friend.is_friend()
                ) return false;
            }
        
        Friend friend = new Friend(friend_user);
        friends[friend_number++] = friend;
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

        for(int i = 0; i < this.friend_number; i++) {
            Friend friend = friends[i];
            User friend_user = friends[i].user;

            if(friend != null && friend_user != null)
                continue;
            
            if(!friend.is_friend())
                System.out.println("Demande d'ami de: " + friend_user.get_username());
            else
                System.out.println(friend_user.get_username() + " est votre ami.");
        }
    }
}
