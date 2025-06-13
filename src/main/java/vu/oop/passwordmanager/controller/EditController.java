/**
 * Editing of an existing entry page controller
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
        this.ownName = newOwnName;
    }

    protected void setIndex(int newIndex) {
        if (newIndex > 0)
            this.index = newIndex;
        else
            throw new IllegalArgumentException();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public String getUsername() {
        return username.getText();
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public String getPassword() {
        return password.getText();
    }

    public void setPassword(String password) {
        this.password.setText(password);
    }

    public String getWebsite() {
        return website.getText();
    }

    public void setWebsite(String website) {
        this.website.setText(website);
    }
    @FXML
    protected void update(ActionEvent event) throws IOException {
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

    private boolean isNameReserved(String name) {
        for (String reservedName : reservedNames)
            if (name.compareTo(reservedName) == 0 && name.compareTo(ownName) != 0)
                return true;
        return false;
    }

    @FXML
    protected void goBack(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.LOGGED_FILE);

//        Node source = (Node) event.getSource();
//        Stage stage = (Stage) source.getScene().getWindow();
//        stage.close();

//        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenesManager.PATH_FXML + ScenesManager.LOGGED_FILE + ".fxml"));
//        Parent root = loader.load();
//        LoggedController loggedController = loader.getController();
//        ScenesManager.sceneSwitchToAnotherRoot(event, root);
    }
}
