/**
 * Authorization page controller
 * Controls the flow from of credentials
 * Passes credentials to the logged page
 * @author Andrius Kolenda
 * @contact: andrius.kolenda@mif.stud.vu.lt
 * @since 2025-06-04
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

import vu.oop.passwordmanager.db.ApiDB;
import vu.oop.passwordmanager.service.EncryptionAlgorithm;
import vu.oop.passwordmanager.util.HelperDomainObject;

public class AuthController {
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;


    @FXML
    protected void login(ActionEvent event) throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();

        EncryptionAlgorithm crypt;
        try {
            crypt = new EncryptionAlgorithm(password);
            username = crypt.encrypt(username);
            password = crypt.encrypt(password);
        
            try (ApiDB db = new ApiDB(username, password)) {
                if (db.getConnection() != null) {
                    System.out.println("[DEBUG] ApiDB instance created and connected.");

                    
                    // Mandatory: Create tables for the user (Stays in onLoginClick)
                    db.createTABLES();

                    // Everything below is just an example of how to use the ApiDB class methods on diffrent buttons.
                    // Example of when pressed to insert new password FRONTEND
                    db.populateTABLE(username + "_pass",
                    new String[] {"domain_name", "domain_username", "domain_password"},
                    new String[] {"google.com", "Username111",      "Password123"}
                    );

                    db.populateTABLE(username + "_pass",                                // Table to populate
                    new String[] {"domain_name", "domain_username", "domain_password"}, // Column names
                    new String[] {"youtube.com", "Username222",     "Password456"}      // Values to insert
                    );

                    // Example of retrieving a table for the user
                    ArrayList<HelperDomainObject> arrayDomains = db.getTABLE(username + "_pass");

                    // Example of deleting a domain's password
                    db.removeTABLEValue(username + "_pass", "password_id", 2);

                    // Debugging output to verify the retrieved domains
                    System.out.println("\n[DEBUG] Retrieved domains:");
                    arrayDomains = db.getTABLE(username + "_pass");
                    for (HelperDomainObject domain : arrayDomains) {
                        System.out.println("[DEBUG] Retrieved domain: " + domain);
                    }

                    // Example of updating a domain's password
                    db.updateTABLEValue(
                        username + "_pass",                                  // Table to update
                        "password_id",                              // Column where the ID is stored
                        1,                                             // ID of the row to update
                        new String[] {"domain_name", "domain_password"},     // Columns to update
                        new String[] {"youtube.com", "NewPassword789"}       // New values for the columns
                    );

                    // Debugging output to verify the updated domains
                    System.out.println("\n[DEBUG] Updated domains:");
                    arrayDomains = db.getTABLE(username + "_pass");
                    for (HelperDomainObject domain : arrayDomains) {
                        System.out.println("[DEBUG] Retrieved domain: " + domain);
                    }

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
            loggedController.displayName(crypt.decrypt(username), username);
            loggedController.displayPassword(crypt.decrypt(password));
            ScenesManager.sceneSwitchToAnotherRoot(event, root);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void register(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.REGISTER_FILE);
    }
}