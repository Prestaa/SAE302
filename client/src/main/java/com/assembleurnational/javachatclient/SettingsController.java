package com.assembleurnational.javachatclient;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsController {

    public static String ip_addr = "127.0.0.1";
    public static int port = 1337;

    @FXML
    private TextField portInput;

    @FXML
    private TextField ipAddrInput;

    public void initialize() {
        // Add a listener to ensure the portInput is always an integer
        portInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portInput.setText(oldValue); // Restore previous value if input is invalid
            }
        });
    }

    @FXML
    private void apply() throws IOException {
        // Retrieve the IP address and port from input fields
        ip_addr = ipAddrInput.getText(); // Get text from the IP input field
        try {
            port = Integer.parseInt(portInput.getText()); // Parse port input to integer
            System.out.println("IP Address: " + ip_addr);
            System.out.println("Port: " + port);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number entered. Please enter a valid integer.");
        }
        App.setRoot("primary");
    }
}
