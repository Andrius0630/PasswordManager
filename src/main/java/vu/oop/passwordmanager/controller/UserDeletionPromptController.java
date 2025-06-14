/**
 * User deletion prompt controller
 * @author Andrius Kolenda
 * @contact: andrius.kolenda@mif.stud.vu.lt
 * @since 2025-06-04
 */
package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import vu.oop.passwordmanager.util.HelperDB;

import java.io.IOException;

public class UserDeletionPromptController {
    public void deleteBtn(ActionEvent event) throws IOException {
        HelperDB.deleteUser();
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.AUTH_FILE);
    }

    public void cancelBtn(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.LOGGED_FILE);
    }
}
