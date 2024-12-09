package com.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.server.DAO.User;

public class Server {

    public static int PORT = 1337;

    private DatagramSocket socket;
    private DatagramPacket received;
    private DatagramPacket sent;

    private byte[] received_bytes;
    private int user_number = 0;
    private User[] users;

    
    public Server() {
        users = new User[10];
    }


    /**
     * Méthode qui permet le lancement du serveur UDP sur le port spécifié
     * 
     * @throws SocketException
     * @throws IOException
     */
    public void run() throws SocketException, IOException {
        this.socket = new DatagramSocket(Server.PORT);
        System.out.println("[*] Server listening on udp://0.0.0.0:" + Server.PORT);
        
        // Tableau dans lequel on mettera les bytes envoyés par le client
        this.received_bytes = new byte[256];

        while(true) {
            // On récupère le message envoyé par le client
            String received_message = get_client_message();

            // To send contiendra le message à envoyer au client 
            byte[] to_send = this.get_to_send_message(received_message);
            
            // On envoie au client
            send_message(to_send);
        }
    }


    /**
     * Génére et return la réponse du serveur en fonction du message 
     * envoyé par le client
     * 
     * @param client_message        Message du client (ex: connexion,test,test)
     * @return
     */
    public byte[] get_to_send_message(String client_message) {

        String[] words = client_message.split(",");
        String to_send;

        // words[0] => la commande envoyée par le client
        switch (words[0]) {
            case "inscription":
                to_send = (words.length >= 3) ? signup(words[1], words[2]) : "reponse,inscription,null,erreur\n";
                break;

            case "connexion":
                to_send = (words.length >= 3) ? login(words[1], words[2]) : "reponse,connexion,null,erreur\n";
                break;

            case "demande_ami":
                to_send = (words.length >= 3) ? invite_friend(words[1], words[2]) : "reponse,demande_ami,null,erreur\n";
                break;

            case "recuperer_demande":
                to_send = (words.length >= 2) ? get_friend_request(words[1]) : "reponse,recuperer_demande,null,erreur\n";
                break;

            case "accepte_demande":
                to_send = accept_friend_request();
                break;

            case "demande_accepte":
                to_send = get_accepted_friend();
                break;

            case "envoie_message":
            case "recuperer_message":
                to_send = "\n";
                break;
                
            case "":
                to_send = "\n";
                break;

            /* DEV COMMANDS */
            case "show":
                users[this.username_to_id(words[1])].show();
                to_send = "\n";
                break;
            default:
                to_send = "reponse,null,null,erreur\n";
                break;
        }
        leak_db();
        return to_send.getBytes();
    }


    /**
     * Inscrit un utilisateur
     * 
     * @param login
     * @param password
     * @return
     */
    public String signup(String login, String password) {
        // Si on a atteint le quota max d'utilisateurs inscrits
        if(user_number >= 10)
            return "reponse,inscription,null,erreur\n";

        // Si l'utilisateur existe déjà OU que le login/password est vide
        if(username_to_id(login) != -1 || login == "" || password == "") 
            return "reponse,inscription," + login + ",erreur\n";

        users[user_number++] = new User(login, password);
        return "reponse,inscription," + login + ",ok\n";
    }


    /**
     * Authentifie un utilisateur
     * 
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        int user_id = username_to_id(username);

        // L'utilisateur n'existe pas OU les identifiants sont incorrect
        if(user_id == -1 || !this.users[user_id].login(username, password))
            return "reponse,connexion," + username + ",erreur\n";
        
        return "reponse,connexion," + username + ",ok\n";
    }


    /**
     * Permet d'envoyer une demande d'ami 
     * 
     * @param sender_name       
     * @param receiver_name       
     * @return
     */
    public String invite_friend(String sender_name, String receiver_name) {
        
        int sender_id = username_to_id(sender_name);
        int receiver_id = username_to_id(receiver_name);

        // Le demandeur OU le receveur n'existe pas
        if(sender_id == -1 || receiver_id == -1) {
            return "reponse,demande_ami," + sender_name + "," + receiver_name + ",erreur\n";
        }

        User sender = users[sender_id];
        User receiver = users[receiver_id];

        if(!receiver.add_friend_request(sender))
            return "reponse,demande_ami," + sender_name + "," + receiver_name + ",erreur\n";


        return "reponse,demande_ami," + sender_name + "," + receiver_name + ",ok\n";
    }

    public String get_friend_request(String username) {
        int user_id = username_to_id(username);
        
        if(user_id == -1)
            return "reponse,recuperation_demande,demandeur,receveur,erreur\n";

        User current_user = users[user_id];

        // Nous n'avons aucune demande d'ami
        if(current_user.friend_request_number == 0)
            return "reponse,recuperation_demande,demandeur,receveur,erreur\n";

        User sender = current_user.friend_requests[current_user.friend_request_number-1];

        return "reponse,recuperation_demande," + sender.get_username() + "," + username + ",ok\n";
    }

    public String accept_friend_request() {
        return "reponse,accepte_demande,demandeur,receveur,erreur\n";
    }

    public String get_accepted_friend() {
        return "reponse,demande_accepte,demandeur,receveur,erreur\n";
    }


    /* --------------------- UTILS --------------------- */
    /*                                                   */
    /*     Fonctions utiles permettant de faciliter      */
    /*     la phase de développement                     */
    /*                                                   */
    /* ------------------------------------------------- */


    /**
     * Permet d'envoyer un messages au client UDP
     * 
     * @param sent_bytes
     * @throws IOException
     */
    public void send_message(byte[] sent_bytes) throws IOException {
        InetAddress client_addr = this.received.getAddress();
        int client_port = this.received.getPort();

        this.sent = new DatagramPacket(sent_bytes, sent_bytes.length, client_addr, client_port);
        this.socket.send(this.sent);
    }


    /**
     * Récupère le message envoyé par le client UDP
     * @return
     * @throws IOException
     */
    public String get_client_message() throws IOException {
        this.received = new DatagramPacket(this.received_bytes, this.received_bytes.length);
        this.socket.receive(this.received);

        // Récupération du message envoyé par le client
        return new String(this.received.getData(), 0, this.received.getLength()).strip();
    }


    /**
     * Renvoie l'id dans le tableau users[] correspondant au user ayant comme 
     * username la valeur passé en paramètre. -1 sinon.
     * 
     * @param username
     * @return
     */
    public int username_to_id(String username) {
        for(int i=0; i < user_number; i++) {
            // Si le login est deja présent dans la DB
            if(users[i] != null && users[i].get_username().equals(username)) {
                return i;
            }
        }

        return -1;
    }


    /**
     * Fonction permettant d'afficher le contenu du tableau users[] sous un format
     * lisible
     */
    public void leak_db() {
        System.out.println("---------------------------");
        for(int i = 0; i <= this.user_number; i++)
            if(users[i] != null)    
                System.out.println(this.users[i].get_username());
        System.out.println("---------------------------");
    }


    public void close() {
        if(!socket.isClosed()) socket.close();
    }


}
