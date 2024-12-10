package com.server.Services;

import com.server.Server;

public class GetMessage {
    
    Server server;

    public GetMessage(Server server) {
        this.server = server;
    }

    public String action(String demandeur, String receveur) {
        return "reponse,demande_accepte,demandeur,receveur,erreur\n";
    }
}
