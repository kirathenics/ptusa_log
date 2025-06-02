package org.example.ptusa_log.controllers;

import com.dlsc.gemsfx.SVGImageView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.example.ptusa_log.DAO.SessionsDAO;
import org.example.ptusa_log.models.Session;
import org.example.ptusa_log.services.LogFileManager;
import org.example.ptusa_log.services.LogFileMonitorService;
import org.example.ptusa_log.services.GridPaneUpdater;
import org.example.ptusa_log.utils.*;
import org.example.ptusa_log.utils.enums.LogFileVisibility;
import org.example.ptusa_log.utils.enums.SortOrder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class AppController implements Initializable  {

    @FXML
    private FontAwesomeIconView homeSidebarButton;

    @FXML
    private FontAwesomeIconView archiveSidebarButton;

    @FXML
    private FontAwesomeIconView trashSidebarButton;

    @FXML
    private FontAwesomeIconView aboutSidebarButton;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox searchBarContainer;

    @FXML
    private Button addSessionButton;

    @FXML
    private SVGImageView gridViewIcon;

    @FXML
    private SVGImageView rowViewIcon;

    @FXML
    private GridPane sessionItemGridPane;

    private GridPaneUpdater gridPaneUpdater;
    private LogFileManager logFileManager;
    private LogFileMonitorService logFileMonitorService;

    private final String ACTIVE_SIDEBAR_ICON_COLOR = "#fec526";
    private final String DEFAULT_SIDEBAR_ICON_COLOR = "#c1c1c1";

    private FontAwesomeIconView activeIcon;


    private static final String SORT_PREF_KEY = "log_sort_order";
    private final Preferences preferences = Preferences.userNodeForPackage(AppController.class);

    @FXML
    private MenuButton sortMenuButton;

    @FXML
    private MenuItem sortByDefaultMenuItem;

    @FXML
    private MenuItem sortByNameDescMenuItem;

    @FXML
    private MenuItem sortByNameAscMenuItem;

    @FXML
    private MenuItem sortByTimeDescMenuItem;

    @FXML
    private MenuItem sortByTimeAscMenuItem;

    @FXML
    private SVGImageView timeDescIcon;

    @FXML
    private SVGImageView timeAscIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSidebarButtons();
        initializeLogControls();
        loadSearchBar();
        initializeGridViewIcons();
        initializeSortMenu();

        addSessionButton.setOnAction(event -> handleAddSession());
    }

    private void setupSidebarButtons() {
        homeSidebarButton.setOnMouseClicked(mouseEvent -> {
            setActiveIcon(homeSidebarButton);
            logFileManager.setFilter(logFile -> logFile.getVisibility() == LogFileVisibility.VISIBLE.getValue());
        });

        archiveSidebarButton.setOnMouseClicked(mouseEvent -> {
            setActiveIcon(archiveSidebarButton);
            logFileManager.setFilter(logFile -> logFile.getVisibility() == LogFileVisibility.ARCHIVED.getValue());
        });

        trashSidebarButton.setOnMouseClicked(mouseEvent -> {
            setActiveIcon(trashSidebarButton);
            logFileManager.setFilter(logFile -> logFile.getVisibility() == LogFileVisibility.DELETED.getValue());
        });

        aboutSidebarButton.setOnMouseClicked(mouseEvent -> {
            FontAwesomeIconView previousActiveIcon = activeIcon;
            setActiveIcon(aboutSidebarButton);
            UserDialogs.showInfo(StringConstants.ABOUT_PROGRAM, StringConstants.PROGRAM_INFO);
            setActiveIcon(previousActiveIcon);
        });

        activeIcon = homeSidebarButton;
        setActiveIcon(homeSidebarButton);
    }

    private void setActiveIcon(FontAwesomeIconView selectedIcon) {
        resetActiveIcon();
        activeIcon = selectedIcon;
        activeIcon.setFill(Color.web(ACTIVE_SIDEBAR_ICON_COLOR));
        activeIcon.getStyleClass().add("selected-item");
    }

    private void resetActiveIcon() {
        activeIcon.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));
        activeIcon.getStyleClass().remove("selected-item");
    }

    private void initializeLogControls() {
        gridPaneUpdater = new GridPaneUpdater(sessionItemGridPane);
        logFileManager = new LogFileManager(gridPaneUpdater::updateGrid);
        logFileMonitorService = new LogFileMonitorService(logFileManager::updateLogs);

        gridPaneUpdater.setLogManager(logFileManager);

        logFileManager.setFilter(logFile -> logFile.getVisibility() == LogFileVisibility.VISIBLE.getValue());

        logFileMonitorService.loadInitialLogs();
        logFileMonitorService.startWatching();

        scrollPane.widthProperty().addListener((obs, oldWidth, newWidth) ->
                gridPaneUpdater.updateGridOnResize((double) newWidth)
        );
        gridPaneUpdater.updateGridOnResize(scrollPane.getPrefWidth());
    }

    private void loadSearchBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(StringConstants.VIEWS_PATH + "search_bar_view.fxml"));
            loader.load();

            SearchBarController controller = loader.getController();
            controller.setOnSearchQueryChange(logFileManager::setSearchQuery);

            searchBarContainer.getChildren().add(controller.getRootPane());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddSession() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл сессии");

        fileChooser.setInitialDirectory(new File(SystemPaths.defineLogFilesPath()));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Лог-файлы (*.log)", "*.log");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("Выбран файл: " + filePath);

            SessionsDAO.insertOrUpdateSession(filePath);

            logFileManager.updateLogs();
        } else {
            System.out.println("Выбор файла отменён");
        }
    }

    private boolean isGridViewSelected = true;

    private void initializeGridViewIcons() {
        updateIconColors();
    }

    @FXML
    private void toggleView(MouseEvent event) {
        if (event.getSource() == gridViewIcon) {
            isGridViewSelected = true;
        } else if (event.getSource() == rowViewIcon) {
            isGridViewSelected = false;
        }

        updateIconColors();
        gridPaneUpdater.setGridViewSelected(isGridViewSelected);
    }

    private void updateIconColors() {
        gridViewIcon.setSvgUrl(SVGProcessor.getSvgUrl(isGridViewSelected ? "grid_view.svg" : "grid_view_inactive.svg"));
        rowViewIcon.setSvgUrl(SVGProcessor.getSvgUrl(!isGridViewSelected ? "row_view.svg" : "row_view_inactive.svg"));
    }

    private void initializeSortMenu() {
        MenuButtonUtils.adjustMenuButtonWidthToLongestItem(sortMenuButton);

        timeAscIcon.setSvgUrl(SVGProcessor.getSvgUrl("sort_clock_asc.svg"));
        timeDescIcon.setSvgUrl(SVGProcessor.getSvgUrl("sort_clock_desc.svg"));

        sortByDefaultMenuItem.setOnAction(e -> setSortingAndSavePreference(SortOrder.DEFAULT));

        sortByNameDescMenuItem.setOnAction(e -> setSortingAndSavePreference(SortOrder.NAME_DESC));

        sortByNameAscMenuItem.setOnAction(e -> setSortingAndSavePreference(SortOrder.NAME_ASC));

        sortByTimeDescMenuItem.setOnAction(e -> setSortingAndSavePreference(SortOrder.TIME_DESC));

        sortByTimeAscMenuItem.setOnAction(e -> setSortingAndSavePreference(SortOrder.TIME_ASC));

        SortOrder savedOrder = loadSavedSortOrder();
        setSortingAndSavePreference(savedOrder);
    }

    private void setSortingAndSavePreference(SortOrder order) {
        switch (order) {
            case DEFAULT -> {
                logFileManager.setSorting(Comparator.comparing(Session::getId));
                sortMenuButton.setText(StringConstants.SORT_DEFAULT);
            }
            case NAME_DESC -> {
                logFileManager.setSorting(Comparator.comparing(Session::getAliasName).reversed());
                sortMenuButton.setText(StringConstants.SORT_NAME_DESC);
            }
            case NAME_ASC -> {
                logFileManager.setSorting(Comparator.comparing(Session::getAliasName));
                sortMenuButton.setText(StringConstants.SORT_NAME_ASC);
            }
            case TIME_DESC -> {
                logFileManager.setSorting(Comparator.comparing(Session::getCreatedAt));
                sortMenuButton.setText(StringConstants.SORT_TIME_DESC);
            }
            case TIME_ASC -> {
                logFileManager.setSorting(Comparator.comparing(Session::getCreatedAt));
                sortMenuButton.setText(StringConstants.SORT_TIME_ASC);
            }
        }
        preferences.put(SORT_PREF_KEY, order.name());
    }

    private SortOrder loadSavedSortOrder() {
        String saved = preferences.get(SORT_PREF_KEY, SortOrder.DEFAULT.name());
        try {
            return SortOrder.valueOf(saved);
        } catch (IllegalArgumentException e) {
            return SortOrder.DEFAULT;
        }
    }
}
