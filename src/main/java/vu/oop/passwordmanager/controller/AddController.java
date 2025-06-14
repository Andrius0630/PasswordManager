/**
 * Adding a new entry controller
 * @author Andrius Kolenda
 * @contact: andrius.kolenda@mif.stud.vu.lt
 * @since 2025-06-04
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import vu.oop.passwordmanager.util.HelperDB;

import java.io.IOException;
import java.util.ArrayList;

public class AddController {
    @FXML private TextField name;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField website;
    @FXML private Label emptyNameText;
    @FXML private Label reservedNameText;
    private ArrayList<String> reservedNames;

    public void setReservedNames(ArrayList<String> reservedNames) {
        this.reservedNames = reservedNames;
    }

    @FXML
    protected void add(ActionEvent event) throws Exception {
        String newName = name.getText();
        if (!newName.isBlank()) {
            if (!isNameReserved(newName)) {
                String newUsername = username.getText();
                String newPassword = password.getText();
                String newWebsite = website.getText();
                HelperDB.addNewEntry(newName, newUsername, newPassword, newWebsite);
                ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.LOGGED_FILE);
            } else {
                emptyNameText.setVisible(false);
                reservedNameText.setVisible(true);
                return;
            }
        }
        reservedNameText.setVisible(false);
        emptyNameText.setVisible(true);
    }

    private boolean isNameReserved(String name) throws Exception {
        name = HelperDB.encryptString(name);
        for (String reservedName : reservedNames)
            if (name.compareTo(reservedName) == 0)
                return true;
        return false;
    }



    @FXML
    protected void goBack(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.LOGGED_FILE);
    }
}
