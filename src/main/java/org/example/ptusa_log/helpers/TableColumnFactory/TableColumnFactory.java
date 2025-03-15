package org.example.ptusa_log.helpers.TableColumnFactory;

import javafx.scene.control.TableColumn;

public interface TableColumnFactory<T, V> {
    TableColumn<T, V> createColumn(String title, String property);
}
