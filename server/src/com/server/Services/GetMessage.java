package com.server.Services;

import java.util.ArrayList;

import com.server.Server;
import com.server.Models.User;
import com.server.Models.Message;

public class GetMessage {
    
    Server server;

    public GetMessage(Server server) {
        this.server = server;
    }

    public String action(String user, String friend, String envoyeur, String str_position) {

        String error_message = "reponse,demande_message,null,null,null,null\n";
        int user_id = server.username_to_id(user);
        int friend_id = server.username_to_id(friend);
        int position = -1;
        
        try {
            position = Integer.parseInt(str_position);
        } catch(Exception e) {
            return error_message;
        }
        if(position == -1 || user_id == -1 || friend_id == -1) return error_message;

        User user_user = server.users[user_id];
        User friend_user = server.users[friend_id];

        ArrayList<Message> message = user_user.get_messages(friend_user, position);
        
        if(message != null)
            return "reponse,demande_message," + user + "," + friend + "," + envoyeur + ",null,null\n";


        String suite = "non";
        if(position < message.size()) {
            suite = "oui";
        }

        return "reponse,demande_message," + user + "," + friend + "," + envoyeur + "," + message + "," + suite + "\n";
    }
}
