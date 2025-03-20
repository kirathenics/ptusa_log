package org.example.ptusa_log.helpers.TableViewFactory;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.VBox;

import java.util.List;

public abstract class AbstractTableView<T> {
    protected VBox tableContainer;
    protected TableView<T> tableView;
    protected ObservableList<T> observableList;

    public AbstractTableView() {
        tableContainer = new VBox();
        VBox.setMargin(tableContainer, new Insets(10,0,10,0));

        tableView = new TableView<>();
        tableView.setPlaceholder(new Label("Нет данных для отображения"));

        observableList = FXCollections.observableArrayList();
        tableView.setItems(observableList);

        observableList.addListener((ListChangeListener<T>) change -> Platform.runLater(this::adjustTableSize));

        setupTableSize();
        tableView.getStyleClass().add("custom-table-view");

        tableContainer.getChildren().add(tableView);
    }

    protected abstract AbstractTableView<T> cloneTable();

    public VBox getTableContainer() {
        return tableContainer;
    }

    protected abstract void setupColumns();

    public void setupTableSize() {
        tableView.getItems().addListener((ListChangeListener<? super Object>) change -> tableView.refresh());
    }

    private void adjustTableSize() {
        tableView.setFixedCellSize(-1);

        tableView.applyCss();
        tableView.layout();

        TableViewSkin<?> skin = (TableViewSkin<?>) tableView.getSkin();
        if (skin != null) {
            VirtualFlow<?> flow = (VirtualFlow<?>) skin.getChildren().get(1);
            double totalHeight = tableView.lookup(".column-header-background").getBoundsInLocal().getHeight();
            totalHeight += 5;

            if (tableView.getItems().isEmpty()) {
                totalHeight += 24;
            }

            for (int i = 0; i < tableView.getItems().size(); i++) {
                totalHeight += flow.getCell(i).getBoundsInLocal().getHeight();
            }

            tableView.setPrefHeight(totalHeight);
        }

        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());

        tableView.prefWidthProperty().bind(tableContainer.widthProperty());
    }

    public void updateTable(List<T> items) {
        observableList.setAll(items);

        adjustColumnWidths();

        Platform.runLater(this::adjustTableSize);
        Platform.runLater(() -> Platform.runLater(this::adjustTableSize));
    }

    protected abstract void adjustColumnWidths();
}
