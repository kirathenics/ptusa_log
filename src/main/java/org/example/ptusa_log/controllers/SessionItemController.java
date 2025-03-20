package org.example.ptusa_log.controllers;

import com.dlsc.gemsfx.ExpandingTextArea;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.example.ptusa_log.DAO.LogFileDAO;
import org.example.ptusa_log.listeners.LogFileListener;
import org.example.ptusa_log.models.LogFile;
import org.example.ptusa_log.utils.Constants;
import org.example.ptusa_log.utils.LogFileVisibility;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SessionItemController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ExpandingTextArea sessionTextArea;

    @FXML
    private Label deviceLabel;

    @FXML
    private MaterialIconView moreOptionsIconButton;

    private final ContextMenu contextMenu = new ContextMenu();

    private LogFile logFile;

    private LogFileListener logFileListener;

    public void setLogFile(LogFile logFile) {
        this.logFile = logFile;

        sessionTextArea.setText(logFile.getAliasName());
        sessionTextArea.setEditable(false);

        deviceLabel.setText(Constants.DEVICE + logFile.getDeviceName());

        initializeContextMenu();
    }

    public void setLogFileListener(LogFileListener logFileListener) {
        this.logFileListener = logFileListener;
    }

    public void setHoverEffect(boolean isGridViewSelected) {
        double scaleValue = isGridViewSelected ? 1.05 : 1.01;

        rootPane.setOnMouseEntered(event -> rootPane.setStyle("-fx-border-color: #0f4876; -fx-scale-x: " + scaleValue + "; -fx-scale-y: " + scaleValue + ";"));

        rootPane.setOnMouseExited(event -> rootPane.setStyle("-fx-border-color: transparent; -fx-scale-x: 1.0; -fx-scale-y: 1.0;"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        initializeContextMenu();
        setupTextArea();

        rootPane.setOnMouseClicked(this::openDetailScene);
    }

    private void initializeContextMenu() {
        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
        editIcon.setSize("1.5em");
        editIcon.setFill(Paint.valueOf("#000000"));
        MenuItem editItem = new MenuItem(Constants.EDIT_CONTEXT_MENU_ACTION, editIcon);

        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteIcon.setSize("1.5em");
        deleteIcon.setFill(Paint.valueOf("#B73F0C"));
        MenuItem deleteItem = new MenuItem(Constants.DELETE_CONTEXT_MENU_ACTION, deleteIcon);

        contextMenu.getItems().addAll(editItem, deleteItem);

        if (logFile.getVisibility() == LogFileVisibility.ARCHIVED.getValue()) {
            MaterialIconView unarchiveIcon = new MaterialIconView(MaterialIcon.UNARCHIVE);
            unarchiveIcon.setSize("1.5em");
            unarchiveIcon.setFill(Paint.valueOf("#000000"));
            MenuItem unarchiveItem = new MenuItem(Constants.UNARCHIVE_CONTEXT_MENU_ACTION, unarchiveIcon);
            contextMenu.getItems().add(unarchiveItem);

            unarchiveItem.setOnAction(event -> handleUnarchive());
        }
        else {
            MaterialIconView archiveIcon = new MaterialIconView(MaterialIcon.ARCHIVE);
            archiveIcon.setSize("1.5em");
            archiveIcon.setFill(Paint.valueOf("#000000"));
            MenuItem archiveItem = new MenuItem(Constants.ARCHIVE_CONTEXT_MENU_ACTION, archiveIcon);
            contextMenu.getItems().add(archiveItem);

            archiveItem.setOnAction(event -> handleArchive());
        }

        editItem.setOnAction(event -> handleEdit());
        deleteItem.setOnAction(event -> handleDelete());

        moreOptionsIconButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            contextMenu.show(moreOptionsIconButton, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        moreOptionsIconButton.setOnContextMenuRequested(event -> {
            contextMenu.show(moreOptionsIconButton, event.getScreenX(), event.getScreenY());
            event.consume();
        });
    }

    private void setupTextArea() {
        sessionTextArea.setOnMouseClicked(this::openDetailScene);

        Platform.runLater(() -> {
            ScrollPane sp = (ScrollPane) sessionTextArea.lookup(".scroll-pane");
            if (sp != null) {
                for (Node n : sp.getChildrenUnmodifiable()) {
                    n.setCache(false);
                }
            }
        });
    }

    private void handleEdit() {
        System.out.println("Редактирование...");

        sessionTextArea.setEditable(true);
        sessionTextArea.requestFocus();
        sessionTextArea.selectAll();

        sessionTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();

                sessionTextArea.setText(sessionTextArea.getText().replace("\n", "").trim());

                saveEdit();
            }
        });

        sessionTextArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                saveEdit();
            }
        });
    }

    private void saveEdit() {
        sessionTextArea.setEditable(false);

        String newText = sessionTextArea.getText().trim();
        LogFileDAO.setLogFileAliasName(logFile.getId(), newText);

        System.out.println("Сохранено: " + newText);

        logFileListener.onLogsUpdated();
    }

    private void handleDelete() {
        System.out.println("Удаление...");

        LogFileDAO.setLogFileVisibility(logFile.getId(), LogFileVisibility.DELETED.getValue());

        logFileListener.onLogsUpdated();
    }

    private void handleUnarchive() {
        LogFileDAO.setLogFileVisibility(logFile.getId(), LogFileVisibility.VISIBLE.getValue());

        logFileListener.onLogsUpdated();
    }

    private void handleArchive() {
        LogFileDAO.setLogFileVisibility(logFile.getId(), LogFileVisibility.ARCHIVED.getValue());

        logFileListener.onLogsUpdated();
    }

    private void openDetailScene(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.VIEWS_PATH + "log_session_view.fxml"));
            Parent detailRoot = loader.load();

            LogSessionController controller = loader.getController();
            controller.setLogFile(logFile);

            Stage stage = new Stage();
            stage.setScene(new Scene(detailRoot));
            stage.setTitle(logFile.getAliasName());
//            stage.setResizable(false);
//            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource(Constants.IMAGE_PATH + "icon-app.png")).toExternalForm()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getItemWidth() {
        FXMLLoader fxmlLoader = new FXMLLoader(SessionItemController.class.getResource(Constants.VIEWS_PATH + "session_item_view.fxml"));
        AnchorPane anchorPane;
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return anchorPane.getPrefWidth();
    }
}
