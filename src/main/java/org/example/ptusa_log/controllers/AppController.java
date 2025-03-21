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
import org.example.ptusa_log.DAO.LogFileDAO;
import org.example.ptusa_log.services.LogFileManager;
import org.example.ptusa_log.services.LogFileMonitorService;
import org.example.ptusa_log.services.GridPaneUpdater;
import org.example.ptusa_log.utils.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSidebarButtons();
        initializeLogControls();
        loadSearchBar();
        initializeGridViewIcons();

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
            UserDialogs.showInfo(Constants.ABOUT_PROGRAM, Constants.PROGRAM_INFO);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.VIEWS_PATH + "search_bar_view.fxml"));
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

            LogFileDAO.insertOrUpdateFile(filePath);

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
}
