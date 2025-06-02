package org.example.ptusa_log.services;

import org.example.ptusa_log.DAO.SessionsDAO;
import org.example.ptusa_log.listeners.LogFileListener;
import org.example.ptusa_log.models.Session;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class LogFileManager {
    private List<Session> allSessions = new ArrayList<>();
    private Predicate<Session> filterPredicate = logFile -> true;
    private Comparator<Session> sortComparator = Comparator.comparing(Session::getAliasName);
    private String searchQuery = "";

    private final LogFileListener logFileListener; // UI-обновление

    public LogFileManager(LogFileListener logFileListener) {
        this.logFileListener = logFileListener;
    }

    public void updateLogs() {
        updateLogs(SessionsDAO.getSessions());
    }

    public void updateLogs(List<Session> newLogs) {
        this.allSessions = newLogs;
        logFileListener.onLogsUpdated();
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        logFileListener.onLogsUpdated();
    }

    public void setFilter(Predicate<Session> filter) {
        this.filterPredicate = filter;
        logFileListener.onLogsUpdated();
    }

    public void setSorting(Comparator<Session> comparator) {
        this.sortComparator = comparator;
        logFileListener.onLogsUpdated();
    }

    public List<Session> getFilteredLogs() {
        return allSessions.stream()
                .filter(log -> log.getAliasName().toLowerCase().contains(searchQuery.toLowerCase())
                        || log.getDeviceName().toLowerCase().contains(searchQuery.toLowerCase()))
                .filter(filterPredicate)
                .sorted(sortComparator)
                .toList();
    }
}
