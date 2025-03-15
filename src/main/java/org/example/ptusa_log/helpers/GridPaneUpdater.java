package org.example.ptusa_log.helpers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.example.ptusa_log.controllers.SessionItemController;
import org.example.ptusa_log.models.LogDoc;
import org.example.ptusa_log.utils.Constants;

import java.io.IOException;
import java.util.List;

public class GridPaneUpdater {
    private final GridPane sessionItemGridPane;

    private List<LogDoc> logDocs;

    private int lastColumnCount = -1;
    private double gridWidth;

    public GridPaneUpdater(GridPane sessionItemGridPane) {
        this.sessionItemGridPane = sessionItemGridPane;
    }

    public void updateGrid(List<LogDoc> logDocs) {
        this.logDocs = logDocs;

//        System.out.println(logDocs);
//        System.out.println(this.logDocs);

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

//            System.out.println("------------------------------------------");
            int i = 1;

            for (LogDoc logDoc : logDocs) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.VIEWS_PATH + "session_item_view.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    SessionItemController technicItemController = fxmlLoader.getController();
                    technicItemController.setData(logDoc);

//                    System.out.println(Integer.toString(i++) + " " + logDoc);

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