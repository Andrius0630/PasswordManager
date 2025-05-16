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
import vu.oop.passwordmanager.db.ApiDB;

import java.io.IOException;
import java.sql.SQLException;

public class AuthController {
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;


    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();

        try (ApiDB db = new ApiDB(username, password)) {
            if (db.getConnection() != null) {
                System.out.println("[DEBUG] ApiDB instance created and connected.");

                db.createTABLES(username, password);

                db.populateUSER_PASSWORDS("google.com", "Username", "Password123");

                db.getTABLE(String.format("%s_pass", username));

            }
            else {
                System.err.println("[DEBUG] ApiDB connection failed upon creation.");
            }
        }
        catch (SQLException e) {
            System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB:");
            if (e.getErrorCode()==19) {
                // CONTROLLER CODE TO INFORM USER OF ERROR [NOT UNIQUE] // FRONTEND
                // ... //
                // ... //
            }
            else e.printStackTrace();
        }
        catch (Exception e) {
            System.err.println("[DEBUG] An unexpected exception occurred:");
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenesManager.PATH_FXML + ScenesManager.LOGGED_FILE + ".fxml"));
        Parent root = loader.load();
        LoggedController loggedController = loader.getController();
        loggedController.displayName(username);
        loggedController.displayPassword(password);

        ScenesManager.sceneSwitchToAnotherRoot(event, root);
    }
}