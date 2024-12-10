package com.server.Services;

import com.server.Server;

public class GetAcceptedFriendRequest {

    Server server;
    public GetAcceptedFriendRequest(Server server) {
        this.server = server;
    }

    public String action(String demandeur, String receveur) {
        return "reponse,recuperer_demande_accepte,demandeur,receveur,erreur\n";
    }
}
