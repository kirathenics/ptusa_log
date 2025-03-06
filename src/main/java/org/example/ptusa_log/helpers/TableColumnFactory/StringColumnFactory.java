package org.example.ptusa_log.helpers.TableColumnFactory;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ptusa_log.helpers.TableColumnFactory.Wrappers.WrappedColumnHeader;

public class StringColumnFactory<T> implements TableColumnFactory<T, String> {
    @Override
    public TableColumn<T, String> createColumn(String title, String property) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        WrappedColumnHeader.makeHeaderWrappable(column);
        return column;
    }
}
