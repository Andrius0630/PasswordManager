/**
 * Main file
 * Opens appropriate authorization page
 * @author Andrius Kolenda
 * @contact: andrius.kolenda@mif.stud.vu.lt
 * @since 2025-06-04
 */

package vu.oop.passwordmanager.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import vu.oop.passwordmanager.controller.ScenesManager;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        String firstFMXL = ScenesManager.AUTH_FILE + ".fxml";
        try {
            Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource(ScenesManager.PATH_FXML + firstFMXL))));
            stage.setTitle("Password manager app");
            Scene scene = new Scene(root);

            String css = this.getClass().getResource(ScenesManager.CSS_PATH + "Global" + ".css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.setResizable(false);

            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vu/oop/passwordmanager/icons/icon.png")));
                stage.getIcons().add(icon);
            } catch (NullPointerException e) {
                System.err.println("Icon not found. Make sure 'icon.png' is in your resources folder.");
            }
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}