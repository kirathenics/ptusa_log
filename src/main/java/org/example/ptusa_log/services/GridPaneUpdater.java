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

    private double gridWidth;
    private int lastColumnCount = -1;
    private boolean isGridViewSelected = true;

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
        if (isGridViewSelected) {
            int columnCount = calculateColumnCount();
            if (columnCount == lastColumnCount) return;
            lastColumnCount = columnCount;
        }
        updateGrid();
    }

    public void setGridViewSelected(boolean isGridViewSelected) {
        this.isGridViewSelected = isGridViewSelected;
        updateGrid();
    }

    public void updateGrid() {
        if (logFileManager == null) return;
        List<LogFile> logFiles = logFileManager.getFilteredLogs();

        Platform.runLater(() -> {
            sessionItemGridPane.getChildren().clear();
            if (isGridViewSelected) {
                updateGridView(logFiles);
            } else {
                updateRowView(logFiles);
            }
        });
    }

    private void updateGridView(List<LogFile> logFiles) {
        lastColumnCount = calculateColumnCount();
        int column = 0, row = 1;
        for (LogFile logFile : logFiles) {
            AnchorPane item = createSessionItem(logFile);
            sessionItemGridPane.add(item, column, row);
            GridPane.setMargin(item, new Insets(10));
            column = (column + 1) % lastColumnCount;
            if (column == 0) row++;
        }
    }

    private void updateRowView(List<LogFile> logFiles) {
        int row = 1;
        for (LogFile logFile : logFiles) {
            AnchorPane item = createSessionItem(logFile);
            item.setPrefWidth(gridWidth - 30);
            item.setMaxWidth(Double.MAX_VALUE);
            sessionItemGridPane.add(item, 0, row++);
            GridPane.setMargin(item, new Insets(10, 0, 10, 0));
        }
    }

    private AnchorPane createSessionItem(LogFile logFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.VIEWS_PATH + "session_item_view.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            SessionItemController controller = fxmlLoader.getController();
            controller.setLogFile(logFile);
            controller.setLogFileListener(logFileManager::updateLogs);
            controller.setHoverEffect(isGridViewSelected);
            return anchorPane;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int calculateColumnCount() {
        return (int) Math.max(1, gridWidth / (SessionItemController.getItemWidth() + 20));
    }
}