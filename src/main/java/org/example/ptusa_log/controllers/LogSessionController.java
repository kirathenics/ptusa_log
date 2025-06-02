package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.ptusa_log.DAO.LogPriorityDAO;
import org.example.ptusa_log.helpers.TableViewFactory.LogRecordTableView;
import org.example.ptusa_log.models.Session;
import org.example.ptusa_log.models.LogPriority;
import org.example.ptusa_log.services.LogRecordManager;
import org.example.ptusa_log.services.SessionMonitorService;
import org.example.ptusa_log.utils.StringConstants;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LogSessionController implements Initializable {

    @FXML
    private MaterialIconView backIconButton;

    @FXML
    private HBox searchBarContainer;

    @FXML
    private HBox logTypeLabelContainer;

    @FXML
    private Label allTypesLabel;

    @FXML
    private VBox logTableContainer;

    private LogRecordTableView logRecordTableView;

    private LogRecordManager logRecordManager;
    private SessionMonitorService sessionMonitorService;

    private static final String DEFAULT_INACTIVE_COLOR = "#bcbcbe";
    private static final String ACTIVE_COLOR = "black";

    private Label selectedLabel;

    private Session session;

    @FXML
    private void goBack() {
        Stage stage = (Stage) backIconButton.getScene().getWindow();
        stage.close();
    }

    public void setLogFile(Session session) {
        this.session = session;

        initializeLogRecordServices();
        loadSearchBar();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeLogPriorityFilters();
        initializeLogRecordTable();
    }

    private void initializeLogRecordTable() {
        logRecordTableView = new LogRecordTableView.Builder().build();
        logTableContainer.getChildren().add(logRecordTableView.getTableContainer());
    }

    private void initializeLogRecordServices() {
        logRecordManager = new LogRecordManager(session.getPath(), () -> logRecordTableView.updateTable(logRecordManager.getFilteredLogs()));
        sessionMonitorService = new SessionMonitorService(session.getPath(), logRecordManager::updateLogRecords);

        sessionMonitorService.loadInitialLogs();
        sessionMonitorService.startWatching();
    }

    private void loadSearchBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(StringConstants.VIEWS_PATH + "search_bar_view.fxml"));
            loader.load();

            SearchBarController controller = loader.getController();
            controller.setOnSearchQueryChange(logRecordManager::setSearchQuery);

            searchBarContainer.getChildren().add(controller.getRootPane());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeLogPriorityFilters() {
        List<LogPriority> logPriorityList = LogPriorityDAO.getPriorities();
        logPriorityList.forEach(priority -> logTypeLabelContainer.getChildren().add(createLogPriorityLabel(priority)));

        configureAllTypesLabel();
    }

    private void configureAllTypesLabel() {
        allTypesLabel.setOnMouseClicked(event -> resetLogPriorityFilter());
        setLabelStyle(allTypesLabel, ACTIVE_COLOR, true);
    }

    private Label createLogPriorityLabel(LogPriority logPriority) {
        Label label = new Label(logPriority.getName());
        label.getStyleClass().add("log-priority-label");
        HBox.setMargin(label, new Insets(0, 10, 0, 10));

        label.setOnMouseClicked(event -> applyLogPriorityFilter(label, logPriority));

        return label;
    }

    private void applyLogPriorityFilter(Label label, LogPriority logPriority) {
        deselectLabel(allTypesLabel);
        deselectLabel(selectedLabel);

        setLabelStyle(label, logPriority.getColor(), true);
        selectedLabel = label;

        logRecordManager.setFilter(logRecord -> logRecord.getPriority().equalsIgnoreCase(logPriority.getName()));
    }

    private void resetLogPriorityFilter() {
        deselectLabel(selectedLabel);
        setLabelStyle(allTypesLabel, ACTIVE_COLOR, true);
        selectedLabel = null;

        logRecordManager.setFilter(log -> true);
    }

    private void setLabelStyle(Label label, String color, boolean isSelected) {
        if (label == null) return;
        label.setStyle("-fx-text-fill: " + color + ";");
        if (isSelected) {
            label.getStyleClass().add("selected-item");
        } else {
            label.getStyleClass().remove("selected-item");
        }
    }

    private void deselectLabel(Label label) {
        setLabelStyle(label, DEFAULT_INACTIVE_COLOR, false);
    }
}