package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.ptusa_log.DAO.LogRecordDAO;
import org.example.ptusa_log.DAO.LogTypeDAO;
import org.example.ptusa_log.helpers.TableViewFactory.LogRecordTableView;
import org.example.ptusa_log.models.LogType;
import org.example.ptusa_log.utils.Constants;
import org.example.ptusa_log.utils.UserDialogs;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AppController implements Initializable  {

    @FXML
    private FontAwesomeIconView homeSidebarButton;

    @FXML
    private FontAwesomeIconView aboutSidebarButton;

    @FXML
    private HBox logTypeContainer;

    @FXML
    private Label allTypesLabel;

    @FXML
    private VBox logTableContainer;

    private Label selectedLabel;

    private final String ACTIVE_COLOR = "#FEC526";
    private final String DEFAULT_COLOR = "#C1C1C1";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeSidebarButton.setOnMouseClicked(mouseEvent -> setActiveIcon(homeSidebarButton));

        aboutSidebarButton.setOnMouseClicked(mouseEvent -> {
            setActiveIcon(aboutSidebarButton);
            UserDialogs.showInfo(Constants.ABOUT_PROGRAM, Constants.PROGRAM_INFO);
            setActiveIcon(homeSidebarButton);
        });

        setActiveIcon(homeSidebarButton);

        LogRecordTableView logRecordTableView = new LogRecordTableView.Builder().build();
        logRecordTableView.updateTable(LogRecordDAO.loadRecords());
        logTableContainer.getChildren().add(logRecordTableView.getTableContainer());

        List<LogType> logTypeList = LogTypeDAO.loadTypes();

        for (LogType logType : logTypeList) {
            Label label = new Label(logType.getName());
            label.getStyleClass().add("log-type-label");
            HBox.setMargin(label, new Insets(0, 10, 0, 10));

            label.setOnMouseClicked(mouseEvent -> {
                allTypesLabel.setStyle("-fx-text-fill: #bcbcbe;");
                if (selectedLabel != null) {
                    selectedLabel.setStyle("-fx-text-fill: #bcbcbe;");
                }
                label.setStyle("-fx-text-fill: " + logType.getColor() + ";");
                selectedLabel = label;

                logRecordTableView.updateTable(LogRecordDAO.filterRecords(logType.getName()));
            });

            logTypeContainer.getChildren().add(label);
        }

        allTypesLabel.setOnMouseClicked(mouseEvent -> {
            if (selectedLabel != null) {
                selectedLabel.setStyle("-fx-text-fill: #bcbcbe;");
            }

            allTypesLabel.setStyle("-fx-text-fill: black;");
            logRecordTableView.updateTable(LogRecordDAO.loadRecords());
        });
    }

    private void setActiveIcon(FontAwesomeIconView activeIcon) {
        homeSidebarButton.setFill(Color.web(DEFAULT_COLOR));
        aboutSidebarButton.setFill(Color.web(DEFAULT_COLOR));

        activeIcon.setFill(Color.web(ACTIVE_COLOR));
    }
}

/*

сделай так, чтобы для создаваемого Label задавался класс стиля css (выравнивание текста по горизонтали и вертикали по центру, размер 13), задавались margin 5 слева и справа, вешалось событие при нажатии на выбранный label текст становился bold, цвет текста менялся на тот, что хранится у объекта LogType, а у предыдущего выбранного (либо просто у всех остальных в зависимости от того, что проще реализовать) убирался эффект выбранного

public class LogRecordDAO {
    private static final List<LogRecord> logRecordList = new ArrayList<>(Arrays.asList(
            new LogRecord(1, "06-03-2025", "20:31:10", "INFO", "Started!"),
            new LogRecord(2, "06-03-2025", "20:31:12", "WARNING", "Do something please"),
            new LogRecord(2, "06-03-2025", "20:31:13", "ERROR", "Something went wrong"),
            new LogRecord(2, "06-03-2025", "20:31:14", "INFO", "Finished!"),
            new LogRecord(2, "06-03-2025", "20:31:18", "INFO", "Finished!")
    ));

    public static List<LogRecord> loadRecords() {
        return logRecordList;
    }

    public static List<LogRecord> filterRecords(String logType) {
        return logRecordList.stream()
                .filter(record -> logType.equals(record.getType()))
                .toList();
    }
}

css
.h1-label {
    -fx-font-size: 14px;
    -fx-font-weight: bold;

    -fx-padding: 5px;
    -fx-border-insets: 5px;
    -fx-background-insets: 5px;
}

.log-type-label {
    -fx-font-size: 12px;
    -fx-font-weight: bold;
    -fx-text-fill: #bcbcbe;

    -fx-alignment: center;
    -fx-text-alignment: center;
}

<HBox fx:id="logTypeContainer" alignment="CENTER_LEFT">
<children>
  <FontAwesomeIconView fill="#bcbcbe" glyphName="BOOK" size="1.7em" styleClass="sidebar-opt" />
  <MaterialIconView fill="#bcbcbe" glyphName="KEYBOARD_ARROW_RIGHT" size="1.5em">
     <HBox.margin>
        <Insets left="7.0" />
     </HBox.margin>
  </MaterialIconView>
  <Label styleClass="h1-label" text="Все">
     <HBox.margin>
        <Insets left="5.0" right="5.0" />
     </HBox.margin>
  </Label>
</children>
<VBox.margin>
  <Insets bottom="10.0" left="15.0" right="10.0" top="10.0" />
</VBox.margin></HBox>

public class AppController implements Initializable  {

    @FXML
    private HBox logTypeContainer;

    @FXML
    private VBox logTableContainer;

    private Label selectedLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LogRecordTableView logRecordTableView = new LogRecordTableView.Builder().build();
        logRecordTableView.updateTable(LogRecordDAO.loadRecords());
        logTableContainer.getChildren().add(logRecordTableView.getTableContainer());

        List<LogType> logTypeList = LogTypeDAO.loadTypes();

        for (LogType logType : logTypeList) {
            Label label = new Label(logType.getName());
            label.getStyleClass().add("log-type-label");
            HBox.setMargin(label, new Insets(0, 10, 0, 10));

            label.setOnMouseClicked(event -> {
                if (selectedLabel != null) {
                    selectedLabel.setStyle("-fx-text-fill: #bcbcbe;");
                }
                label.setStyle("-fx-text-fill: " + logType.getColor() + ";");
                selectedLabel = label;

                logRecordTableView.updateTable(LogRecordDAO.filterRecords(logType.getName()));
            });

            logTypeContainer.getChildren().add(label);
        }
    }
}

 */