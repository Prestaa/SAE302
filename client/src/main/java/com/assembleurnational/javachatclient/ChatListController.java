package com.assembleurnational.javachatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatListController {
    String username = PrimaryController.username;

    @FXML
    private ListView<String> listView;

    private ScheduledExecutorService messagePoller; // Used for checking messages every 10 seconds

    @FXML
    public void initialize() {

        fetchAndDisplayFriends();

        listView.setOnMouseClicked(event -> {
            String selectedFriend = listView.getSelectionModel().getSelectedItem(); // Get clicked friend
            if (selectedFriend != null) {
                openChat(selectedFriend);
            }
        });
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

            // Receive server reponse
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
                    showErrorAlert("Unexpected server reponse format: " + message);
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
        Platform.runLater(() -> {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText(message);
            a.show();
        });
    }

    @FXML
    private void openChat(String friend) {
        Stage chatStage = new Stage();
        chatStage.setTitle("Chat with " + friend);

        VBox chatLayout = new VBox(10);
        chatLayout.setPadding(new Insets(10));

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);

        TextField chatInput = new TextField();
        chatInput.setPromptText("Hello, World !");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String message = chatInput.getText();
            if (!message.isEmpty()) {
                chatInput.clear();
                sendMessage(friend, message);
                chatArea.appendText(username + ": " + message + "\n");
            }
        });
        String newMessages = getMessages(friend);
        System.out.println("New messages : " + newMessages);

        HBox inputLayout = new HBox(10, chatInput, sendButton);
        inputLayout.setAlignment(Pos.CENTER);

        chatLayout.getChildren().addAll(chatArea, inputLayout);

        Scene chatScene = new Scene(chatLayout, 400, 300);
        chatStage.setScene(chatScene);
        chatStage.show();

        startPollingMessages(chatArea, friend);

        // Stop polling when the chat window is closed
        chatStage.setOnCloseRequest(e -> stopPollingMessages());
    }

    private void startPollingMessages(TextArea chatArea, String friend) {
        messagePoller = Executors.newSingleThreadScheduledExecutor();
        messagePoller.scheduleAtFixedRate(() -> {
            try {
                String messages = getMessages(friend);
                if (messages != null && !messages.isEmpty()) {
                    Platform.runLater(() -> chatArea.setText(messages));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void stopPollingMessages() {
        if (messagePoller != null && !messagePoller.isShutdown()) {
            messagePoller.shutdownNow();
        }
    }

    private void sendMessage(String friend, String message) {
        String sendMessage = "envoi_message," + username + "," + friend + "," + message;
        try (DatagramSocket socket = new DatagramSocket()) {
            // Set connection timeout to 5 seconds (5000 milliseconds)
            socket.setSoTimeout(5000);

            // Send registration request
            byte[] sentBytes = sendMessage.getBytes();
            InetAddress serverAddress = InetAddress.getByName(SettingsController.ip_addr);
            int serverPort = SettingsController.port;
            DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, serverPort);
            socket.send(sendPacket);

            // Receive server reponse
            byte[] receiveBytes = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);

            try {
                socket.receive(receivePacket);

                String recvMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] messplit = recvMessage.split(",");

                if (messplit.length < 3 && !"ok".equals(messplit[4].trim())) {
                    showErrorAlert("Friend request failed: " + message);
                }
            } catch (SocketTimeoutException e) {
                                showErrorAlert("Connection timed out. Please try again later.");

            }
        } catch (IOException e) {
            showErrorAlert("An error occurred during message fetching: " + e.getMessage());

        }
    }

    private String getMessages(String friend) {
        StringBuilder messages = new StringBuilder();
        boolean suite = true;
        int id = 0;

        while (suite) {
            String fetchMessage = "demande_message," + username + "," + friend + "," + id;

            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setSoTimeout(5000);
                System.out.println("Sending :" + fetchMessage);
                // Send request
                byte[] sentBytes = fetchMessage.getBytes();
                InetAddress serverAddress = InetAddress.getByName(SettingsController.ip_addr);
                int serverPort = SettingsController.port;
                DatagramPacket sendPacket = new DatagramPacket(sentBytes, sentBytes.length, serverAddress, serverPort);
                socket.send(sendPacket);

                // Receive response
                byte[] receiveBytes = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);

                try {
                    socket.receive(receivePacket);

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Received :" + response);
                    String[] messplit = response.split(",");

                    if (messplit.length >= 7) {
                        if (!"erreur".equals(messplit[messplit.length - 1].trim())) {
                            String sender = messplit[4];
                            String message = messplit[5];
                            messages.append(sender).append(": ").append(message).append("\n");

                            suite = "oui".equals(messplit[6].trim());
                            id++;
                        } else {
                            if (id == 0) {
                                messages.replace(0, messages.length(), "No messages"); // Replace chatbox content by "No messages" if the server returns no messages for current user
                                break;
                            } else {
                                break;
                            }
                        }
                    } else {
                        showErrorAlert("Unexpected server response: " + response);
                        suite = false;
                    }
                } catch (SocketTimeoutException e) {
                    showErrorAlert("Connection timed out while fetching messages.");
                    suite = false;
                }
            } catch (IOException e) {
                showErrorAlert("An error occurred during message fetching: " + e.getMessage());
                suite = false;
            }
        }

        return messages.toString();
    }

    @FXML
    private void deleteFriendView() throws IOException {
        App.setRoot("deleteFriend");
    }

    @FXML
    private void addFriendView() throws IOException {
        App.setRoot("addFriend");
    }

    @FXML
    private void friendRequestsView() throws IOException {
        App.setRoot("friendRequests");
    }

    @FXML
    private void disconnect() throws IOException {
        App.setRoot("primary");
    }
}
