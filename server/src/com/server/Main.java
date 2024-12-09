/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.server;

import java.net.SocketException;

/**
 *
 * @author anas
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {	
        Server server = new Server();

        try {
            server.run();
        } catch(SocketException e) {
            System.out.println("[ERROR] " + e);
        } catch(Exception e) {
            System.out.println("[ERROR] " + e);
        }
        server.close();
    }

}
