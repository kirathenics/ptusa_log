package org.example.ptusa_log.services;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.example.ptusa_log.models.LogRecord;
import org.example.ptusa_log.utils.WindowCreator;

import java.nio.file.Path;

public class NotificationService {
    public static void showNotification(LogRecord record, Path path) {
        Notifications.create()
                .title("Критический лог: " + path.getFileName())
                .text(record.getMessage())
                .onAction(e -> WindowCreator.openLogWindow(path))
                .hideAfter(Duration.seconds(10))
                .position(Pos.BOTTOM_RIGHT)
                .showError();
    }
}
