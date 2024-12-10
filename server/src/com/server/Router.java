package com.server;

import com.server.Services.AcceptFriendRequest;
import com.server.Services.GetAcceptedFriendRequest;
import com.server.Services.GetFriendRequest;
import com.server.Services.GetFriends;
import com.server.Services.GetMessage;
import com.server.Services.InviteFriend;
import com.server.Services.Login;
import com.server.Services.SendMessages;
import com.server.Services.Signup;

public class Router {

    Server server;

    public Router(Server server) {
        this.server = server;
    }

    public String get_server_response(String[] words) {
        
        String to_send = "reponse,null,null,null";

        if(words[0].equals("inscription")) {
            Signup signup = new Signup(server);

            to_send = "reponse,inscription,null,erreur\n";

            if(words.length >= 3)
                to_send = signup.action(words[1], words[2]); 
        } 
        else if(words[0].equals("connexion")) {
            Login login = new Login(server);

            to_send = "reponse,connexion,null,erreur\n";

            if(words.length >= 3)
                to_send = login.action(words[1], words[2]);            
        }
        else if(words[0].equals("demande_ami")) {
            InviteFriend inviteFriend = new InviteFriend(server);

            to_send = "reponse,demande_ami,null,erreur\n";
            
            if(words.length >= 3)
                to_send = inviteFriend.action(words[1], words[2]);
        }
        else if(words[0].equals("recuperer_demande")) {
            GetFriendRequest getFriendRequest = new GetFriendRequest(server);

            to_send = "reponse,recuperer_demande,null,erreur\n";

            if(words.length >= 2)
                to_send = getFriendRequest.action(words[1]);
        } 
        else if(words[0].equals("accepter_demande")) {
            AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest(server);

            to_send = "reponse,accepter_demande,null,null,erreur\n";

            if(words.length >= 4)
                to_send = acceptFriendRequest.action(words[1], words[2], words[3]);
        }
        else if(words[0].equals("recuperer_demande_accepte")) {
            GetAcceptedFriendRequest getAcceptedFriendRequest = new GetAcceptedFriendRequest(server);

            to_send = "reponse,recuperer_demande_accepte,null,null,erreur\n";

            if(words.length >= 3)
                to_send = getAcceptedFriendRequest.action(words[1], words[2]);
        }
        else if(words[0].equals("recuperer_amis")) {
            GetFriends getFriends = new GetFriends(server);
            
            to_send = "reponse,recuperer_amis,null,null,null,null,null,null,null,null,null,null,null";
            
            if(words.length >= 2)
                to_send = getFriends.action(words[1]);
        }
        else if(words[0].equals("envoi_message")) {
            SendMessages sendMessages = new SendMessages(server);

            to_send = "reponse,envoi_message,null,null,null\n";

            if(words.length >= 4)
                to_send = sendMessages.action(words[1], words[2], words[3]);    
        }
        else if(words[0].equals("demande_message")) {
            GetMessage getMessage = new GetMessage(server);

            to_send = "reponse,demande_message,null,null,null,erreur\n";
            if(words.length >= 4)
                to_send = getMessage.action(words[1], words[2], words[3]);    
        }
        else if(words[0].equals("")) { to_send = "\n"; }

        return to_send;
    }
}
