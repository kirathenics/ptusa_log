package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.ptusa_log.DAO.LogRecordDAO;
import org.example.ptusa_log.helpers.TableViewFactory.LogRecordTableView;
import org.example.ptusa_log.utils.Constants;
import org.example.ptusa_log.utils.UserDialogs;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable  {

    @FXML
    private FontAwesomeIconView homeSidebarButton;

    @FXML
    private FontAwesomeIconView aboutSidebarButton;

    @FXML
    private VBox logTableContainer;

    private final String ACTIVE_COLOR = "#FEC526";
    private final String DEFAULT_COLOR = "#C1C1C1";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeSidebarButton.setOnMouseClicked(mouseEvent -> setActiveIcon(homeSidebarButton));

        aboutSidebarButton.setOnMouseClicked(mouseEvent -> {
            setActiveIcon(aboutSidebarButton);
            UserDialogs.showInfo(Constants.ABOUT_PROGRAM, Constants.PROGRAM_INFO);
            setActiveIcon(homeSidebarButton);
        });

        setActiveIcon(homeSidebarButton);

        LogRecordTableView logRecordTableView = new LogRecordTableView.Builder().build();
        logRecordTableView.updateTable(LogRecordDAO.loadRecords());
        logTableContainer.getChildren().add(logRecordTableView.getTableContainer());
    }

    private void setActiveIcon(FontAwesomeIconView activeIcon) {
        homeSidebarButton.setFill(Color.web(DEFAULT_COLOR));
        aboutSidebarButton.setFill(Color.web(DEFAULT_COLOR));

        activeIcon.setFill(Color.web(ACTIVE_COLOR));
    }
}