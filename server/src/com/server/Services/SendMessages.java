package com.server.Services;

import com.server.Server;

public class SendMessages {
    Server server;

    public SendMessages(Server server) {
        this.server = server;
    }

    public String action(String username, String receiver, String title, String body) {
        return "reponse,envoi_message,null,null,null,null,erreur";
    }
}
