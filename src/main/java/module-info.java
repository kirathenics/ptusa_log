module org.example.ptusa_log {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ptusa_log to javafx.fxml;
    exports org.example.ptusa_log;
    exports org.example.ptusa_log.Controlllers;
    opens org.example.ptusa_log.Controlllers to javafx.fxml;
}