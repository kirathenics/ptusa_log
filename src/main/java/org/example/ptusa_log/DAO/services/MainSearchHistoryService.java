package org.example.ptusa_log.DAO.services;

import org.example.ptusa_log.DAO.MainSearchHistoryDAO;
import org.example.ptusa_log.models.MainSearchHistoryQuery;

import java.util.List;
import java.util.stream.Collectors;

public class MainSearchHistoryService extends SearchHistoryService {
    @Override
    public List<String> getSearchHistory(String query, int visibility, int limit) {
        return MainSearchHistoryDAO.getSearchHistory(query, visibility, limit)
                .stream()
                .map(MainSearchHistoryQuery::getQuery)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSearchHistory() {
        return MainSearchHistoryDAO.getSearchHistory()
                .stream()
                .map(MainSearchHistoryQuery::getQuery)
                .collect(Collectors.toList());
    }

    @Override
    public void saveQuery(String query, int visibility) {
        if (query == null || query.isBlank()) return;

        if (!MainSearchHistoryDAO.findByQueryAndVisibility(query, visibility)) {
            MainSearchHistoryDAO.addQuery(query, visibility);
        }
    }

    @Override
    public void deleteHistoryQuery(String query, int visibility) {
        MainSearchHistoryDAO.deleteHistoryQuery(query, visibility);
    }
}
