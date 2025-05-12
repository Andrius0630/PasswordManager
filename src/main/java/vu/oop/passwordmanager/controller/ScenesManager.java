/**
 * andrius.kolenda@mif.stud.vu.lt
 * Purpose: File with helper methods for controlling scenes of UI
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ScenesManager {
    public static final String PATH = "/vu/oop/passwordmanager/";
    public static final String AUTH_FILE = "Auth.fxml";
    public static final String LOGGED_FILE = "Logged.fxml";

    public static void sceneSwitchToAnotherRoot(ActionEvent event, Parent root) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void sceneSwitchToAnotherFXML(ActionEvent event, String fxmlName) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScenesManager.class.getResource(PATH + fxmlName)));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
