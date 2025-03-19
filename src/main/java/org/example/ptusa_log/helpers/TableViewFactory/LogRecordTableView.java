package org.example.ptusa_log.helpers.TableViewFactory;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import org.example.ptusa_log.helpers.TableColumnFactory.MultilineStringColumnFactory;
import org.example.ptusa_log.helpers.TableViewFactory.TableBuilder.AbstractTableBuilder;
import org.example.ptusa_log.models.LogRecord;
import org.example.ptusa_log.utils.Constants;

import java.util.Arrays;

public class LogRecordTableView extends AbstractTableView<LogRecord> {
    private TableColumn<LogRecord, String> dateColumn;
    private TableColumn<LogRecord, String> timeColumn;
    private TableColumn<LogRecord, String> priorityColumn;
    private TableColumn<LogRecord, String> infoColumn;

    public LogRecordTableView() {
        super();
        setupColumns();
    }

    @Override
    protected AbstractTableView<LogRecord> cloneTable() {
        LogRecordTableView copy = new LogRecordTableView();
        copy.updateTable(FXCollections.observableArrayList(this.observableList));
        return copy;
    }

    @Override
    protected void setupColumns() {
        dateColumn = new MultilineStringColumnFactory<LogRecord>().createColumn(Constants.DATE_LABEL, "date");
        timeColumn = new MultilineStringColumnFactory<LogRecord>().createColumn(Constants.TIME_LABEL, "time");
        priorityColumn = new MultilineStringColumnFactory<LogRecord>().createColumn(Constants.TYPE_LABEL, "priority");
        infoColumn = new MultilineStringColumnFactory<LogRecord>().createColumn(Constants.MESSAGE_LABEL, "message");

        tableView.getColumns().addAll(Arrays.asList(
                dateColumn,
                timeColumn,
                priorityColumn,
                infoColumn));
    }

    @Override
    protected void adjustColumnWidths() {
        double dateColumnWidth = 0.1;
        double timeColumnWidth = 0.1;
        double typeColumnWidth = 0.1;
        double infoColumnWidth = 0.67;

        dateColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(dateColumnWidth));
        timeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(timeColumnWidth));
        priorityColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(typeColumnWidth));
        infoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(infoColumnWidth));
    }

    public static class Builder extends AbstractTableBuilder<LogRecord, LogRecordTableView> {
        @Override
        public LogRecordTableView build() {
            return new LogRecordTableView();
        }
    }
}
