package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.ptusa_log.DAO.LogRecordDAO;
import org.example.ptusa_log.DAO.LogTypeDAO;
import org.example.ptusa_log.helpers.TableViewFactory.LogRecordTableView;
import org.example.ptusa_log.models.LogRecord;
import org.example.ptusa_log.models.LogType;
import org.example.ptusa_log.utils.Constants;
import org.example.ptusa_log.utils.UserDialogs;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppController implements Initializable  {

    @FXML
    private FontAwesomeIconView homeSidebarButton;

    @FXML
    private FontAwesomeIconView aboutSidebarButton;

    @FXML
    private HBox logTypeContainer;

    @FXML
    private HBox logTypeLabelContainer;

    @FXML
    private TextField searchField;

    @FXML
    private FontAwesomeIconView searchIconButton;

    @FXML
    private MaterialIconView closeIconButton;

    @FXML
    private Label allTypesLabel;

    @FXML
    private VBox logTableContainer;

    private Label selectedLabel;

    private boolean isSearchVisible = false;

    private LogRecordTableView logRecordTableView;

    private final String ACTIVE_SIDEBAR_ICON_COLOR = "#fec526";
    private final String DEFAULT_SIDEBAR_ICON_COLOR = "#c1c1c1";
    private final String DEFAULT_INACTIVE_COLOR = "#bcbcbe";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSidebarButtons();
        initializeLogTable();
        initializeLogTypeFilters();
        initializeSearchBar();
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

    private void initializeSearchBar() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> updateFilteredRecords());

        searchField.setPrefWidth(0);
        searchField.setVisible(false);
        setSearchVisibility();

        searchIconButton.setOnMouseClicked(event -> toggleSearch());
        closeIconButton.setOnMouseClicked(event -> toggleSearch());
    }

    private void toggleSearch() {
        isSearchVisible = !isSearchVisible;

        double expandedWidth = 400.0;
        double collapsedWidth = 0.0;

        Timeline timeline = new Timeline();

        if (isSearchVisible) {
            searchField.setVisible(true);
            setSearchVisibility();
            searchField.requestFocus();

            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300),
                    new KeyValue(searchField.prefWidthProperty(), expandedWidth, Interpolator.EASE_BOTH)));
        } else {
            setSearchVisibility();

            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300),
                    new KeyValue(searchField.prefWidthProperty(), collapsedWidth, Interpolator.EASE_BOTH)));
            timeline.setOnFinished(e -> searchField.setVisible(false));
            searchField.clear();
        }

        timeline.play();
    }

    private void setSearchVisibility() {
        searchField.setFocusTraversable(isSearchVisible);

        searchIconButton.setVisible(!isSearchVisible);
        searchIconButton.setManaged(!isSearchVisible);

        closeIconButton.setVisible(isSearchVisible);
        closeIconButton.setManaged(isSearchVisible);
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
            logTypeLabelContainer.getChildren().add(label);
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
        allTypesLabel.getStyleClass().remove("selected-label");
        allTypesLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");

        if (selectedLabel != null) {
            selectedLabel.getStyleClass().remove("selected-label");
            selectedLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");
        }

        label.getStyleClass().add("selected-label");
        label.setStyle("-fx-text-fill: " + logType.getColor() + ";");

        selectedLabel = label;

        updateFilteredRecords();
    }

    private void resetLogTypeFilter() {
        if (selectedLabel != null) {
            selectedLabel.getStyleClass().remove("selected-label");
            selectedLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");
        }

        allTypesLabel.getStyleClass().add("selected-label");
        allTypesLabel.setStyle("-fx-text-fill: black;");

        selectedLabel = null;

        updateFilteredRecords();
    }

    private void updateFilteredRecords() {
        String searchText = searchField.getText().toLowerCase();
        String selectedType = selectedLabel != null ? selectedLabel.getText() : null;

        List<LogRecord> allRecords = LogRecordDAO.loadRecords();

        List<LogRecord> filteredRecords = allRecords.stream()
                .filter(record -> (selectedType == null || selectedType.equals("Все") || record.getType().equals(selectedType)))
                .filter(record -> record.getDate().toLowerCase().contains(searchText) ||
                        record.getTime().toLowerCase().contains(searchText) ||
                        record.getType().toLowerCase().contains(searchText) ||
                        record.getMessage().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        logRecordTableView.updateTable(filteredRecords);
    }

    private void setActiveIcon(FontAwesomeIconView activeIcon) {
        homeSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));
        aboutSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));

        activeIcon.setFill(Color.web(ACTIVE_SIDEBAR_ICON_COLOR));
    }
}
