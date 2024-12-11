package com.assembleurnational.javachatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

public class FriendRequestsController {

    @FXML
    private ListView<String> listView;

    String username = PrimaryController.username;

    @FXML
    public void initialize() {
        fetchAndDisplayRequests();

        listView.setOnMouseClicked(event -> { 
            String selectedRequest = listView.getSelectionModel().getSelectedItem(); // Get clicked request
            if (selectedRequest != null) {
                handleFriendRequest(selectedRequest);
            }
        });
    }

    private void fetchAndDisplayRequests() {
        List<String> requests = fetchRequests();
        System.out.println("Fetched requests: " + requests);

    
        if (requests != null) {
            for (String request : requests) {
                listView.getItems().add(request);
            }
        }
    }
    

    private List<String> fetchRequests() {
        List<String> requests = new ArrayList<>();
        boolean suite = true;
        int i = 0;

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000); // 5 seconds timeout

            while (suite) { // As long as there is a request after this one, keep fetching
                String fetchMessage = "recuperer_demande" + "," + username + "," + i;

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
                    System.out.println("Server response: " + message);
                    String[] messplit = message.split(",");

                    if (messplit.length > 3) {
                        String status = messplit[messplit.length - 1].trim();
                        if ("pas_de_demande_ami".equals(status)) {
                            suite = false; // No more requests to process
                        } else if (!"erreur".equals(status)) {
                            requests.add(messplit[3]);
                            i++; // Increment to fetch the next request
                        } else {
                            showErrorAlert("Fetching failed: " + message);
                            suite = false; // Stop processing on error
                        }
                    } else {
                        showErrorAlert("Unexpected server response format: " + message);
                        suite = false; // Stop processing on unexpected format
                    }
                } catch (SocketTimeoutException e) {
                    showErrorAlert("Connection timed out. Please try again later.");
                    suite = false; // Stop processing on timeout
                }
            }
        } catch (IOException e) {
            showErrorAlert("An error occurred during fetching: " + e.getMessage());
        }

        return requests.isEmpty() ? null : requests; // Returns nothing OR the requests array
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void handleFriendRequest(String request) { // Create dialog to accept/deny friend request
        Alert decisionDialog = new Alert(Alert.AlertType.CONFIRMATION);
        decisionDialog.setTitle("Friend Request");
        decisionDialog.setHeaderText("Friend Request from: " + request);
        decisionDialog.setContentText("Do you want to accept or deny this request?");

        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType denyButton = new ButtonType("Deny");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        decisionDialog.getButtonTypes().setAll(acceptButton, denyButton, cancelButton);

        decisionDialog.showAndWait().ifPresent(response -> {
            if (response == acceptButton) {
                answerRequest(request, "oui");
            } else if (response == denyButton) {
                answerRequest(request, "non");
            }
        });
    }

    private void answerRequest(String request, String action) {
        String requestMessage = "accepter_demande,"+ request +","+username+","+action;
        System.out.println("To send : " + requestMessage);
        try (DatagramSocket socket = new DatagramSocket()) {
            // Set connection timeout to 5 seconds (5000 milliseconds)
            socket.setSoTimeout(5000);

            // Send request
            byte[] sentBytes = requestMessage.getBytes();
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

                if (messplit.length > 3 && "ok".equals(messplit[messplit.length-1].trim())) {
                    Alert a = new Alert(AlertType.CONFIRMATION);
                    a.setContentText("Friend request answer sent successfully");
                    a.show();
                } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Friend request answer failed: " + message);
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
