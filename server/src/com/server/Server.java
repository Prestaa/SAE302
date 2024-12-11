package com.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.server.Models.User;

/**
 * La classe Server gère tout ce qui a attrait au serveur, c'est à dire entre autre 
 * - écouter sur le port Server.PORT
 * - récupérer les messages du clients 
 * - envoyer les réponses générées par les action au client  
 * - conserver le tableau contenant les utilisateurs.
 */
public class Server {

    public static int PORT = 1337;

    public static int MAX_USERS = 10;
    public static int MAX_FRIENDS = 10;

    private DatagramSocket socket;
    private DatagramPacket received;
    private DatagramPacket sent;
    
    private byte[] received_bytes;
    public int user_number = 0;
    public User[] users;
    
    Router router;
    
    public Server() {
        users = new User[Server.MAX_USERS];

        // Le router a besoin de l'instance courante de serveur
        // afin de bénéficier du tableau d'utilisateur etc
        router = new Router(this);
    }


    /**
     * Méthode qui permet le lancement du serveur UDP sur le port spécifié
     * 
     * @throws SocketException
     * @throws IOException
     */
    public void listen() throws SocketException, IOException {
        this.socket = new DatagramSocket(Server.PORT, InetAddress.getByName("0.0.0.0"));
        System.out.println("[*] Server listening on udp://0.0.0.0:" + Server.PORT);
        
        // Tableau dans lequel on mettera les bytes envoyés par le client
        this.received_bytes = new byte[256];

        while(true) {
            // On récupère le message envoyé par le client
            String received_message = get_client_datagram();

            // To send contiendra le message à envoyer au client 
            byte[] to_send = this.get_to_send_datagram(received_message);
            
            // On envoi au client
            send_datagram(to_send);
        }
    }


    /**
     * Génére et return la réponse du serveur en fonction du message 
     * envoyé par le client et de l'action à utiliser dans ce cas
     * 
     * @param client_message        Message du client (ex: connexion,test,test)
     * @return
     */
    public byte[] get_to_send_datagram(String client_message) {

        String[] words = client_message.split(",");
        String to_send = this.router.get_server_response(words);

        // words[0] => la commande envoyée par le client
        return to_send.getBytes();
    }


    /**
     * Permet d'envoyer un datagramme au client UDP
     * 
     * @param sent_bytes
     * @throws IOException
     */
    public void send_datagram(byte[] sent_bytes) throws IOException {
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
    public String get_client_datagram() throws IOException {
        this.received = new DatagramPacket(this.received_bytes, this.received_bytes.length);
        this.socket.receive(this.received);

        // Récupération du message envoyé par le client
        return new String(this.received.getData(), 0, this.received.getLength()).strip();
    }

    
    /* --------------------- UTILS --------------------- */
    /*                                                   */
    /*                 Fonctions utiles                  */
    /*                                                   */
    /* ------------------------------------------------- */


    /**
     * Renvoi l'id dans le tableau users[] correspondant au user ayant comme 
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

    public void close() {
        if(!socket.isClosed()) socket.close();
    }
}
