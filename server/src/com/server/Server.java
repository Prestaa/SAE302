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
            DatagramPacket received_packet = new DatagramPacket(recived_bytes, recived_bytes.length);
            this.socket.receive(received_packet);

            // Récupération du message envoyé par le client
            String message = new String(received_packet.getData(), 0, received_packet.getLength());
            System.out.println(message);

            // Envoi de la réponse
            String response = "Message bien reçu !";
            byte[] sent_bytes = response.getBytes();
            
            InetAddress client_addr = received_packet.getAddress();
            int client_port = received_packet.getPort();

            DatagramPacket send_packet = new DatagramPacket(sent_bytes, sent_bytes.length, client_addr, client_port);
            this.socket.send(send_packet);


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
