package org.example.ptusa_log.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Session {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty path;
    private final SimpleStringProperty aliasName;
    private final SimpleStringProperty deviceName;
    private final SimpleIntegerProperty visibility;
    private final SimpleStringProperty created_at;

    public Session(Integer id, String path, String aliasName, String deviceName, Integer visibility, String created_at) {
        this.id = new SimpleIntegerProperty(id);
        this.path = new SimpleStringProperty(path);
        this.aliasName = new SimpleStringProperty(aliasName);
        this.deviceName = new SimpleStringProperty(deviceName);
        this.visibility = new SimpleIntegerProperty(visibility);
        this.created_at = new SimpleStringProperty(created_at);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public String getAliasName() {
        return aliasName.get();
    }

    public SimpleStringProperty aliasNameProperty() {
        return aliasName;
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public SimpleStringProperty deviceNameProperty() {
        return deviceName;
    }

    public int getVisibility() {
        return visibility.get();
    }

    public SimpleIntegerProperty visibilityProperty() {
        return visibility;
    }

    public String getCreated_at() {
        return created_at.get();
    }

    public SimpleStringProperty created_atProperty() {
        return created_at;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", path=" + path +
                ", aliasName=" + aliasName +
                ", deviceName=" + deviceName +
                ", visibility=" + visibility +
                ", created_at=" + created_at +
                '}';
    }
}
