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

        while(true) {
            // On récupère le message envoyé par le client
            String received_message = get_client_message();
            // On l'affiche
            System.out.println(received_message);

            // To send contiendra le message à envoyer au client 
            byte[] to_send = this.get_to_send_message(received_message.strip());
            
            // On envoie au client
            send_message(to_send);
        }
    }

    public byte[] get_to_send_message(String message) {
        String to_send;

        if(message.equals("test")) {
            to_send = "Test classique\n";
        } else if (message.equals("test2")) {
            to_send = "Test numéro deux\n";
        } else {
            to_send = "Bah tu m'as envoyé ni test ni test2 donc jsp\n";
        }

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
}
