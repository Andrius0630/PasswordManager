/**
 * andrius.kolenda@mif.stud.vu.lt
 * Purpose: File for controlling the logged page
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import vu.oop.passwordmanager.service.EncryptionAlgorithm;

import java.io.IOException;

public class LoggedController {
    @FXML private Label usernameLabelTEMP;
    @FXML private Label passwordLabelTEMP;


    @FXML
    protected void displayName(String username, String usernameEncrypted) {
        usernameLabelTEMP.setText("Welcome, " + username + " (" + usernameEncrypted + ")");
    }

    @FXML
    protected void displayPassword(String password) {
        passwordLabelTEMP.setText(password);
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.AUTH_FILE);
    }
}
