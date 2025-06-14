/**
 * Logged page controller
 * Displays appropriate name of entries
 * Has ability to log out or add a new entry
 * @author Andrius Kolenda
 * @contact: andrius.kolenda@mif.stud.vu.lt
 * @since 2025-06-04
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
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import vu.oop.passwordmanager.util.HelperDB;
import vu.oop.passwordmanager.util.HelperDomainObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class LoggedController implements Initializable {
    @FXML private ListView<String> entriesList;
    @FXML private HBox entriesView;
    private ArrayList<HelperDomainObject> passwordEntries;
    private ArrayList<String> reservedNames;

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.AUTH_FILE);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordEntries = HelperDB.retrieveUserData();
        if (passwordEntries == null)
            throw new NoSuchElementException();

        reservedNames = new ArrayList<>();
        for (HelperDomainObject entry : passwordEntries) {
            entriesList.getItems().add(HelperDB.decryptString(entry.getEntryName()));
            reservedNames.add(entry.getEntryName());
        }


        entriesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
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
                int index = entriesList.getSelectionModel().getSelectedIndex();
                card.displayName(HelperDB.decryptString(passwordEntries.get(index).getEntryName()));
                card.displayUsername(HelperDB.decryptString(passwordEntries.get(index).getDomainUsername()));
                card.displayPasswd(HelperDB.decryptString(passwordEntries.get(index).getDomainPassword()));
                card.displayWebsite(HelperDB.decryptString(passwordEntries.get(index).getDomainName()));
                card.setIndex(passwordEntries.get(index).getIndex());
                card.setReservedNames(reservedNames);


                loadedDetailedView.setStyle("-fx-border-color: #393E46; -fx-border-width: 5px 5px 5px 5px");
                if (entriesView.getChildren().size() > 1)
                    entriesView.getChildren().remove(1);

                entriesView.getChildren().add(loadedDetailedView);
            }
        });
    }

    @FXML
    protected void addNew(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScenesManager.PATH_FXML + "Add" + ".fxml"));
        Parent root = loader.load();
        AddController addController = loader.getController();
        addController.setReservedNames(reservedNames);
        ScenesManager.sceneSwitchToAnotherRoot(event, root);
    }

    @FXML
    protected void openPasswdGenerator(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.PASSWD_GENERATOR_FILE);
    }

    @FXML
    protected void deleteUser(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.USR_DEL_PRMPT_FILE);
    }
}
