package org.example.ptusa_log.DAO.services;

import org.example.ptusa_log.DAO.SessionSearchHistoryDAO;
import org.example.ptusa_log.models.SessionSearchHistoryQuery;

import java.util.List;
import java.util.stream.Collectors;

public class SessionSearchHistoryService extends SearchHistoryService {
    @Override
    public List<String> getSearchHistory(String query, int sessionId, int limit) {
        return SessionSearchHistoryDAO.getSearchHistory(query, sessionId, limit)
                .stream()
                .map(SessionSearchHistoryQuery::getQuery)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSearchHistory() {
        return SessionSearchHistoryDAO.getSearchHistory()
                .stream()
                .map(SessionSearchHistoryQuery::getQuery)
                .collect(Collectors.toList());
    }

    @Override
    public void saveQuery(String query, int sessionId) {
        if (query == null || query.isBlank()) return;

        if (!SessionSearchHistoryDAO.findByQueryAndSessionId(query, sessionId)) {
            SessionSearchHistoryDAO.addQuery(query, sessionId);
        }
    }

    @Override
    public void deleteHistoryQuery(String query, int sessionId) {
        SessionSearchHistoryDAO.deleteHistoryQuery(query, sessionId);
    }
}
