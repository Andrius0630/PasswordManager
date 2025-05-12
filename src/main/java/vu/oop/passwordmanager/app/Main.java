/**
 * andrius.kolenda@mif.stud.vu.lt
 * Purpose: File for controlling the authorization page
 */

package vu.oop.passwordmanager.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        String firstFMXL = ScenesManager.AUTH_FILE;
        try {
            Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource(ScenesManager.PATH + firstFMXL))));
            stage.setTitle("Password manager app");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}