package com.server.Actions;

import com.server.Server;
import com.server.Models.Friend;
import com.server.Models.User;


public class GetFriends {
    Server server;

    public GetFriends(Server server) {
        this.server = server;
    }

    public String action(String username) {
        String to_send = "reponse,recuperer_amis," + username + "";

        int user_id = server.username_to_id(username);

        if(user_id == -1)
            return "reponse,recuperer_amis," + username + ",null,null,null,null,null,null,null,null,null,null,erreur";

        // Moi
        User user = server.users[user_id];
        
        for(int i=0; i<Server.MAX_FRIENDS; i++) {

            if(user.friends[i] == null) {
                to_send += ",null";
                continue;
            }

            Friend tmp_friend = user.friends[i];            
            if(tmp_friend.is_friend())
                to_send += "," + tmp_friend.user.get_username();
        }
        return to_send + "\n";
    }
}
