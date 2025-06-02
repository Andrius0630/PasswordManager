/**
 * andrius.kolenda@mif.stud.vu.lt
 * Purpose: File for controlling the logged page
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class RegisterController {
    @FXML
    protected void goBack(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.AUTH_FILE);
    }
}
