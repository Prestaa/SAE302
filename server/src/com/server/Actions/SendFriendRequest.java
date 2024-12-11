package com.server.Actions;

import com.server.Server;
import com.server.Models.User;

public class SendFriendRequest {
    
    Server server;

    public SendFriendRequest(Server server) {
        this.server = server;
    } 
    
    /**
     * Permet d'envoyer une demande d'ami 
     * 
     * @param sender_name       
     * @param receiver_name       
     * @return
     */
    public String action(String sender_name, String receiver_name) {
        
        int sender_id = server.username_to_id(sender_name);
        int receiver_id = server.username_to_id(receiver_name);

        // Le demandeur OU le receveur n'existe pas
        if(sender_id == -1 || receiver_id == -1) {
            return "reponse,demande_ami," + sender_name + "," + receiver_name + ",erreur\n";
        }

        User sender = server.users[sender_id];
        User receiver = server.users[receiver_id];

        if(!receiver.add_friend_request(sender))
            return "reponse,demande_ami," + sender_name + "," + receiver_name + ",erreur\n";


        return "reponse,demande_ami," + sender_name + "," + receiver_name + ",ok\n";
    }
}
