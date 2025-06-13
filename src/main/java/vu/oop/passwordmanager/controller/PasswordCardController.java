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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class PasswordCardController {
    @FXML private Label name;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField website;
    @FXML private ProgressBar strength;
    private int index;
    private ArrayList<String> reservedNames;

    public void setReservedNames(ArrayList<String> reservedNames) {
        this.reservedNames = reservedNames;
    }

    protected void displayName(String name) {
        if (name == null || name.isBlank())
            this.name.setText("name_placeholder");
        else
            this.name.setText(name);
    }

    protected void displayUsername(String username) {
        if (username == null || username.isBlank())
            this.username.setText("username_placeholder");
        else
            this.username.setText(username);
    }

    protected void displayPasswd(String passwd) {
        if (passwd == null || passwd.isBlank())
            this.password.setText("passwd_placeholder");
        else
            this.password.setText(passwd);
    }

    protected void displayWebsite(String website) {
        if (website == null || website.isBlank())
            this.website.setText("website_placeholder");
        else
            this.website.setText(website);
    }

    protected void displayStrength(double strength) {
        if (strength < 0 || strength > 1)
            throw new IllegalArgumentException();
        this.strength.setProgress(strength);
    }

    protected void setIndex(int newIndex) {
        if (newIndex > 0)
            this.index = newIndex;
        else
            throw new IllegalArgumentException();
    }

    @FXML
    protected void editEntry(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenesManager.PATH_FXML + "Edit" + ".fxml"));
        Parent root = loader.load();
        EditController editController = loader.getController();
        editController.setName(name.getText());
        editController.setUsername(username.getText());
        editController.setPassword(password.getText());
        editController.setWebsite(website.getText());
        editController.setReservedNames(reservedNames);
        editController.setIndex(index);
        editController.setOwnName(name.getText());

//        Stage stage = new Stage();
//        stage.setScene(new Scene(root));
//        stage.show();

        ScenesManager.sceneSwitchToAnotherRoot(event, root);
        System.out.println("Edit!");
    }

    @FXML
    protected void deleteEntry(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenesManager.PATH_FXML + "DeletionPrompt" + ".fxml"));
        Parent root = loader.load();
        DeletionPromptController deletionPromptController = loader.getController();
        deletionPromptController.setDeletionIndex(index);
        ScenesManager.sceneSwitchToAnotherRoot(event, root);
    }

    public void copyPassword(ActionEvent event) {
    }

    public void copyWebsite(ActionEvent event) {
    }

    public void copyUsername(ActionEvent event) {
    }
}
