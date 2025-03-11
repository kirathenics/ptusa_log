package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import org.example.ptusa_log.utils.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class SessionItemController implements Initializable {

    @FXML
    private MaterialIconView moreOptionsIconButton;

    private final ContextMenu contextMenu = new ContextMenu();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeContextMenu();
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
}