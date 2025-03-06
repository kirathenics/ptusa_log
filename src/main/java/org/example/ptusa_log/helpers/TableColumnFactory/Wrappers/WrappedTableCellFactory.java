package org.example.ptusa_log.helpers.TableColumnFactory.Wrappers;

import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class WrappedTableCellFactory {
    public static <T> TableCell<T, String> createWrappedCell() {
        return new TableCell<>() {
            private final Text text = new Text();
            {
                text.wrappingWidthProperty().bind(widthProperty());
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(text);
                }
            }
        };
    }
}
