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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.example.ptusa_log.App;
import org.example.ptusa_log.DAO.SessionsDAO;
import org.example.ptusa_log.listeners.LogFileListener;
import org.example.ptusa_log.models.Session;
import org.example.ptusa_log.utils.StringConstants;
import org.example.ptusa_log.utils.enums.LogFileVisibility;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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

    private Session session;

    private LogFileListener logFileListener;

    public void setLogFile(Session session) {
        this.session = session;

        sessionTextArea.setText(session.getAliasName());
        sessionTextArea.setEditable(false);

        deviceLabel.setText(StringConstants.DEVICE + session.getDeviceName());

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
        MenuItem editItem = new MenuItem(StringConstants.EDIT_CONTEXT_MENU_ACTION, editIcon);
        editItem.getStyleClass().add("context-menu-item");

        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteIcon.setSize("1.5em");
        deleteIcon.setFill(Paint.valueOf("#B73F0C"));
        MenuItem deleteItem = new MenuItem(StringConstants.DELETE_CONTEXT_MENU_ACTION, deleteIcon);
        deleteItem.getStyleClass().add("context-menu-item");

        contextMenu.getItems().addAll(editItem, deleteItem);

        if (session.getVisibility() == LogFileVisibility.ARCHIVED.getValue()) {
            MaterialIconView unarchiveIcon = new MaterialIconView(MaterialIcon.UNARCHIVE);
            unarchiveIcon.setSize("1.5em");
            unarchiveIcon.setFill(Paint.valueOf("#000000"));
            MenuItem unarchiveItem = new MenuItem(StringConstants.UNARCHIVE_CONTEXT_MENU_ACTION, unarchiveIcon);
            unarchiveItem.getStyleClass().add("context-menu-item");

            contextMenu.getItems().add(unarchiveItem);

            unarchiveItem.setOnAction(event -> handleUnarchive());
        }
        else {
            MaterialIconView archiveIcon = new MaterialIconView(MaterialIcon.ARCHIVE);
            archiveIcon.setSize("1.5em");
            archiveIcon.setFill(Paint.valueOf("#000000"));
            MenuItem archiveItem = new MenuItem(StringConstants.ARCHIVE_CONTEXT_MENU_ACTION, archiveIcon);
            archiveItem.getStyleClass().add("context-menu-item");

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
        SessionsDAO.setSessionAliasName(session.getId(), newText);

        System.out.println("Сохранено: " + newText);

        logFileListener.onLogsUpdated();
    }

    private void handleDelete() {
        System.out.println("Удаление...");

        SessionsDAO.setSessionVisibility(session.getId(), LogFileVisibility.DELETED.getValue());

        logFileListener.onLogsUpdated();
    }

    private void handleUnarchive() {
        SessionsDAO.setSessionVisibility(session.getId(), LogFileVisibility.VISIBLE.getValue());

        logFileListener.onLogsUpdated();
    }

    private void handleArchive() {
        SessionsDAO.setSessionVisibility(session.getId(), LogFileVisibility.ARCHIVED.getValue());

        logFileListener.onLogsUpdated();
    }

    private void openDetailScene(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(StringConstants.VIEWS_PATH + "log_session_view.fxml"));
            Parent detailRoot = loader.load();

            LogSessionController controller = loader.getController();
            controller.setLogFile(session);

            Stage stage = new Stage();
            stage.setScene(new Scene(detailRoot));
            stage.setTitle(session.getAliasName());
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream( StringConstants.ICONS_PATH + "app.png"))));
//            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getItemWidth() {
        FXMLLoader fxmlLoader = new FXMLLoader(SessionItemController.class.getResource(StringConstants.VIEWS_PATH + "session_item_view.fxml"));
        AnchorPane anchorPane;
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return anchorPane.getPrefWidth();
    }
}
