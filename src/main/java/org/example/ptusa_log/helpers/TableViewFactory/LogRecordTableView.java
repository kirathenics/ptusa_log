package org.example.ptusa_log.helpers.TableViewFactory;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import org.example.ptusa_log.helpers.TableColumnFactory.MultilineStringColumnFactory;
import org.example.ptusa_log.helpers.TableViewFactory.TableBuilder.AbstractTableBuilder;
import org.example.ptusa_log.models.LogRecord;
import org.example.ptusa_log.utils.Constants;

import java.util.Arrays;

public class LogRecordTableView extends AbstractTableView<LogRecord> {
    private TableColumn<LogRecord, String> dateTimeColumn;
    private TableColumn<LogRecord, String> typeColumn;
    private TableColumn<LogRecord, String> infoColumn;

    @Override
    protected AbstractTableView<LogRecord> cloneTable() {
        LogRecordTableView copy = new LogRecordTableView();
        copy.updateTable(FXCollections.observableArrayList(this.observableList));
        return copy;
    }

    @Override
    protected void setupColumns() {
        dateTimeColumn = new MultilineStringColumnFactory<LogRecord>().createColumn(Constants.DATE_TIME_LABEL, "dateTime");
        typeColumn = new MultilineStringColumnFactory<LogRecord>().createColumn(Constants.TYPE_LABEL, "type");
        infoColumn = new MultilineStringColumnFactory<LogRecord>().createColumn(Constants.MESSAGE_LABEL, "message");

        tableView.getColumns().addAll(Arrays.asList(
                dateTimeColumn,
                typeColumn,
                infoColumn));
    }

    @Override
    protected void adjustColumnWidths(boolean hasAdditionalInfo) {
        double dateTimeColumnWidth = 0.19;
        double typeColumnWidth = 0.19;
        double infoColumnWidth = 0.6;

        dateTimeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(dateTimeColumnWidth));
        typeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(typeColumnWidth));
        infoColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(infoColumnWidth));
    }

    public static class Builder extends AbstractTableBuilder<LogRecord, LogRecordTableView> {
        @Override
        public LogRecordTableView build() {
            return new LogRecordTableView();
        }
    }
}
