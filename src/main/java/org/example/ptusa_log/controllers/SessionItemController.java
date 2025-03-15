package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import org.example.ptusa_log.models.LogDoc;
import org.example.ptusa_log.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class SessionItemController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label sessionLabel;

    @FXML
    private Label deviceLabel;

    @FXML
    private MaterialIconView moreOptionsIconButton;

    private final ContextMenu contextMenu = new ContextMenu();

    private LogDoc logDoc;

    public void setData(LogDoc logDoc) {
        this.logDoc = logDoc;

        sessionLabel.setText(Constants.SESSION + logDoc.getName());
        sessionLabel.setMinHeight(Region.USE_PREF_SIZE);

        deviceLabel.setText((logDoc.getDeviceName()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeContextMenu();

        rootPane.setOnMouseClicked(this::openDetailScene);
    }

    private void initializeContextMenu() {
        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
        editIcon.setSize("1.5em");
        editIcon.setFill(Paint.valueOf("#bcbcbe"));
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
    }

    private void handleDelete() {
        System.out.println("Удаление...");
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