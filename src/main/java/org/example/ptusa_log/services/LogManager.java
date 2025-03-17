package org.example.ptusa_log.services;

import org.example.ptusa_log.models.LogFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LogManager {
    private List<LogFile> allLogFiles = new ArrayList<>();
    private Predicate<LogFile> filterPredicate = log -> true;
    private Comparator<LogFile> sortComparator = Comparator.comparing(LogFile::getAliasName);
    private String searchQuery = "";

    private final Consumer<List<LogFile>> onLogsUpdated; // UI-обновление

    public LogManager(Consumer<List<LogFile>> onLogsUpdated) {
        this.onLogsUpdated = onLogsUpdated;
    }

    public void updateLogs(List<LogFile> newLogs) {
        this.allLogFiles = newLogs;
        applyFiltersAndSort();
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        applyFiltersAndSort();
    }

    public void setFilter(Predicate<LogFile> filter) {
        this.filterPredicate = filter;
        applyFiltersAndSort();
    }

    public void setSorting(Comparator<LogFile> comparator) {
        this.sortComparator = comparator;
        applyFiltersAndSort();
    }

    private void applyFiltersAndSort() {
        List<LogFile> filteredLogs = allLogFiles.stream()
                .filter(log -> log.getAliasName().toLowerCase().contains(searchQuery.toLowerCase())
                        || log.getDeviceName().toLowerCase().contains(searchQuery.toLowerCase()))
                .filter(filterPredicate)
                .sorted(sortComparator)
                .toList();

        onLogsUpdated.accept(filteredLogs);
    }
}
