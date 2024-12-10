package com.server.Services;

import com.server.Server;

public class SendMessages {
    Server server;

    public SendMessages(Server server) {
        this.server = server;
    }

    public String action(String username, String receiver, String body) {
        
        return "reponse,recuperer_message,login,receveur,message,suite";
    }
}
