package com.server.Services;

import com.server.Server;
import com.server.Models.User;
import com.server.Models.Friend;

public class GetFriendRequest {
    
    Server server;

    public GetFriendRequest(Server server) {
        this.server = server;
    }

    /**
     * Récuperer les friend requests reçues
     * 
     * @param username
     * @return
     */
    public String action(String username) {
        int user_id = server.username_to_id(username);

        
        if(user_id == -1)
            return "reponse,recuperer_demande,null," + username + ",erreur\n";
        
        User current_user = server.users[user_id];
        
        for(int i=0; i < current_user.friend_number; i++) {
            Friend tracker = current_user.friends[i];
            
            if(!tracker.is_friend())
                return "reponse,recuperer_demande," + tracker.user.get_username() + "," + username + ",ok\n";
        }        


        return "reponse,recuperer_demande,null," + username + ",pas_de_demande_ami\n";
    }
}
