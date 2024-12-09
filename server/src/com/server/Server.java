package com.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    private DatagramSocket socket;
    private DatagramPacket received;
    private DatagramPacket sent;
    
    private byte[] received_bytes;
    
    public Server() {}

    public void run() throws SocketException, IOException {
        this.socket = new DatagramSocket(1337);
        System.out.println("[*] Server listening on udp://0.0.0.0:1337");
        
        // Tableau dans lequel on mettera les bytes envoyés par le client
        this.received_bytes = new byte[256];

        // Récéption des paquets du client
        while(true) {
            this.received = new DatagramPacket(this.received_bytes, this.received_bytes.length);
            this.socket.receive(this.received);

            // Récupération du message envoyé par le client
            String received_message = new String(this.received.getData(), 0, this.received.getLength());
            System.out.println(received_message);

            // Envoi de la réponse
            byte[] sent_bytes = this.get_message().getBytes();
            send_message(sent_bytes);
        }
    }

    public void close() {
        if(!socket.isClosed()) socket.close();
    }

    public String get_message() {
        return "Message bien reçu";
    }

    public void send_message(byte[] sent_bytes) throws IOException {
        InetAddress client_addr = this.received.getAddress();
        int client_port = this.received.getPort();

        this.sent = new DatagramPacket(sent_bytes, sent_bytes.length, client_addr, client_port);
        this.socket.send(this.sent);
    }
}
