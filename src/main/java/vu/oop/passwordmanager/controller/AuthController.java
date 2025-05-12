/**
 * andrius.kolenda@mif.stud.vu.lt
 * Purpose: File for controlling the authorization page
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class AuthController {
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;


    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenesManager.PATH + ScenesManager.LOGGED_FILE));
        Parent root = loader.load();
        LoggedController loggedController = loader.getController();
        loggedController.displayName(username);
        loggedController.displayPassword(password);

        ScenesManager.sceneSwitchToAnotherRoot(event, root);
    }
}