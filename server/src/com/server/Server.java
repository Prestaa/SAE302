package com.server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
    private DatagramSocket serverSocket;
    
    public Server() {
        
        try {
            this.serverSocket = new DatagramSocket(1337);
        } catch(SocketException e) {
            System.out.println("Error while binding port 1337");
        }
        System.out.println(this.serverSocket);

    }
}
