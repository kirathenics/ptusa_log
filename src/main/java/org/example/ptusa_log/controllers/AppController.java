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
    private LogRecordTableView logRecordTableView;

    private final String ACTIVE_SIDEBAR_ICON_COLOR = "#fec526";
    private final String DEFAULT_SIDEBAR_ICON_COLOR = "#c1c1c1";
    private final String DEFAULT_LOG_TYPE_TEXT_COLOR = "#bcbcbe";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSidebarButtons();
        initializeLogTable();
        initializeLogTypeFilters();
    }

    private void initializeSidebarButtons() {
        homeSidebarButton.setOnMouseClicked(mouseEvent -> setActiveIcon(homeSidebarButton));

        aboutSidebarButton.setOnMouseClicked(mouseEvent -> {
            setActiveIcon(aboutSidebarButton);
            UserDialogs.showInfo(Constants.ABOUT_PROGRAM, Constants.PROGRAM_INFO);
            setActiveIcon(homeSidebarButton);
        });

        setActiveIcon(homeSidebarButton);
    }

    private void initializeLogTable() {
        logRecordTableView = new LogRecordTableView.Builder().build();
        logRecordTableView.updateTable(LogRecordDAO.loadRecords());
        logTableContainer.getChildren().add(logRecordTableView.getTableContainer());
    }

    private void initializeLogTypeFilters() {
        List<LogType> logTypeList = LogTypeDAO.loadTypes();
        for (LogType logType : logTypeList) {
            Label label = createLogTypeLabel(logType);
            logTypeContainer.getChildren().add(label);
        }

        allTypesLabel.setOnMouseClicked(mouseEvent -> resetLogTypeFilter());
    }

    private Label createLogTypeLabel(LogType logType) {
        Label label = new Label(logType.getName());
        label.getStyleClass().add("log-type-label");
        HBox.setMargin(label, new Insets(0, 10, 0, 10));

        label.setOnMouseClicked(mouseEvent -> applyLogTypeFilter(label, logType));

        return label;
    }

    private void applyLogTypeFilter(Label label, LogType logType) {
        allTypesLabel.setStyle("-fx-text-fill: " + DEFAULT_LOG_TYPE_TEXT_COLOR + ";");
        allTypesLabel.getStyleClass().remove("selected-label");

        if (selectedLabel != null) {
            selectedLabel.getStyleClass().remove("selected-label");
            selectedLabel.setStyle("-fx-text-fill: " + DEFAULT_LOG_TYPE_TEXT_COLOR + ";");
        }

        label.getStyleClass().add("selected-label");
        label.setStyle("-fx-text-fill: " + logType.getColor() + ";");

        selectedLabel = label;

        logRecordTableView.updateTable(LogRecordDAO.filterRecords(logType.getName()));
    }

    private void resetLogTypeFilter() {
        if (selectedLabel != null) {
            selectedLabel.getStyleClass().remove("selected-label");
            selectedLabel.setStyle("-fx-text-fill: " + DEFAULT_LOG_TYPE_TEXT_COLOR + ";");
        }

        allTypesLabel.getStyleClass().add("selected-label");
        allTypesLabel.setStyle("-fx-text-fill: black;");

        logRecordTableView.updateTable(LogRecordDAO.loadRecords());
    }

    private void setActiveIcon(FontAwesomeIconView activeIcon) {
        homeSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));
        aboutSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));

        activeIcon.setFill(Color.web(ACTIVE_SIDEBAR_ICON_COLOR));
    }
}
