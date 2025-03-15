package org.example.ptusa_log.helpers;

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

// TODO: если сделать ширину окна очень узкой, чтобы помещался только один элемент в ряду. то он не выравнивается по центру
public class GridPaneUpdater {
    private final GridPane sessionItemGridPane;

    private List<LogFile> logFiles;

    private int lastColumnCount = -1;
    private double gridWidth;

    public GridPaneUpdater(GridPane sessionItemGridPane) {
        this.sessionItemGridPane = sessionItemGridPane;
    }

    public void updateGrid(List<LogFile> logFiles) {
        this.logFiles = logFiles;

        updateGrid();
    }

    public void updateGridOnResize(double width) {
        gridWidth = width;

        updateGrid();
    }

    private void updateGrid() {
        Platform.runLater(() -> {
            int columnCount = (int) Math.max(1, gridWidth / SessionItemController.getItemWidth());

//        if (columnCount == lastColumnCount) {
//            return;
//        }
            lastColumnCount = columnCount;

            sessionItemGridPane.getChildren().clear();

            int column = 0, row = 1;

            for (LogFile logFile : logFiles) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.VIEWS_PATH + "session_item_view.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    SessionItemController technicItemController = fxmlLoader.getController();
                    technicItemController.setData(logFile);

                    sessionItemGridPane.add(anchorPane, column, row);
                    GridPane.setMargin(anchorPane, new Insets(10));

                    column++;
                    if (column >= columnCount) {
                        column = 0;
                        row++;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}