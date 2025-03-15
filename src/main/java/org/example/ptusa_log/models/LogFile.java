package org.example.ptusa_log.models;

import javafx.beans.property.SimpleStringProperty;

public class LogFile {
    private final SimpleStringProperty name;
    private final SimpleStringProperty deviceName;

    public LogFile(String name, String deviceName) {
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
        return "LogFile{" +
                "name=" + name.get() +
                ", deviceName=" + deviceName.get() +
                '}';
    }
}
