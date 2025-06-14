/**
 * Entry deletion prompt controller
 * @author Andrius Kolenda
 * @contact: andrius.kolenda@mif.stud.vu.lt
 * @since 2025-06-04
 */

package vu.oop.passwordmanager.controller;

import javafx.event.ActionEvent;
import vu.oop.passwordmanager.util.HelperDB;

import java.io.IOException;

public class DeletionPromptController {
    private int deletionIndex;

    public void setDeletionIndex(int deletionIndex) {
        if (deletionIndex > 0)
            this.deletionIndex = deletionIndex;
        else
            throw new IllegalArgumentException();
    }
    public void deleteBtn(ActionEvent event) throws IOException {
        HelperDB.deleteEntry(deletionIndex);
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.LOGGED_FILE);

    }

    public void cancelBtn(ActionEvent event) throws IOException {
        ScenesManager.sceneSwitchToAnotherFXML(event, ScenesManager.LOGGED_FILE);
    }
}
