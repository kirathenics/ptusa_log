package org.example.ptusa_log.DAO.services;

import java.util.List;

public abstract class SearchHistoryService {
    public abstract List<String> getSearchHistory(String query, int id, int limit);
    public abstract List<String> getSearchHistory();
    public abstract void saveQuery(String query, int id);
    public abstract void deleteHistoryQuery(String query, int id);
}
