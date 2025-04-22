package vu.oop.passwordmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WindowController {
    @FXML
    private Label lol;

    @FXML
    protected void onHelloButtonClick() {
        lol.setText("Your mom");
    }
}