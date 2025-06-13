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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vu.oop.passwordmanager.service.EncryptionAlgorithm;
import vu.oop.passwordmanager.util.HelperDB;

import java.io.IOException;

public class PasswordGeneratorController {
    @FXML TextField generatedPassword;



    @FXML
    protected void generate(ActionEvent event) throws IOException {

    }

    @FXML
    protected void close(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}