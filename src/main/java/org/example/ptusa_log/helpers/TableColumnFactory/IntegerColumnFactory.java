package org.example.ptusa_log.helpers.TableColumnFactory;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ptusa_log.helpers.TableColumnFactory.Wrappers.WrappedColumnHeader;

public class IntegerColumnFactory<T> implements TableColumnFactory<T, Integer> {
    @Override
    public TableColumn<T, Integer> createColumn(String title, String property) {
        TableColumn<T, Integer> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        WrappedColumnHeader.makeHeaderWrappable(column);
        return column;
    }
}
