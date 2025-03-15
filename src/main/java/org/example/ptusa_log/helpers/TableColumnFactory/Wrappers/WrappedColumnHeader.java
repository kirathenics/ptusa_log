package org.example.ptusa_log.helpers.TableColumnFactory.Wrappers;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class WrappedColumnHeader {
    public static  <T, L> void makeHeaderWrappable(TableColumn<T, L> column) {
        Label headerLabel = new Label(column.getText());
        headerLabel.setWrapText(true);
        headerLabel.setStyle("-fx-text-alignment: center; -fx-padding: 5px;");

        headerLabel.prefWidthProperty().bind(column.widthProperty().subtract(10));

        column.setGraphic(headerLabel);
    }
}
