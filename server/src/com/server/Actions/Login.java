package com.server.Actions;

import com.server.Server;

public class Login {

    Server server;

    public Login(Server server) {
        this.server = server;    
    }

    /**
     * Authentifie un utilisateur
     * 
     * @param username
     * @param password
     * @return
     */
    public String action(String username, String password) {
        int user_id = server.username_to_id(username);

        // L'utilisateur n'existe pas OU les identifiants sont incorrect
        if(user_id == -1 || !server.users[user_id].login(username, password))
            return "reponse,connexion," + username + ",erreur\n";
        
        return "reponse,connexion," + username + ",ok\n";
    }
}
