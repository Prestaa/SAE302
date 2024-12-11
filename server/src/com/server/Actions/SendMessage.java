package com.server.Actions;

import com.server.Models.Message;
import com.server.Models.User;
import com.server.Server;


/**
 * Action êrmettant d'envoyer un message
 */
public class SendMessage {
    Server server;

    public SendMessage(Server server) {
        this.server = server;
    }

    public String action(String username, String receiver, String body) {
        int user_id = server.username_to_id(username);
        int receiver_id = server.username_to_id(receiver);
        
        if(user_id == -1 || receiver_id == -1 || body.equals("")) {
            return "reponse,envoi_message,null,null,erreur";
        }

        User sender_user = server.users[user_id];
        User receiver_user = server.users[receiver_id];
    
        Message message = new Message(sender_user, receiver_user, body);
        
        if(!sender_user.send_message(message, false) || !receiver_user.send_message(message, true)) {
            return "reponse,envoi_message,null,null,erreur";
        }

        return "reponse,envoi_message," + user_id + "," + receiver + ",ok";
    }
}
