package org.example.ptusa_log.helpers.TableViewFactory.TableBuilder;

import org.example.ptusa_log.helpers.TableViewFactory.AbstractTableView;

public abstract class AbstractTableBuilder<T, R extends AbstractTableView<T>> {
    public abstract R build();
}
