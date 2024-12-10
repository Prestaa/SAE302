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
    public String action(String username, String friend_id_string) {
        int user_id = server.username_to_id(username);
        int friend_id = Integer.parseInt(friend_id_string);
        
        if(user_id == -1)
            return "reponse,recuperer_demande,null," + username + ",erreur\n";
        
        User current_user = server.users[user_id];
        
        int[] ids = new int[Server.MAX_FRIENDS];
        int fr_requests = 0;

        for(int i=0; i < current_user.friend_number; i++) {
            Friend tracker = current_user.friends[i];
            if(!tracker.is_friend()) {
                ids[fr_requests] = i;
                fr_requests++;
            }   
        }    
        
        if(fr_requests == 0 || friend_id > fr_requests) {
            return "reponse,recuperer_demande," + username + ",null,pas_de_demande_ami\n";
        }

        // Si c'est le dernier ami on met non, sinon on met oui
        String next = (friend_id == fr_requests -1 ) ? "non" : "oui";
        Friend demandeur = current_user.friends[ids[friend_id]];
        
        return "reponse,recuperation_demande,login," + demandeur.user.get_username() + "," + next + "\n";
    }
}
