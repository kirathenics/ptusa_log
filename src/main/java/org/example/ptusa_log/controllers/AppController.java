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
import org.example.ptusa_log.utils.Constants;
import org.example.ptusa_log.utils.SVGProcessor;
import org.example.ptusa_log.utils.SystemPaths;
import org.example.ptusa_log.utils.UserDialogs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSidebarButtons();
        initializeLogControls();
        loadSearchBar();
        initializeGridViewIcons();

        addSessionButton.setOnAction(event -> handleAddSession());
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

    private void initializeLogControls() {
        gridPaneUpdater = new GridPaneUpdater(sessionItemGridPane);
        logFileManager = new LogFileManager(gridPaneUpdater::updateGrid);
        logFileMonitorService = new LogFileMonitorService(logFileManager::updateLogs);

        gridPaneUpdater.setLogManager(logFileManager);

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

    private void setActiveIcon(FontAwesomeIconView activeIcon) {
        homeSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));
        aboutSidebarButton.setFill(Color.web(DEFAULT_SIDEBAR_ICON_COLOR));

        activeIcon.setFill(Color.web(ACTIVE_SIDEBAR_ICON_COLOR));
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

        // работает
//        File newFile = new File("C:/Users/bka32/GitHub-repos/ptusa_log/src/main/resources/org/example/ptusa_log/icons/grid_view.svg");
//        gridViewIcon.setSvgUrl(newFile.toURI().toString());
    }

    @FXML
    private void toggleView(MouseEvent event) {
        if (event.getSource() == gridViewIcon) {
            isGridViewSelected = true;
        } else if (event.getSource() == rowViewIcon) {
            isGridViewSelected = false;
        }

        updateIconColors();
    }

    private void updateIconColors() {
        gridViewIcon.setSvgUrl(SVGProcessor.getSvgUrl(isGridViewSelected ? "grid_view.svg" : "grid_view_inactive.svg"));
        rowViewIcon.setSvgUrl(SVGProcessor.getSvgUrl(!isGridViewSelected ? "row_view.svg" : "row_view_inactive.svg"));
    }
}
