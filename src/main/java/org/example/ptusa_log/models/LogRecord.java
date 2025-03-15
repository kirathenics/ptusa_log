package org.example.ptusa_log.models;

import javafx.beans.property.SimpleStringProperty;

public class LogRecord {
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty priority;
    private final SimpleStringProperty message;

    public LogRecord(String date, String time, String priority, String message) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.priority = new SimpleStringProperty(priority);
        this.message = new SimpleStringProperty(message);
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

    public String getPriority() {
        return priority.get();
    }

    public SimpleStringProperty priorityProperty() {
        return priority;
    }

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }

    public static LogRecord parseLine(String line) {
        String[] parts = line.split("\\|;");
        if (parts.length < 4) return null;

        return new LogRecord(parts[0], parts[1], parts[2], parts[3]);
    }
}
