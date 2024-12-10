package com.server.Models;

import com.server.Server;

public class User {
    private String username, password;
    public int friend_number = 0;
    public Friend[] friends;
    
    public boolean connected = false;

    public User(String username, String password) {
        this.friends = new Friend[Server.MAX_FRIENDS];

        this.username = username;
        this.password = password;
    }

    

    public boolean add_friend_request(User friend_user) {
        // On a atteint le quota max d'ami
        if(this.friend_number >= Server.MAX_FRIENDS) return false;
        
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

    public void delete_friend_request(String to_delete_username) {
        int id_to_delete = -1;

        for(int i = 0; i < this.friend_number; i++) {
            Friend friend = friends[i];
            User friend_user = friends[i].user;
        
            // !friend.is_friend() => c'est une friend request
            // friend_user....     => c'est le username de la personne qu'on ne souhaite pas accepter
            if(!friend.is_friend() && friend_user.get_username().equals(to_delete_username)) {
                id_to_delete = i;
            }
        }

        if(id_to_delete == -1) return;

        // On décale tous les éléments après la friend request
        // de 1 à gauche
        for (int i = id_to_delete; i < this.friend_number - 1; i++) {
            this.friends[i] = this.friends[i + 1];
        }

    }


    /**
     * Si on est sender alors il faut ajouter l'ami dans la liste d'ami, si on est receiver
     * il faut juste toggle le state is_friend à true.
     * 
     * @param to_add
     * @param sender
     */
    public boolean add_friend(User to_add, boolean sender) {
        int id_to_add = -1;

        if(sender) {
            // On est l'envoyeur, on doit ajouter l'ami dans notre liste d'ami
            Friend new_friend = new Friend(to_add);
            friends[friend_number] = new_friend;
            friend_number++;

            // On a ajouté l'ami on quitte donc la fonction
            return true;
        }

        // Sous entendu si on est receiver

        for(int i = 0; i < this.friend_number; i++) {
            Friend friend = friends[i];
            User friend_user = friends[i].user;
        
            // !friend.is_friend() => c'est une friend request
            // friend_user....     => c'est le username de la personne qu'on ne souhaite pas accepter
            if(!friend.is_friend() && friend_user.get_username().equals(to_add.get_username())) {
                id_to_add = i;
            }
        }

        // On essaye d'ajouter un ami qui nous a pas demandé en ami
        if(id_to_add == -1) return false;

        this.friends[id_to_add].friendify();
        return true;
    }

    
} 
