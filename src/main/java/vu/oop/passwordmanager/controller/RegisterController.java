/**
 * Registration page controller
 * @author Andrius Kolenda
 * @contact: andrius.kolenda@mif.stud.vu.lt
 * @since 2025-06-04
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import vu.oop.passwordmanager.db.ApiDB;
import vu.oop.passwordmanager.service.EncryptionAlgorithm;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;
    @FXML private TextField passwordField2;
    @FXML private Label emptyFieldsText;
    @FXML private Label fieldsNotTheSameText;
    @FXML private Label userExistsText;

    @FXML
    protected void register(ActionEvent event) throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();
        String password2 = passwordField2.getText();

        if (username.isBlank() || password.isBlank()) {
            fieldsNotTheSameText.setVisible(false);
            emptyFieldsText.setVisible(true);
            return;
        }

        if (password.compareTo(password2) != 0) {
            emptyFieldsText.setVisible(false);
            fieldsNotTheSameText.setVisible(true);
            return;
        }


        EncryptionAlgorithm crypt;
        try {
            crypt = new EncryptionAlgorithm(password);
            username = crypt.encrypt(username);
            password = crypt.encrypt(password);

            try (ApiDB db = new ApiDB(username, password)) {
                if (db.getConnection() != null) {
                    System.out.println("[DEBUG] ApiDB instance created and connected.");

                    db.createTABLES();

                    db.populateTABLE(username + "_pass",
                            new String[] {"entry_name", "domain_name", "domain_username", "domain_password"},
                            new String[] {crypt.encrypt("Saved_entry_1"), crypt.encrypt("your_website_1.com"), crypt.encrypt("your_username_1"), crypt.encrypt("your_password_1")}
                    );

                    db.populateTABLE(username + "_pass",
                            new String[] {"entry_name", "domain_name", "domain_username", "domain_password"},
                            new String[] {crypt.encrypt("Saved_entry_2"), crypt.encrypt("your_website_2.com"), crypt.encrypt("your_username_2"), crypt.encrypt("your_password_2")}
                    );
                }
                else {
                    System.err.println("[DEBUG] ApiDB connection failed upon creation.");
                }
            }
            catch (SQLException e) {
                if (e.getErrorCode()==19) {
                    fieldsNotTheSameText.setVisible(false);
                    emptyFieldsText.setVisible(false);
                    userExistsText.setVisible(true);
                    return;
                }
                else e.printStackTrace();
            }
            catch (Exception e) {
                System.err.println("[DEBUG] An unexpected exception occurred:");
                e.printStackTrace();
            }

            ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.AUTH_FILE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goBack(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.AUTH_FILE);
    }
}
