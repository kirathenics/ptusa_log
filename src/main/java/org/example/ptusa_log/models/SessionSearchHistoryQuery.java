package org.example.ptusa_log.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SessionSearchHistoryQuery {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty query;
    private final SimpleIntegerProperty sessionId;
    private final SimpleStringProperty createdAt;

    public SessionSearchHistoryQuery(int id, String query, int sessionId, String createdAt) {
        this.id = new SimpleIntegerProperty(id);
        this.query = new SimpleStringProperty(query);
        this.sessionId = new SimpleIntegerProperty(sessionId);
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getQuery() {
        return query.get();
    }

    public SimpleStringProperty queryProperty() {
        return query;
    }

    public int getSessionId() {
        return sessionId.get();
    }

    public SimpleIntegerProperty sessionIdProperty() {
        return sessionId;
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public SimpleStringProperty createdArProperty() {
        return createdAt;
    }
}
