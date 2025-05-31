module vu.oop.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires java.sql;
    requires org.slf4j;
    exports vu.oop.passwordmanager.app;
    opens vu.oop.passwordmanager.app to javafx.fxml;
    exports vu.oop.passwordmanager.controller;
    opens vu.oop.passwordmanager.controller to javafx.fxml;
    exports vu.oop.passwordmanager.db;
    opens vu.oop.passwordmanager.db to java.sql, org.slf4j;
    exports vu.oop.passwordmanager.util;
    opens vu.oop.passwordmanager.util to javafx.fxml, org.slf4j;
}