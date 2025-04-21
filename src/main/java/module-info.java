module vu.oop.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    exports vu.oop.passwordmanager.app;
    opens vu.oop.passwordmanager.app to javafx.fxml;
    exports vu.oop.passwordmanager.controller;
    opens vu.oop.passwordmanager.controller to javafx.fxml;
}