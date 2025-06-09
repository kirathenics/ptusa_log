module org.example.ptusa_log {
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.materialicons;
    requires java.sql;
    requires com.dlsc.gemsfx;
    requires java.prefs;


    opens org.example.ptusa_log to javafx.fxml;
    exports org.example.ptusa_log;
    exports org.example.ptusa_log.controllers;
    opens org.example.ptusa_log.controllers to javafx.fxml;
    opens org.example.ptusa_log.models to javafx.base;
    exports org.example.ptusa_log.models;
    exports org.example.ptusa_log.listeners;
    exports org.example.ptusa_log.services;
    exports org.example.ptusa_log.DAO.services;
}