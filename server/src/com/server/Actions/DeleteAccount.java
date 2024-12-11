package com.server.Actions;

import com.server.Server;
import com.server.Models.User;

public class DeleteAccount {
    public Server server;

    public DeleteAccount(Server server) {
        this.server = server;
    }

    public String action(String username, String password) {

        int id = server.username_to_id(username);
        // L'utilisateur n'existe pas
        if(id == -1) 
            return "response,delete,login,erreur\n";

        User user = server.users[id];
        if(!user.login(username, password)) 
            return "response,delete,login,erreur\n";
        
        if(!server.delete_user(user))     
            return "response,delete,login,erreur\n";
            
    
        return "response,delete," + username + ",ok\n";
    }

}
