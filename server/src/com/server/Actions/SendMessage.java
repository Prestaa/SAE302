package com.server.Actions;

import com.server.Models.*;
import com.server.Server;


/**
 * Action êrmettant d'envoyer un message
 */
public class SendMessage {
    Server server;

    public SendMessage(Server server) {
        this.server = server;
    }

    public String action(String sender_string, String receiver_string, String body) {
        
        int sender_id = server.username_to_id(sender_string);
        if(sender_id == -1) {
            return "reponse,envoi_message,null,null,erreur\n";
        }

        // On envoie le message à tous les utilisateurs
        if(receiver_string.equals("tous")) {
            
            // Envoyeur de message
            User user = server.users[sender_id];

            // Parcours les amis de l'envoyeur
            for(int i = 0; i < user.friend_number; i++) {
                // On crée un message
                User sender = user;
                User receiver = user.friends[i].user;
                
                Message message = new Message(sender, receiver, body);

                // On ajoute le message au tableau de message du friend
                if(!sender.send_message(message, true) ||
                   !receiver.send_message(message, false)) {
                    return "reponse,envoi_message,null,null,erreur\n";
                }

                System.out.println("DU COTÉ DE " + user.get_username());
                System.out.println("DU COTÉ DE " + user.friends[i].user.get_username());

            }
            
            return "reponse,envoi_message," + sender_string + ",tous,ok\n";
        }

        int receiver_id = server.username_to_id(receiver_string);

        if(sender_id == -1 || receiver_id == -1 || body.equals("")) {
            return "reponse,envoi_message,null,null,erreur\n";
        }

        User receiver_user = server.users[receiver_id];
        User sender_user = server.users[sender_id];
        
        Message message = new Message(sender_user, receiver_user, body);
        
        if(!sender_user.send_message(message, true) || !receiver_user.send_message(message, false)) {
            return "reponse,envoi_message,null,null,erreur\n";
        }

        return "reponse,envoi_message," + sender_user.get_username() + "," + receiver_user.get_username() + ",ok\n";
    }
}
