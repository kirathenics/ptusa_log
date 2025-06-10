package org.example.ptusa_log.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ptusa_log.DAO.SessionsDAO;
import org.example.ptusa_log.controllers.LogSessionController;
import org.example.ptusa_log.models.Session;

import java.nio.file.Path;

public class WindowCreator {
    public static void openLogWindow(Path logPath) {
        try {
            Session session = SessionsDAO.findSessionByFilePath(logPath.toString());

            FXMLLoader loader = new FXMLLoader(WindowCreator.class.getResource(StringConstants.VIEWS_PATH + "log_session_view.fxml"));
            Parent root = loader.load();

            LogSessionController controller = loader.getController();
            controller.setLogFile(session);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(session.getAliasName());
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
