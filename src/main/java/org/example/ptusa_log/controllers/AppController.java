package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.ptusa_log.MonitorServices.LogMonitorService;
import org.example.ptusa_log.helpers.GridPaneUpdater;
import org.example.ptusa_log.utils.Constants;
import org.example.ptusa_log.utils.UserDialogs;

import java.net.URL;
import java.util.ResourceBundle;

//public class AppController implements Initializable  {
//
//    @FXML
//    private FontAwesomeIconView homeSidebarButton;
//
//    @FXML
//    private FontAwesomeIconView aboutSidebarButton;
//
//    @FXML
//    private HBox logTypeContainer;
//
//    @FXML
//    private HBox logTypeLabelContainer;
//
//    @FXML
//    private TextField searchField;
//
//    @FXML
//    private FontAwesomeIconView searchIconButton;
//
//    @FXML
//    private MaterialIconView closeIconButton;
//
//    @FXML
//    private FontAwesomeIconView searchIcon;
//
//    @FXML
//    private Label allTypesLabel;
//
//    @FXML
//    private VBox logTableContainer;
//
//    @FXML
//    private VBox sessionContainer;
//
//    private Label selectedLabel;
//
//    private boolean isSearchVisible = false;
//
//    private LogRecordTableView logRecordTableView;
//
//    private final String ACTIVE_SIDEBAR_ICON_COLOR = "#fec526";
//    private final String DEFAULT_SIDEBAR_ICON_COLOR = "#c1c1c1";
//    private final String DEFAULT_INACTIVE_COLOR = "#bcbcbe";
//
//    private static final int READ_INTERVAL = 1; // Интервал обновления в секундах
//    private String logPath;
//    private long lastReadPosition = 0;
////    private final FilteredList<LogRecord> filteredLogData = new FilteredList<>(logData, p -> true);
//    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//    private String deviceName = "Unknown Device";
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        initializeSidebarButtons();
//        initializeLogTable();
//        initializeLogTypeFilters();
//        initializeSearchBar();
//
////        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.VIEW_PATH + "session_item_view.fxml"));
////        AnchorPane anchorPane = null;
////        try {
////            anchorPane = fxmlLoader.load();
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////
////        sessionContainer.getChildren().add(anchorPane);
////        VBox.setMargin(anchorPane, new Insets(10));
//
//        logPath = LogPath.defineLogPath();
//        startLogMonitoring();
//    }
//
//    private void initializeSidebarButtons() {
//        homeSidebarButton.setOnMouseClicked(mouseEvent -> setActiveIcon(homeSidebarButton));
//
//        aboutSidebarButton.setOnMouseClicked(mouseEvent -> {
//            setActiveIcon(aboutSidebarButton);
//            UserDialogs.showInfo(Constants.ABOUT_PROGRAM, Constants.PROGRAM_INFO);
//            setActiveIcon(homeSidebarButton);
//        });
//
//        setActiveIcon(homeSidebarButton);
//    }
//
////    private void initializeSearchBar() {
////        searchField.textProperty().addListener((observable, oldValue, newValue) -> updateFilteredRecords());
////
////        searchField.setPrefWidth(0);
////        searchField.setVisible(false);
////        setSearchVisibility();
////
////        searchIconButton.setOnMouseClicked(event -> toggleSearch());
////        closeIconButton.setOnMouseClicked(event -> toggleSearch());
////    }
//
//    private void initializeSearchBar() {
//        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
//            updateFilteredRecords();
//            closeIconButton.setVisible(!newValue.isEmpty()); // Показывать closeIconButton только если есть текст
//        });
//
//        searchField.setPrefWidth(0);
//        searchField.setVisible(false);
//        closeIconButton.setVisible(false); // Закрытие изначально скрыто
//        searchIcon.setVisible(false);
//
//        searchIconButton.setOnMouseClicked(event -> showSearchField());
//        closeIconButton.setOnMouseClicked(event -> hideSearchField());
//
//        searchField.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ESCAPE) {
//                if (searchField.getText().isEmpty()) {
//                    hideSearchField();
//                } else {
//                    searchField.clear();
//                    closeIconButton.setVisible(false);
//                }
//            }
//        });
//    }
//
//    /* */
//    private void showSearchField() {
//        if (!isSearchVisible) {
//            isSearchVisible = true;
//            searchField.setVisible(true);
//            searchField.setManaged(true);
//            searchField.requestFocus();
//
//            setSearchVisibility();
//
//            Timeline timeline = new Timeline(
//                    new KeyFrame(Duration.millis(300),
//                            new KeyValue(searchField.prefWidthProperty(), 400.0, Interpolator.EASE_BOTH)
//                    )
//            );
//            timeline.play();
//        }
//    }
//
//    /* */
//    private void hideSearchField() {
//        if (isSearchVisible) {
//            isSearchVisible = false;
//            searchField.clear();
//            closeIconButton.setVisible(false);
//
//            setSearchVisibility();
//
//            Timeline timeline = new Timeline(
//                    new KeyFrame(Duration.millis(300),
//                            new KeyValue(searchField.prefWidthProperty(), 0.0, Interpolator.EASE_BOTH)
//                    )
//            );
//            timeline.setOnFinished(e -> {
//                searchField.setVisible(false);
//                searchField.setManaged(false);
//            });
//            timeline.play();
//        }
//    }
//
//
//
////    private void toggleSearch() {
////        isSearchVisible = !isSearchVisible;
////
////        double expandedWidth = 400.0;
////        double collapsedWidth = 0.0;
////
////        Timeline timeline = new Timeline();
////
////        if (isSearchVisible) {
////            searchField.setVisible(true);
////            setSearchVisibility();
////            searchField.requestFocus();
////
////            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300),
////                    new KeyValue(searchField.prefWidthProperty(), expandedWidth, Interpolator.EASE_BOTH)));
////        } else {
////            setSearchVisibility();
////
////            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300),
////                    new KeyValue(searchField.prefWidthProperty(), collapsedWidth, Interpolator.EASE_BOTH)));
////            timeline.setOnFinished(e -> searchField.setVisible(false));
////            searchField.clear();
////        }
////
////        timeline.play();
////    }
//
//    private void setSearchVisibility() {
//        searchField.setFocusTraversable(isSearchVisible);
//
//        searchIconButton.setVisible(!isSearchVisible);
//        searchIconButton.setManaged(!isSearchVisible);
//
//        closeIconButton.setVisible(isSearchVisible);
//        closeIconButton.setManaged(isSearchVisible);
//
//        searchIcon.setVisible(isSearchVisible);
//        searchIcon.setManaged(isSearchVisible);
//    }
//
//    private void initializeLogTable() {
//        logRecordTableView = new LogRecordTableView.Builder().build();
//        logRecordTableView.updateTable(LogRecordDAO.loadRecords());
//        logTableContainer.getChildren().add(logRecordTableView.getTableContainer());
//    }
//
//    private void initializeLogTypeFilters() {
//        List<LogPriority> logPriorityList = LogPriorityDAO.loadTypes();
//        for (LogPriority logPriority : logPriorityList) {
//            Label label = createLogPriorityLabel(logPriority);
//            logTypeLabelContainer.getChildren().add(label);
//        }
//
//        allTypesLabel.setOnMouseClicked(mouseEvent -> resetLogPriorityFilter());
//    }
//
//    private Label createLogPriorityLabel(LogPriority logPriority) {
//        Label label = new Label(logPriority.getName());
//        label.getStyleClass().add("log-priority-label");
//        HBox.setMargin(label, new Insets(0, 10, 0, 10));
//
//        label.setOnMouseClicked(mouseEvent -> applyLogPriorityFilter(label, logPriority));
//
//        return label;
//    }
//
//    private void applyLogPriorityFilter(Label label, LogPriority logPriority) {
//        allTypesLabel.getStyleClass().remove("selected-label");
//        allTypesLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");
//
//        if (selectedLabel != null) {
//            selectedLabel.getStyleClass().remove("selected-label");
//            selectedLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");
//        }
//
//        label.getStyleClass().add("selected-label");
//        label.setStyle("-fx-text-fill: " + logPriority.getColor() + ";");
//
//        selectedLabel = label;
//
//        updateFilteredRecords();
//    }
//
//    private void resetLogPriorityFilter() {
//        if (selectedLabel != null) {
//            selectedLabel.getStyleClass().remove("selected-label");
//            selectedLabel.setStyle("-fx-text-fill: " + DEFAULT_INACTIVE_COLOR + ";");
//        }
//
//        allTypesLabel.getStyleClass().add("selected-label");
//        allTypesLabel.setStyle("-fx-text-fill: black;");
//
//        selectedLabel = null;
//
//        updateFilteredRecords();
//    }
//
//    private void updateFilteredRecords() {
//        String searchText = searchField.getText().toLowerCase();
//        String selectedType = selectedLabel != null ? selectedLabel.getText() : null;
//
//        List<LogRecord> allRecords = LogRecordDAO.loadRecords();
//
//        List<LogRecord> filteredRecords = allRecords.stream()
//                .filter(record -> (selectedType == null || selectedType.equals("Все") || record.getPriority().equals(selectedType)))
//                .filter(record -> record.getDate().toLowerCase().contains(searchText) ||
//                        record.getTime().toLowerCase().contains(searchText) ||
//                        record.getPriority().toLowerCase().contains(searchText) ||
//                        record.getMessage().toLowerCase().contains(searchText))
//                .collect(Collectors.toList());
//
//        logRecordTableView.updateTable(filteredRecords);
//    }
//
//    private void setActiveIcon(FontAwesomeIconView activeIcon) {
//        homeSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));
//        aboutSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));
//
//        activeIcon.setFill(Color.web(ACTIVE_SIDEBAR_ICON_COLOR));
//    }
//
//    private void startLogMonitoring() {
//        executorService.scheduleAtFixedRate(() -> {
//            try {
//                Path path = Paths.get(logPath);
//                if (!Files.exists(path)) {
//                    return;
//                }
//
//                long currentSize = Files.size(path);
//                if (currentSize > lastFileSize) {
//                    List<String> newLines = Files.lines(path)
//                            .skip(lastFileSize > 0 ? lastFileSize / 100 : 0)
//                            .toList();
//
//                    lastFileSize = currentSize;
//
//                    Platform.runLater(() -> {
//                        for (String line : newLines) {
//                            LogRecord log = LogRecord.parseLine(line);
//                            if (log != null) {
//                                logRecordTableView.getObservableList().add(log);
//                            }
//                        }
//                    });
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }, 0, READ_INTERVAL, TimeUnit.SECONDS);
//    }
//}



