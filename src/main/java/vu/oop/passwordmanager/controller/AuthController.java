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
import java.sql.SQLException;
import java.util.ArrayList;

import vu.oop.passwordmanager.db.HelperDomainObject;
import vu.oop.passwordmanager.db.ApiDB;

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

                // Mandatory: Create tables for the user (Stays in onLoginClick)
                db.createTABLES(username, password);

                // Example of when pressed to insert new password FRONTEND
                db.populateTABLE(username+"_pass", "google.com", "Username", "Password123"); 
                db.populateTABLE(username+"_pass", "google.net", "Username222", "Password155");

                // Example of retrieving a table for the user
                ArrayList<HelperDomainObject> arrayDomains = db.getTABLE(username + "_pass");

                // Example of deleting a domain's password
                db.removeTABLEValue(username + "_pass", 2);

                // Debugging output to verify the retrieved domains
                // println("[DEBUG] Retrieved domains:");
                // arrayDomains = db.getTABLE(username + "_pass");
                // for (HelperDomainObject domain : arrayDomains) {
                //     System.out.println("[DEBUG] Retrieved domain: " + domain);
                // }

                // Example of updating a domain's password
                db.updateTABLEValue(username + "_pass", 1, "google.com", "NewUsername", "NewPassword123");

                // Debugging output to verify the updated domains
                // println("[DEBUG] Updated domains:");
                // arrayDomains = db.getTABLE(username + "_pass");
                // for (HelperDomainObject domain : arrayDomains) {
                //     System.out.println("[DEBUG] Retrieved domain: " + domain);
                // }

            }
            else {
                System.err.println("[DEBUG] ApiDB connection failed upon creation.");
            }
        }
        catch (SQLException e) {
            System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB:");
            if (e.getErrorCode()==19) {
                // CONTROLLER CODE TO INFORM USER OF ERROR [ERROR: NOT UNIQUE] // FRONTEND TODO
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