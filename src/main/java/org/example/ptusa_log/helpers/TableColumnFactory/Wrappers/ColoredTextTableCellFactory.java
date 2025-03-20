package org.example.ptusa_log.helpers.TableColumnFactory.Wrappers;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.ptusa_log.DAO.LogPriorityDAO;

public class ColoredTextTableCellFactory {
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

                    text.setStyle("-fx-font-weight: bold;");

                    text.fillProperty().bind(Bindings.when(tableRowProperty().isNotNull()
                                    .and(tableRowProperty().get().selectedProperty()))
                            .then(Color.WHITE)
                            .otherwise(Color.web(LogPriorityDAO.getColorByPriorityName(item))));
                }
            }
        };
    }
}
