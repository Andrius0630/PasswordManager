/**
 * Editing of an existing entry page controller
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

public class EditController {

    @FXML private TextField name;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField website;
    @FXML private Label emptyNameText;
    @FXML private Label reservedNameText;
    private int index;
    private String ownName;
    private ArrayList<String> reservedNames;

    public void setReservedNames(ArrayList<String> reservedNames) {
        this.reservedNames = reservedNames;
    }

    public void setOwnName(String newOwnName) {
        try {
            this.ownName = HelperDB.encryptString(newOwnName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setIndex(int newIndex) {
        if (newIndex > 0)
            this.index = newIndex;
        else
            throw new IllegalArgumentException();
    }

    public void setName(String name) {
        if (name.isBlank())
            throw new IllegalArgumentException();
        this.name.setText(name);
    }

    public void setUsername(String username) {
        if (username.compareTo("none") == 0)
            this.username.setText("");
        else
            this.username.setText(username);
    }

    public void setPassword(String password) {
        if (password.compareTo("none") == 0)
            this.password.setText("");
        else
            this.password.setText(password);
    }


    public void setWebsite(String website) {
        if (website.compareTo("none") == 0)
            this.website.setText("");
        else
            this.website.setText(website);
    }

    @FXML
    protected void update(ActionEvent event) throws Exception {
        String newName = name.getText();
        if (!newName.isBlank()) {
            if (!isNameReserved(newName)) {
                String newUsername = username.getText();
                String newPassword = password.getText();
                String newWebsite = website.getText();
                HelperDB.saveUpdatedEntry(newName, newUsername, newPassword, newWebsite, index);
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
        if (name != null && ownName != null) {
            for (String reservedName : reservedNames)
                if (name.compareTo(reservedName) == 0 && name.compareTo(ownName) != 0)
                    return true;
        }
        return false;

    }

    @FXML
    protected void goBack(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.LOGGED_FILE);
    }
}
