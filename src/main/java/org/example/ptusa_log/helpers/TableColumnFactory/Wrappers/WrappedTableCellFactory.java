package org.example.ptusa_log.helpers.TableColumnFactory.Wrappers;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
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

                    text.fillProperty().bind(Bindings.when(tableRowProperty().isNotNull()
                                    .and(tableRowProperty().get().selectedProperty()))
                            .then(Color.WHITE)
                            .otherwise(Color.BLACK));
                }
            }
        };
    }
}
