package org.example.ptusa_log.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LogRecord {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty dateTime;
    private final SimpleStringProperty type;
    private final SimpleStringProperty message;

    public LogRecord(int id, String dateTime, String type, String message) {
        this.id = new SimpleIntegerProperty(id);
        this.dateTime = new SimpleStringProperty(dateTime);
        this.type = new SimpleStringProperty(type);
        this.message = new SimpleStringProperty(message);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public SimpleStringProperty dateTimeProperty() {
        return dateTime;
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }
}
