/**
 * Password card controller
 * Loads appropriate detailed page for information of specific entry
 * Has ability to delete or edit an existing entry
 * @author Andrius Kolenda
 * @contact: andrius.kolenda@mif.stud.vu.lt
 * @since 2025-06-04
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;

public class PasswordCardController {
    @FXML private Label name;
    @FXML private Label username;
    @FXML private Label password;
    @FXML private Label website;
    @FXML private ProgressBar strength;

    protected void displayName(String name) {
        this.name.setText(name);
    }

    protected void displayUsername(String username) {
        this.username.setText(username);
    }

    protected void displayPasswd(String passwd) {
        this.password.setText(passwd);
    }

    protected void displayWebsite(String website) {
        this.website.setText(website);
    }

    protected void displayStrength(double strength) {
        if (strength < 0 || strength > 1)
            throw new IllegalArgumentException();
        this.strength.setProgress(strength);
    }

    @FXML
    protected void editEntry(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, "Edit");
    }
}