public class AppController implements Initializable  {

    @FXML
    private FontAwesomeIconView homeSidebarButton;

    @FXML
    private FontAwesomeIconView aboutSidebarButton;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchField;

    @FXML
    private FontAwesomeIconView searchIconButton;

    @FXML
    private MaterialIconView closeIconButton;

    @FXML
    private FontAwesomeIconView searchIcon;

    @FXML
    private GridPane sessionItemGridPane;

    private LogMonitorService logMonitorService;
    private GridPaneUpdater gridPaneUpdater;

    private boolean isSearchVisible = false;

    private final String ACTIVE_SIDEBAR_ICON_COLOR = "#fec526";
    private final String DEFAULT_SIDEBAR_ICON_COLOR = "#c1c1c1";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSidebarButtons();
        initializeSearchBar();

        gridPaneUpdater = new GridPaneUpdater(sessionItemGridPane);
        logMonitorService = new LogMonitorService(gridPaneUpdater);

        logMonitorService.loadInitialLogs();
        logMonitorService.startWatching();

        scrollPane.widthProperty().addListener((obs, oldWidth, newWidth) ->
                gridPaneUpdater.updateGridOnResize((double) newWidth)
        );
        gridPaneUpdater.updateGridOnResize(scrollPane.getPrefWidth());
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

