package com.server.Actions;

import com.server.Server;
import com.server.Models.User;

public class SupprimerAmi {
    Server server;
  
    public SupprimerAmi(Server server) {
        this.server = server;
    }

    public String action(String user_string, String friend_string) {
        int user_id = server.username_to_id(user_string);
        int friend_id = server.username_to_id(friend_string);

        if(user_id == -1 || friend_id == -1)
            return "reponse,supprimer_ami,login,ami,erreur\n";
        
        User user = server.users[user_id];

        int id = -1;
        for(int i = 0; i <= user.friend_number; i++) {
            if(user.friends[i].user.get_username().equals(friend_string)) {
                id = i;
                break;
            }
        }

        if(id == -1) 
            return "reponse,supprimer_ami,login,ami,erreur\n";


        for(int i = id; i <= user.friend_number - 1; i++) {
            user.friends[i] = user.friends[i+1];
        }

        return "reponse,supprimer_ami,login,ami,ok\n";
    }
}
