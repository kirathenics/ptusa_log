package org.example.ptusa_log.services;

import org.example.ptusa_log.DAO.LogFileDAO;
import org.example.ptusa_log.listeners.LogFileListener;
import org.example.ptusa_log.models.LogFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class LogFileManager {
    private List<LogFile> allLogFiles = new ArrayList<>();
    private Predicate<LogFile> filterPredicate = log -> true;
    private Comparator<LogFile> sortComparator = Comparator.comparing(LogFile::getAliasName);
    private String searchQuery = "";

    private final LogFileListener logFileListener; // UI-обновление

    public LogFileManager(LogFileListener logFileListener) {
        this.logFileListener = logFileListener;
    }

    public void updateLogs() {
        updateLogs(LogFileDAO.getLogFiles());
    }

    public void updateLogs(List<LogFile> newLogs) {
        this.allLogFiles = newLogs;
        logFileListener.onLogsUpdated();
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        logFileListener.onLogsUpdated();
    }

    public void setFilter(Predicate<LogFile> filter) {
        this.filterPredicate = filter;
        logFileListener.onLogsUpdated();
    }

    public void setSorting(Comparator<LogFile> comparator) {
        this.sortComparator = comparator;
        logFileListener.onLogsUpdated();
    }

    public List<LogFile> getFilteredLogs() {
        return allLogFiles.stream()
                .filter(log -> log.getAliasName().toLowerCase().contains(searchQuery.toLowerCase())
                        || log.getDeviceName().toLowerCase().contains(searchQuery.toLowerCase()))
                .filter(filterPredicate)
                .sorted(sortComparator)
                .toList();
    }
}
