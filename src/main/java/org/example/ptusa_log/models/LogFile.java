package org.example.ptusa_log.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//public class LogFile {
//    private final SimpleStringProperty name;
//    private final SimpleStringProperty deviceName;
//
//    public LogFile(String name, String deviceName) {
//        this.name = new SimpleStringProperty(name);
//        this.deviceName = new SimpleStringProperty(deviceName);
//    }
//
//    public String getName() {
//        return name.get();
//    }
//
//    public SimpleStringProperty nameProperty() {
//        return name;
//    }
//
//    public String getDeviceName() {
//        return deviceName.get();
//    }
//
//    public SimpleStringProperty deviceNameProperty() {
//        return deviceName;
//    }
//
//    @Override
//    public String toString() {
//        return "LogFile{" +
//                "name=" + name.get() +
//                ", deviceName=" + deviceName.get() +
//                '}';
//    }
//}

public class LogFile {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty path;
    private final SimpleStringProperty aliasName;
    private final SimpleStringProperty deviceName;
    private final SimpleIntegerProperty isDeleted;

    public LogFile(Integer id, String path, String aliasName, String deviceName, Integer isDeleted) {
        this.id = new SimpleIntegerProperty(id);
        this.path = new SimpleStringProperty(path);
        this.aliasName = new SimpleStringProperty(aliasName);
        this.deviceName = new SimpleStringProperty(deviceName);
        this.isDeleted = new SimpleIntegerProperty(isDeleted);
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

    public int getIsDeleted() {
        return isDeleted.get();
    }

    public SimpleIntegerProperty isDeletedProperty() {
        return isDeleted;
    }

    @Override
    public String toString() {
        return "LogFile{" +
                "id=" + id +
                ", path=" + path +
                ", aliasName=" + aliasName +
                ", deviceName=" + deviceName +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