    private void setActiveIcon(FontAwesomeIconView activeIcon) {
        homeSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));
        aboutSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));

        activeIcon.setFill(Color.web(ACTIVE_SIDEBAR_ICON_COLOR));
    }

    private void initializeSearchBar() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            closeIconButton.setVisible(!newValue.isEmpty()); // Показывать closeIconButton только если есть текст
        });

        searchField.setPrefWidth(0);
        searchField.setVisible(false);
        closeIconButton.setVisible(false);
        searchIcon.setVisible(false);

        searchIconButton.setOnMouseClicked(event -> showSearchField());
        closeIconButton.setOnMouseClicked(event -> hideSearchField());

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (searchField.getText().isEmpty()) {
                    hideSearchField();
                } else {
                    searchField.clear();
                    closeIconButton.setVisible(false);
                }
            }
        });
    }

    private void showSearchField() {
        if (!isSearchVisible) {
            isSearchVisible = true;
            searchField.setVisible(true);
            searchField.setManaged(true);
            searchField.requestFocus();

            setSearchVisibility();

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(searchField.prefWidthProperty(), 400.0, Interpolator.EASE_BOTH)
                    )
            );
            timeline.play();
        }
    }

    private void hideSearchField() {
        if (isSearchVisible) {
            isSearchVisible = false;
            searchField.clear();
            closeIconButton.setVisible(false);

            setSearchVisibility();

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(searchField.prefWidthProperty(), 0.0, Interpolator.EASE_BOTH)
                    )
            );
            timeline.setOnFinished(e -> {
                searchField.setVisible(false);
                searchField.setManaged(false);
            });
            timeline.play();
        }
    }

    private void setSearchVisibility() {
        searchField.setFocusTraversable(isSearchVisible);

        searchIconButton.setVisible(!isSearchVisible);
        searchIconButton.setManaged(!isSearchVisible);

        closeIconButton.setVisible(isSearchVisible);
        closeIconButton.setManaged(isSearchVisible);

        searchIcon.setVisible(isSearchVisible);
        searchIcon.setManaged(isSearchVisible);
    }
}
