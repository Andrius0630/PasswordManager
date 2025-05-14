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
    private static final String PATH = "/vu/oop/passwordmanager/";
    public static final String PATH_FXML = PATH + "FXMLFiles/";
    public static final String AUTH_FILE = "Auth";
    public static final String LOGGED_FILE = "Logged";
    public static final String ICONS_PATH = PATH + "icons/";
    public static final String CSS_PATH = PATH + "CSS/";

    public static void sceneSwitchToAnotherRoot(ActionEvent event, Parent root) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public static void sceneSwitchToAnotherFXML(ActionEvent event, String name) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScenesManager.class.getResource(PATH_FXML + name + ".fxml")));
        Scene scene = new Scene(root);
        String css = ScenesManager.class.getResource(ScenesManager.CSS_PATH + name + ".css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
}
