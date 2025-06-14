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
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;
import vu.oop.passwordmanager.util.HelperDB;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PasswordCardController implements Initializable {
    @FXML private Label name;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField website;
    @FXML private ProgressBar strength;
    @FXML private Label meter;
    private int index;
    private ArrayList<String> reservedNames;
    private Nbvcxz nbvcxz;

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
            this.username.setText("none");
        else {
            this.username.setText(username);
            this.username.setVisible(true);
        }
    }

    protected void displayPasswd(String passwd) {
        if (passwd == null || passwd.isBlank())
            this.password.setText("none");
        else {
            this.password.setText(passwd);
            this.password.setVisible(true);
            displayStrength(password.getText());
        }

    }

    protected void displayWebsite(String website) {
        if (website == null || website.isBlank())
            this.website.setText("none");
        else {
            this.website.setText(website);
            this.website.setVisible(true);
        }
    }

    private void displayStrength(String targetPasswd) {
        if (nbvcxz == null)
            nbvcxz = new Nbvcxz();
        Result result = nbvcxz.estimate(targetPasswd);
        double strengthScore = result.getEntropy();
        double maxEntropyForProgressBar = 100.0;
        double progress = Math.min(1.0, strengthScore / maxEntropyForProgressBar);

        strength.setProgress(progress);

        String strengthText;
        if (strengthScore < 20) {
            strengthText = "Very Weak";
            strength.setStyle("-fx-accent: red;");
        } else if (strengthScore < 40) {
            strengthText = "Weak";
            strength.setStyle("-fx-accent: orangered;");
        } else if (strengthScore < 60) {
            strengthText = "Moderate";
            strength.setStyle("-fx-accent: orange;");
        } else if (strengthScore < 80) {
            strengthText = "Good";
            strength.setStyle("-fx-accent: yellowgreen;");
        } else {
            strengthText = "Strong";
            strength.setStyle("-fx-accent: green;");
        }
        this.meter.setText(strengthText + String.format(" (%.2f score)", strengthScore));
        this.meter.setVisible(true);
        this.strength.setVisible(true);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
