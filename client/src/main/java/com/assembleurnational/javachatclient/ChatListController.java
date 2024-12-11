package com.assembleurnational.javachatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;

public class ChatListController {
    String username = PrimaryController.username;

    @FXML
    private ListView<String> listView;

    @FXML
    public void initialize() {
        fetchAndDisplayFriends();
    }

    private void fetchAndDisplayFriends() {
        String[] amis = fetchFriends();
        System.out.println("Fetched friends: " + Arrays.toString(amis));

        if (amis != null) {
            for (String ami : amis) {
                listView.getItems().add(ami);
            }
        }
    }

    private String[] fetchFriends() {
        String fetchMessage = "recuperer_amis" + "," + username;
    
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000); // 5 seconds
    
            // Send request
            byte[] sentBytes = fetchMessage.getBytes();
            InetAddress serverAddress = InetAddress.getByName(SettingsController.ip_addr);
            int serverPort = SettingsController.port;
            DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, serverPort);
            socket.send(sendPacket);
    
            // Receive server response
            byte[] receiveBytes = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
    
            try {
                socket.receive(receivePacket);
    
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(message);
                String[] messplit = message.split(",");
                
                if (messplit.length > 3) {
                    if (!"erreur".equals(messplit[messplit.length - 1].trim())) {
                        int startIndex = 3;
                        int friendsCount = 0;
    
                        // Calculate the number of friends
                        for (int i = startIndex; i < messplit.length && !"null".equals(messplit[i]); i++) {
                            friendsCount++;
                        }
    
                        String[] amis = new String[friendsCount];
                        for (int i = 0; i < friendsCount; i++) {
                            amis[i] = messplit[startIndex + i];
                            System.out.println(Arrays.toString(amis));
                        }
                        return amis;
                    } else {
                        showErrorAlert("Fetching failed: " + message);
                    }
                } else {
                    showErrorAlert("Unexpected server response format: " + message);
                }
            } catch (SocketTimeoutException e) {
                showErrorAlert("Connection timed out. Please try again later.");
            }
        } catch (IOException e) {
            showErrorAlert("An error occurred during fetching: " + e.getMessage());
        }
        return null;
    }
    

    private void showErrorAlert(String message) {
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText(message);
        a.show();
    }

    @FXML
    private void addFriendView() throws IOException {
        App.setRoot("addFriend");
    }
}
