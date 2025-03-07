module org.example.ptusa_log {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;


    opens org.example.ptusa_log to javafx.fxml;
    exports org.example.ptusa_log;
    exports org.example.ptusa_log.controllers;
    opens org.example.ptusa_log.controllers to javafx.fxml;
    opens org.example.ptusa_log.models to javafx.base;
}