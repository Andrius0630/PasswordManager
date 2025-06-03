/**
 * andrius.kolenda@mif.stud.vu.lt
 * Purpose: File for controlling the logged page
 */

package vu.oop.passwordmanager.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import vu.oop.passwordmanager.service.EncryptionAlgorithm;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggedController implements Initializable {
    @FXML private Label usernameLabelTEMP;
    @FXML private Label passwordLabelTEMP;
    @FXML private ListView<String> entiresList;
    @FXML private HBox entriesView;

    String[] testEntries = {
            "entry_1",
            "entry_2",
            "entry_3",
            "entry_4",
            "entry_5",
            "entry_6"
    };


    @FXML
    protected void displayName(String username, String usernameEncrypted) {
        usernameLabelTEMP.setText("Welcome, " + username + " (" + usernameEncrypted + ")");
    }

    @FXML
    protected void displayPassword(String password) {
        passwordLabelTEMP.setText(password);
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.AUTH_FILE);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entiresList.getItems().addAll(testEntries);

        entiresList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenesManager.PATH_FXML + "PasswordCard" + ".fxml"));
                Node loadedDetailedView;
                try {
                    loadedDetailedView = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                PasswordCardController card = loader.getController();
                String currName = entiresList.getSelectionModel().getSelectedItem();
                card.displayName(currName);
                card.displayUsername(currName);
                card.displayPasswd(currName);
                card.displayStrength(0.32);
                card.displayWebsite(currName);

                loadedDetailedView.setStyle("-fx-border-color: #393E46; -fx-border-width: 5px 5px 5px 5px");
                if (entriesView.getChildren().size() > 1)
                    entriesView.getChildren().remove(1);

                entriesView.getChildren().add(loadedDetailedView);
            }
        });
    }

    @FXML
    protected void addNew(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, "Add");
    }
}
