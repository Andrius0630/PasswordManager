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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import vu.oop.passwordmanager.service.EncryptionAlgorithm;
import vu.oop.passwordmanager.util.HelperDB;

public class AuthController {
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;
    @FXML private Label emptyFieldsText;
    @FXML private Label nonExistentUserText;


    @FXML
    protected void login(ActionEvent event) throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            nonExistentUserText.setVisible(false);
            emptyFieldsText.setVisible(true);
            return;
        }



        EncryptionAlgorithm crypt;
        try {
            crypt = new EncryptionAlgorithm(password);
            String encodedUsername = crypt.encrypt(username);
            String encodedPassword = crypt.encrypt(password);

            int userId = HelperDB.isUserExist(encodedUsername, encodedPassword);
            if (userId == 0) {
                emptyFieldsText.setVisible(false);
                nonExistentUserText.setVisible(true);
                return;
            } else
                HelperDB.saveValidUserCredentialsToMemory(encodedUsername, encodedPassword, userId, password);


        }
        catch (Exception e) {
            e.printStackTrace();

        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenesManager.PATH_FXML + ScenesManager.LOGGED_FILE + ".fxml"));
        Parent root = loader.load();
        ScenesManager.sceneSwitchToAnotherRoot(event, root);
    }



    @FXML
    protected void register(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.REGISTER_FILE);
    }
}