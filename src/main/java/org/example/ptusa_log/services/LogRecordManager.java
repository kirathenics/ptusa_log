package org.example.ptusa_log.services;

import org.example.ptusa_log.listeners.LogFileListener;
import org.example.ptusa_log.models.LogRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LogRecordManager {
    private List<LogRecord> allLogRecords = new ArrayList<>();
    private Predicate<LogRecord> filterPredicate = log -> true;
//    private Comparator<LogRecord> sortComparator = Comparator.comparing();
    private String searchQuery = "";

    private final LogFileListener logFileListener;
    private final LogFileReader logFileReader;

    public LogRecordManager(String filePath, LogFileListener logFileListener) {
        this.logFileListener = logFileListener;
        logFileReader = new LogFileReader(filePath);
    }

    public void updateLogRecords() {
        allLogRecords.addAll(logFileReader.readNewLines());
        logFileListener.onLogsUpdated();
    }

//    public void updateLogs(List<LogRecord> newLogRecords) {
//        this.allLogRecords = newLogRecords;
//        logFileListener.onLogsUpdated();
//    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        logFileListener.onLogsUpdated();
    }

    public void setFilter(Predicate<LogRecord> filter) {
        this.filterPredicate = filter;
        logFileListener.onLogsUpdated();
    }

//    public void setSorting(Comparator<LogFile> comparator) {
//        this.sortComparator = comparator;
//        logFileListener.onLogsUpdated();
//    }

    public List<LogRecord> getFilteredLogs() {
        return allLogRecords.stream()
                .filter(logRecord -> logRecord.getDate().toLowerCase().contains(searchQuery.toLowerCase())
                        || logRecord.getTime().toLowerCase().contains(searchQuery.toLowerCase())
                        || logRecord.getPriority().toLowerCase().contains(searchQuery.toLowerCase())
                        || logRecord.getMessage().toLowerCase().contains(searchQuery.toLowerCase()))
                .filter(filterPredicate)
//                .sorted(sortComparator)
                .toList();
    }
}
