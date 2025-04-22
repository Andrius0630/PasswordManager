package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoggedController {
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;

    @FXML
    protected void displayName(String username) {
        usernameLabel.setText(username);
    }

    @FXML
    protected void displayPassword(String password) {
        passwordLabel.setText(password);
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vu/oop/passwordmanager/Auth.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
