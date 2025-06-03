/**
 * andrius.kolenda@mif.stud.vu.lt
 * Purpose: File for controlling the logged page
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class EditController {
    @FXML
    protected void goBack(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.LOGGED_FILE);
    }
}
