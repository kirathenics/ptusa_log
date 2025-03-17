package org.example.ptusa_log.controllers;

import com.dlsc.gemsfx.ExpandingTextArea;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import org.example.ptusa_log.DAO.LogFileDAO;
import org.example.ptusa_log.models.LogFile;
import org.example.ptusa_log.utils.Constants;

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

    public void setData(LogFile logFile) {
        this.logFile = logFile;

        sessionTextArea.setText(logFile.getAliasName());
        sessionTextArea.setEditable(false);

        deviceLabel.setText(Constants.DEVICE + logFile.getDeviceName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeContextMenu();

        rootPane.setOnMouseClicked(this::openDetailScene);
    }

    private void initializeContextMenu() {
        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
        editIcon.setSize("1.5em");
        editIcon.setFill(Paint.valueOf("#000000"));
        MenuItem editItem = new MenuItem(Constants.EDIT_CONTENT_MENU_ACTION, editIcon);

        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteIcon.setSize("1.5em");
        deleteIcon.setFill(Paint.valueOf("#B73F0C"));
        MenuItem deleteItem = new MenuItem(Constants.DELETE_CONTENT_MENU_ACTION, deleteIcon);

        contextMenu.getItems().addAll(editItem, deleteItem);

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

    private void handleEdit() {
        System.out.println("Редактирование...");

        sessionTextArea.setEditable(true);
        sessionTextArea.requestFocus();
        sessionTextArea.selectAll();

        sessionTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume(); // Убираем перенос строки

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
        LogFileDAO.setAliasName(logFile.getId(), newText);

        System.out.println("Сохранено: " + newText);
    }

    private void handleDelete() {
        System.out.println("Удаление...");
        LogFileDAO.setLogFileDeletion(logFile.getId(), 1);
    }

    private void openDetailScene(MouseEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.VIEW_PATH + "technic_maintenance_view.fxml"));
//            Parent detailRoot = loader.load();
//
//            TechnicMaintenanceController controller = loader.getController();
//            controller.setTechnic(technic);
//
//            Stage stage = new Stage();
//            stage.setScene(new Scene(detailRoot));
//            stage.setTitle(technic.getName());
//            stage.setResizable(false);
//            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource(Constants.IMAGE_PATH + "icon-app.png")).toExternalForm()));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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