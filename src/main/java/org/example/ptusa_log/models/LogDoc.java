package org.example.ptusa_log.models;

import javafx.beans.property.SimpleStringProperty;

public class LogDoc {
    private final SimpleStringProperty name;
    private final SimpleStringProperty deviceName;

    public LogDoc(String name, String deviceName) {
        this.name = new SimpleStringProperty(name);
        this.deviceName = new SimpleStringProperty(deviceName);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public SimpleStringProperty deviceNameProperty() {
        return deviceName;
    }

    @Override
    public String toString() {
        return "LogDoc{" +
                "name=" + name.get() +
                ", deviceName=" + deviceName.get() +
                '}';
    }
}
