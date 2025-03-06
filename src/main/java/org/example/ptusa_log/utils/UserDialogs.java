package org.example.ptusa_log.utils;

import javafx.scene.control.Alert;

public class UserDialogs {
    public static void showInfo(String title, String message) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(title);
        info.setHeaderText(null);
        info.setContentText(message);
        info.showAndWait();
    }
}
