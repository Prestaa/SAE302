package com.server.Actions;

import java.util.ArrayList;

import com.server.Server;
import com.server.Models.User;
import com.server.Models.Message;


/**
 * Action permettant de récuperer message par message (le client envoie l'id)
 */
public class GetMessage {
    
    Server server;

    public GetMessage(Server server) {
        this.server = server;
    }

    public String action(String user, String friend, String str_position) {
        System.out.println("USER" + user);

        String error_message = "reponse,demande_message,null,null,null,null,erreur\n";
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

        ArrayList<Message> messages = user_user.get_messages(friend_user, position);
        
        if(messages == null)
            return "reponse,demande_message,null,null,null,null,erreur\n";

        String suite = "non";
        if(position < messages.size()) {
            suite = "oui";
        }
        Message message = messages.get(position);

        return "reponse,demande_message," + user + "," + friend + "," + message.sender.get_username() + "," + message.body + "," + suite + "\n";
    }
}
