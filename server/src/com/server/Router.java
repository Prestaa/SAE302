package com.server;

// Authentification
import com.server.Actions.Signup;
import com.server.Actions.Login;

// Gestion des amis
import com.server.Actions.SendFriendRequest;
import com.server.Actions.GetFriendRequest;
import com.server.Actions.AcceptFriendRequest;

import com.server.Actions.GetFriend;

// Gestion des messages
import com.server.Actions.SendMessage;
import com.server.Actions.GetMessage;

import com.server.Models.User;

public class Router {

    // On a besoin du serveur car c'est lui qui contient le tableau de user et les
    // méthodes permettant de convertir un nom en sa position dans le tableau 
    // etc ...
    Server server;

    public Router(Server server) { this.server = server; }


    /**
     * Génère la réponse serveur à envoyer en se basant sur les actions.
     * Chaque action est indépendante, et donc facile à ajouter/retirer.
     * 
     * Cette idée est tirée et basée sur les Laravel Actions que 
     * j'apprécie. 
     * 
     * @param words
     * @return
     */
    public String get_server_response(String[] words) {
        
        String to_send = "reponse,null,null\n";

        if(words[0].equals("inscription")) {
            System.out.println("[DEBUG] INSCRIPTION d'un nouvel utilisateur");

            Signup signup = new Signup(server);

            to_send = "reponse,inscription,erreur\n";

            if(words.length >= 3)
                to_send = signup.action(words[1], words[2]); 
        } 
        else if(words[0].equals("connexion")) {
            System.out.println("[DEBUG] CONNEXION d'un utilisateur");
            
            Login login = new Login(server);
            
            to_send = "reponse,connexion,erreur\n";

            if(words.length >= 3)
                to_send = login.action(words[1], words[2]);            
        }
        else if(words[0].equals("demande_ami")) {
            System.out.println("[DEBUG] DEMANDE_AMI d'un utilisateur");
            
            SendFriendRequest sendFriendRequest = new SendFriendRequest(server);

            to_send = "reponse,demande_ami,erreur\n";
            
            if(words.length >= 3)
                to_send = sendFriendRequest.action(words[1], words[2]);
        }
        else if(words[0].equals("recuperer_demande")) {
            System.out.println("[DEBUG] DEMANDE de récupération d'ami d'un utilisateur");
            
            GetFriendRequest getFriendRequest = new GetFriendRequest(server);

            to_send = "reponse,recuperer_demande,erreur\n";

            if(words.length >= 3)
                to_send = getFriendRequest.action(words[1], words[2]);
        } 
        else if(words[0].equals("accepter_demande")) {
            System.out.println("[DEBUG] ACCEPTATION D'UNE DEMANDE d'ami d'un utilisateur");

            AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest(server);

            to_send = "reponse,accepter_demande,erreur\n";

            if(words.length >= 4)
                to_send = acceptFriendRequest.action(words[1], words[2], words[3]);
        }
        else if(words[0].equals("recuperer_amis")) {
            System.out.println("[DEBUG] ACCEPTATION D'UNE DEMANDE d'ami d'un utilisateur");

            GetFriend getFriends = new GetFriend(server);
            
            to_send = "reponse,recuperer_amis,erreur";
            
            if(words.length >= 2)
                to_send = getFriends.action(words[1]);
        }
        else if(words[0].equals("envoi_message")) {
            System.out.println("[DEBUG] ENVOI DE MESSAGE d'un utilisateur");

            SendMessage sendMessages = new SendMessage(server);

            to_send = "reponse,envoi_message,erreur\n";

            if(words.length >= 4)
                to_send = sendMessages.action(words[1], words[2], words[3]);    
        }
        else if(words[0].equals("demande_message")) {
            System.out.println("[DEBUG] DEMANDE DE MESSAGE d'un utilisateur");

            GetMessage getMessage = new GetMessage(server);

            to_send = "reponse,demande_message,erreur\n";
            if(words.length >= 4)
                to_send = getMessage.action(words[1], words[2], words[3]);    
        }
        else if(words[0].equals("")) { to_send = ""; }

        return to_send;
    }
}
