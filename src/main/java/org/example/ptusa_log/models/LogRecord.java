package org.example.ptusa_log.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LogRecord {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty type;
    private final SimpleStringProperty message;

    public LogRecord(int id, String date, String time, String type, String message) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.type = new SimpleStringProperty(type);
        this.message = new SimpleStringProperty(message);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
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
