package org.example.ptusa_log.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MainSearchHistoryQuery {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty query;
    private final SimpleIntegerProperty visibility;
    private final SimpleStringProperty timestamp;

    public MainSearchHistoryQuery(int id, String query, int visibility, String timestamp) {
        this.id = new SimpleIntegerProperty(id);
        this.query = new SimpleStringProperty(query);
        this.visibility = new SimpleIntegerProperty(visibility);
        this.timestamp = new SimpleStringProperty(timestamp);
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

    public int getVisibility() {
        return visibility.get();
    }

    public SimpleIntegerProperty visibilityProperty() {
        return visibility;
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public SimpleStringProperty createdArProperty() {
        return timestamp;
    }
}
