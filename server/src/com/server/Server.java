package com.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    private DatagramSocket socket;
    
    public Server() {}
    public void run() {
        try {
            this.socket = new DatagramSocket(1337);
            System.out.println("[*] Server listening on udp://0.0.0.0:1337");

            // Récéption des paquets du client
            byte[] recived_bytes = new byte[256];
            DatagramPacket receivedPacket = new DatagramPacket(receivedBytes, receivedBytes.length);
            this.socket.receive(receivedPacket);

            // Récupération du message envoyé par le client
            String message = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
            System.out.println(message);

            // Envoi de la réponse
            String response = "Message bien reçu !";
            byte[] sentBytes = response.getBytes();
            
            InetAddress client_addr = receivedPacket.getAddress()



        } catch(SocketException e) {
            System.out.println("[ERROR] " + e);
        } catch(Exception e) {
            System.out.println("[ERROR] " + e);
        }

        System.out.println(this.serverSocket);
    }

    public void close() {
        if(!serverSocket.isClosed()) serverSocket.close();
    }
}
