package org.example.ptusa_log.services;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.example.ptusa_log.controllers.SessionItemController;
import org.example.ptusa_log.models.LogFile;
import org.example.ptusa_log.utils.Constants;

import java.io.IOException;
import java.util.List;

public class GridPaneUpdater {
    private final GridPane sessionItemGridPane;
    private List<LogFile> currentLogFiles;

    private double gridWidth;
    private int lastColumnCount = -1;

    private LogFileManager logFileManager;

    public GridPaneUpdater(GridPane sessionItemGridPane) {
        this.sessionItemGridPane = sessionItemGridPane;
        gridWidth = sessionItemGridPane.getPrefWidth();
    }

    public void setLogManager(LogFileManager logFileManager) {
        this.logFileManager = logFileManager;
    }

    public void updateGridOnResize(double width) {
        gridWidth = width;
        if (currentLogFiles != null) {
            int columnCount = (int) Math.max(1, gridWidth / (SessionItemController.getItemWidth() + 20));

            if (columnCount == lastColumnCount) {
                return;
            }
            lastColumnCount = columnCount;

            updateGrid(currentLogFiles);
        }
    }

    public void updateGrid(List<LogFile> logFiles) {
        this.currentLogFiles = logFiles;
        Platform.runLater(() -> {
            int columnCount = (int) Math.max(1, gridWidth / (SessionItemController.getItemWidth() + 20));

            sessionItemGridPane.getChildren().clear();
            int column = 0, row = 1;
            for (LogFile logFile : logFiles) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.VIEWS_PATH + "session_item_view.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    SessionItemController controller = fxmlLoader.getController();
                    controller.setData(logFile);
                    controller.setLogFileListener(logFileManager::updateLogs);

                    sessionItemGridPane.add(anchorPane, column, row);
                    GridPane.setMargin(anchorPane, new Insets(10));

                    column = (column + 1) % columnCount;
                    if (column == 0) row++;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}