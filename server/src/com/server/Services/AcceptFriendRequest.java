package com.server.Services;

import com.server.Server;

public class AcceptFriendRequest {
    
    Server server;
    
    public AcceptFriendRequest(Server server) {
        this.server = server;
    }

    public String action(String demandeur, String receveur) {
        return "reponse,accepte_demande,demandeur,receveur,erreur\n";
    }
}
