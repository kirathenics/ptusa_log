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
import org.example.ptusa_log.models.LogFile;
import org.example.ptusa_log.models.LogPriority;
import org.example.ptusa_log.services.LogRecordManager;
import org.example.ptusa_log.services.SessionMonitorService;
import org.example.ptusa_log.utils.Constants;

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

    private final String DEFAULT_INACTIVE_COLOR = "#bcbcbe";

    private Label selectedLabel;

    private LogFile logFile;

    @FXML
    private void goBack() {
        Stage stage = (Stage) backIconButton.getScene().getWindow();
        stage.close();
    }

    public void setLogFile(LogFile logFile) {
        this.logFile = logFile;

        initializeLogRecordServices();
        loadSearchBar();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeLogPriorityFilters();
        initializeLogRecordTable();
    }

    private void initializeLogPriorityFilters() {
        List<LogPriority> logPriorityList = LogPriorityDAO.getPriorities();
        for (LogPriority logPriority : logPriorityList) {
            Label label = createLogPriorityLabel(logPriority);
            logTypeLabelContainer.getChildren().add(label);
        }

        allTypesLabel.setOnMouseClicked(mouseEvent -> resetLogPriorityFilter());
    }

    private void initializeLogRecordTable() {
        logRecordTableView = new LogRecordTableView.Builder().build();
        logTableContainer.getChildren().add(logRecordTableView.getTableContainer());
    }

    private void initializeLogRecordServices() {
        logRecordManager = new LogRecordManager(logFile.getPath(), () -> logRecordTableView.updateTable(logRecordManager.getFilteredLogs()));
        sessionMonitorService = new SessionMonitorService(logFile.getPath(), logRecordManager::updateLogRecords);

        sessionMonitorService.loadInitialLogs();
        sessionMonitorService.startWatching();
    }

    private void loadSearchBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.VIEWS_PATH + "search_bar_view.fxml"));
            loader.load();

            SearchBarController controller = loader.getController();
            controller.setOnSearchQueryChange(logRecordManager::setSearchQuery);

            searchBarContainer.getChildren().add(controller.getRootPane());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Label createLogPriorityLabel(LogPriority logPriority) {
        Label label = new Label(logPriority.getName());
        label.getStyleClass().add("log-priority-label");
        HBox.setMargin(label, new Insets(0, 10, 0, 10));

        label.setOnMouseClicked(mouseEvent -> applyLogPriorityFilter(label, logPriority));

        return label;
    }

    private void applyLogPriorityFilter(Label label, LogPriority logPriority) {
        allTypesLabel.getStyleClass().remove("selected-label");
        allTypesLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");

        if (selectedLabel != null) {
            selectedLabel.getStyleClass().remove("selected-label");
            selectedLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");
        }

        label.getStyleClass().add("selected-label");
        label.setStyle("-fx-text-fill: " + logPriority.getColor() + ";");

        selectedLabel = label;

        logRecordManager.setFilter(logRecord -> logRecord.getPriority().equalsIgnoreCase(logPriority.getName()));
    }

    private void resetLogPriorityFilter() {
        if (selectedLabel != null) {
            selectedLabel.getStyleClass().remove("selected-label");
            selectedLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");
        }

        allTypesLabel.getStyleClass().add("selected-label");
        allTypesLabel.setStyle("-fx-text-fill: black;");

        selectedLabel = null;

        logRecordManager.setFilter(log -> true);
    }
}
