package com.assembleurnational.javachatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class PrimaryController {

    public static String username;
    public String password;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private void openSettings() throws IOException {
        App.setRoot("settings");
    }

    @FXML
    private void register() {
        username = usernameInput.getText();
        password = passwordInput.getText();

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            Alert a = new Alert(AlertType.WARNING);
            a.setContentText("Username and password cannot be empty.");
            a.show();
            return;
        }

        String registerMessage = "inscription" + "," + username + "," + password;

        try (DatagramSocket socket = new DatagramSocket()) {
            // Set connection timeout to 5 seconds (5000 milliseconds)
            socket.setSoTimeout(5000);

            // Send registration request
            byte[] sentBytes = registerMessage.getBytes();
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
                String[] messplit = message.split(",");

                if (messplit.length > 3 && "ok".equals(messplit[3].trim())) {
                    Alert a = new Alert(AlertType.CONFIRMATION);
                    a.setContentText("Inscription réussie");
                    a.show();
                } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Inscription échouée: " + message);
                    a.show();
                }
            } catch (SocketTimeoutException e) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Connection timed out. Please try again later.");
                a.show();
            }
        } catch (IOException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("An error occurred during registration: " + e.getMessage());
            a.show();
        }
    }
    @FXML
    private void login() {
        username = usernameInput.getText();
        password = passwordInput.getText();

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            Alert a = new Alert(AlertType.WARNING);
            a.setContentText("Username and password cannot be empty.");
            a.show();
            return;
        }

        String loginMessage = "connexion," + username + "," + password;

        try (DatagramSocket socket = new DatagramSocket()) {
            // Set connection timeout to 5 seconds (5000 milliseconds)
            socket.setSoTimeout(5000);

            // Send registration request
            byte[] sentBytes = loginMessage.getBytes();
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
                String[] messplit = message.split(",");

                if (messplit.length > 3 && "ok".equals(messplit[3].trim())) {
                    Alert a = new Alert(AlertType.CONFIRMATION);
                    a.setContentText("Connection successful");
                    a.show();
                    App.setRoot("chatList");
                } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Connection failed: " + message);
                    a.show();
                }
            } catch (SocketTimeoutException e) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Connection timed out. Please try again later.");
                a.show();
            }
        } catch (IOException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("An error occurred during login: " + e.getMessage());
            a.show();
        }
    }
}
