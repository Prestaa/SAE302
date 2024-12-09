package com.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.server.DAO.User;



public class Server {
    private DatagramSocket socket;
    private DatagramPacket received;
    private DatagramPacket sent;

    private User[] users;
    private int user_number = 0;
    private byte[] received_bytes;
    
    public Server() {
        users = new User[10];
    }

    public void run() throws SocketException, IOException {
        this.socket = new DatagramSocket(1337);
        System.out.println("[*] Server listening on udp://0.0.0.0:1337");
        
        // Tableau dans lequel on mettera les bytes envoyés par le client
        this.received_bytes = new byte[256];

        while(true) {
            // On récupère le message envoyé par le client
            String received_message = get_client_message();

            // To send contiendra le message à envoyer au client 
            byte[] to_send = this.get_to_send_message(received_message.strip());
            
            // On envoie au client
            send_message(to_send);
        }
    }

    public byte[] get_to_send_message(String message) {
        String to_send;

        String[] words = message.split(",");

        switch (words[0]) {
            case "inscription":
                if(words.length == 3) {
                    to_send = signup(words[1], words[2]);                
                } else {
                    to_send = "reponse,inscription,null,erreur\n";
                }
                break;

            case "connexion":
                if(words.length == 3) {
                    to_send = login(words[1], words[2]);                
                } else {
                    to_send = "reponse,connexion,null,erreur\n";
                }
                break;
            default:
                to_send = "Unrecognized command\n";
                break;
        }
        leak_db();
        return to_send.getBytes();
    }

    public void send_message(byte[] sent_bytes) throws IOException {
        InetAddress client_addr = this.received.getAddress();
        int client_port = this.received.getPort();

        this.sent = new DatagramPacket(sent_bytes, sent_bytes.length, client_addr, client_port);
        this.socket.send(this.sent);
    }

    public String get_client_message() throws IOException {
        this.received = new DatagramPacket(this.received_bytes, this.received_bytes.length);
        this.socket.receive(this.received);

        // Récupération du message envoyé par le client
        return new String(this.received.getData(), 0, this.received.getLength());
    }

    public void close() {
        if(!socket.isClosed()) socket.close();
    }


    public String signup(String login, String password) {
        if(user_number >= 10) {
            System.out.println("[DEBUG] Nombre d'inscrit maximum atteint");
            return "reponse,inscription,null,erreur\n";
        }

        // On itère a travers chaque utilisateur de la db
        for(int i=0; i < user_number; i++) {
            String got_username = this.users[i].get_username();
            // Si le login est deja présent dans la DB
            if(got_username.equals(login)) {
                System.out.println("[DEBUG] L'utilisateur existe déjà !");
                return "reponse,inscription," + login + ",erreur\n";
            }
        } 

        // Si le login ou le mot de passe est vide
        if(login == null || login == "" || password == null || password == "") {
            return "reponse,inscription," + login + ",erreur\n";
        }

        System.out.println("[DEBUG] Ajout de l'utilisateur " + login + ":" + password);
        // On ajoute l'utilisateur
        users[user_number++] = new User(login, password);
        return "reponse,inscription," + login + ",ok\n";
    }

    public String login(String username, String password) {
        for(int i=0; i < user_number; i++) {
            // Si le login est deja présent dans la DB
            if(users[i].login(username, password)) {
                return "reponse,connexion," + username + ",ok\n";
            }
        }
        return "reponse,connexion," + username + ",erreur\n";
    }

    public void leak_db() {
        System.out.println("\n\n---------- LEAK DB ----------");
        for(User user: users) {
            if(user != null) {
                // Si le login est deja présent dans la DB
                System.out.println("user:" + user.get_username() + " pass:" + user.leak_password());
            }
        } 
        System.out.println("---------- END LEAK ----------\n\n");

    }
}
