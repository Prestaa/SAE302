package com.server.Services;

import com.server.Server;
import com.server.Models.User;


public class Signup {

    Server server;

    public Signup(Server server) {
        this.server = server;
    }

    /**
     * Inscrit un utilisateur
     * 
     * @param login
     * @param password
     * @return
     */
    public String action(String login, String password) {
        // Si on a atteint le quota max d'utilisateurs inscrits
        if(server.user_number >= 10) 
            return "reponse,inscription,null,erreur\n";

        // Si l'utilisateur existe déjà OU que le login/password est vide
        if(server.username_to_id(login) != -1 || login == "" || password == "") 
            return "reponse,inscription," + login + ",erreur\n";

        System.out.println("Ajout de l'utilisateur au sein de mes fesses");
        server.users[server.user_number++] = new User(login, password);
        return "reponse,inscription," + login + ",ok\n";
    }
}

