package com.server;

import com.server.Services.AcceptFriendRequest;
import com.server.Services.GetFriendRequest;
import com.server.Services.GetFriends;
import com.server.Services.GetMessage;
import com.server.Services.SendFriendRequest;
import com.server.Services.Login;
import com.server.Services.SendMessages;
import com.server.Services.Signup;

public class Router {

    Server server;

    public Router(Server server) {
        this.server = server;
    }

    public String get_server_response(String[] words) {
        
        String to_send = "reponse,null,null";

        if(words[0].equals("inscription")) {
            Signup signup = new Signup(server);
			System.out.println("INSCRIPTION");
            to_send = "reponse,inscription,erreur\n";

            if(words.length >= 3)
                to_send = signup.action(words[1], words[2]); 
        } 
        else if(words[0].equals("connexion")) {
            Login login = new Login(server);

            to_send = "reponse,connexion,erreur\n";

            if(words.length >= 3)
                to_send = login.action(words[1], words[2]);            
        }
        else if(words[0].equals("demande_ami")) {
            SendFriendRequest sendFriendRequest = new SendFriendRequest(server);

            to_send = "reponse,demande_ami,erreur\n";
            
            if(words.length >= 3)
                to_send = sendFriendRequest.action(words[1], words[2]);
        }
        else if(words[0].equals("recuperer_demande")) {
            GetFriendRequest getFriendRequest = new GetFriendRequest(server);

            to_send = "reponse,recuperer_demande,erreur\n";

            if(words.length >= 3)
                to_send = getFriendRequest.action(words[1], words[2]);
        } 
        else if(words[0].equals("accepter_demande")) {
            AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest(server);

            to_send = "reponse,accepter_demande,erreur\n";

            if(words.length >= 4)
                to_send = acceptFriendRequest.action(words[1], words[2], words[3]);
        }
        else if(words[0].equals("recuperer_amis")) {
            GetFriends getFriends = new GetFriends(server);
            
            to_send = "reponse,recuperer_amis,erreur";
            
            if(words.length >= 2)
                to_send = getFriends.action(words[1]);
        }
        else if(words[0].equals("envoi_message")) {
            SendMessages sendMessages = new SendMessages(server);

            to_send = "reponse,envoi_message,erreur\n";

            if(words.length >= 4)
                to_send = sendMessages.action(words[1], words[2], words[3]);    
        }
        else if(words[0].equals("demande_message")) {
            GetMessage getMessage = new GetMessage(server);

            to_send = "reponse,demande_message,erreur\n";
            if(words.length >= 4)
                to_send = getMessage.action(words[1], words[2], words[3]);    
        }
        else if(words[0].equals("")) { to_send = "\n"; }

        return to_send;
    }
}
