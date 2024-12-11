package com.server.Actions;

import com.server.Server;
import com.server.Models.User;

/**
 * Action permettant d'accepter une demande d'ami
 */
public class AcceptFriendRequest {
    
    Server server;
    
    public AcceptFriendRequest(Server server) {
        this.server = server;
    }

    public String action(String demandeur, String receveur, String state) {
        
        int demandeur_id = server.username_to_id(demandeur);
        int receveur_id = server.username_to_id(receveur);

        if(
            demandeur_id == -1 || 
            receveur_id == -1 || 
            !(state.equals("oui") || state.equals("non"))
        ) return "reponse,accepter_demande,demandeur,receveur,erreur\n";

        
        User receveur_user = server.users[receveur_id];
        User demandeur_user = server.users[demandeur_id];

        if(state.equals("non")) {
            // On vérifie cette demande d'ami
            if(receveur_user.delete_friend_request(demandeur))
                return "reponse,accepter_demande," + demandeur + "," + receveur + ",ok\n";
            else
                return "reponse,accepter_demande," + demandeur + "," + receveur + ",erreur\n";
        }  
        
        if(
            !receveur_user.add_friend(demandeur_user, false) || 
            !demandeur_user.add_friend(receveur_user, true)
        ) return "reponse,accepter_demande," + demandeur + "," + receveur + ",erreur\n";

        return "reponse,accepter_demande," + demandeur + "," + receveur + ",ok\n";
    }
}
