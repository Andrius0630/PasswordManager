/**
 * Authorization page controller
 * Controls the flow from of credentials
 * Passes credentials to the logged page
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
import javafx.scene.control.*;
import javafx.stage.Stage;
import vu.oop.passwordmanager.service.EncryptionAlgorithm;
import vu.oop.passwordmanager.service.RandomPasswordGenerator;
import vu.oop.passwordmanager.util.HelperDB;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PasswordGeneratorController implements Initializable {
    @FXML private CheckBox upper;
    @FXML private CheckBox lower;
    @FXML private CheckBox digits;
    @FXML private CheckBox special;
    @FXML private TextField generatedPassword;
    @FXML private Spinner<Integer> spinnerLength;
    int currentLength;



    @FXML
    protected void generate(ActionEvent event) throws IOException {
        RandomPasswordGenerator generator = new RandomPasswordGenerator();
        String generatedString = generator.generate(currentLength, upper.isSelected(), lower.isSelected(), digits.isSelected(), special.isSelected());
        if (generatedString != null && !generatedString.isBlank())
            generatedPassword.setText(generatedString);
    }

    @FXML
    protected void close(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        upper.setSelected(true);
        lower.setSelected(true);
        digits.setSelected(true);
        special.setSelected(true);
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 100);

        valueFactory.setValue(16);
        spinnerLength.setValueFactory(valueFactory);
        currentLength = spinnerLength.getValue();

        spinnerLength.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
                currentLength = spinnerLength.getValue();
            }
        });
    }
}