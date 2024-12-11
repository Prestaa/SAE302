package com.assembleurnational.javachatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddFriendController {
    @FXML
    private TextField friendInput;

    @FXML
    private Button sendRequestButton;

    @FXML
    private void sendRequest() {
        String friend = friendInput.getText();
        if (friend.isEmpty()) {
            Alert a = new Alert(AlertType.WARNING);
            a.setContentText("Username cannot be empty.");
            a.show();
            return;
        }
        String friendRequestMessage = "demande_ami," + PrimaryController.username + "," + friend;
    
    try (DatagramSocket socket = new DatagramSocket()) {
            // Set connection timeout to 5 seconds (5000 milliseconds)
            socket.setSoTimeout(5000);

            // Send registration request
            byte[] sentBytes = friendRequestMessage.getBytes();
            InetAddress serverAddress = InetAddress.getByName(SettingsController.ip_addr);
            int serverPort = SettingsController.port;
            DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, serverPort);
            socket.send(sendPacket);

            // Receive server reponse
            byte[] receiveBytes = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);

            try {
                socket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] messplit = message.split(",");

                if (messplit.length > 3 && "ok".equals(messplit[4].trim())) {
                    Alert a = new Alert(AlertType.CONFIRMATION);
                    a.setContentText("Friend request sent successfully");
                    a.show();
                } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Friend request failed: " + message);
                    a.show();
                }
            } catch (SocketTimeoutException e) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Connection timed out. Please try again later.");
                a.show();
            }
        } catch (IOException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("An error occurred during the request: " + e.getMessage());
            a.show();
        }
    }
    
    @FXML
    private void back() throws IOException {
        App.setRoot("chatList");
    }
}
