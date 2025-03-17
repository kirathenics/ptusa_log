package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class SearchBarController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private FontAwesomeIconView searchIconButton;

    @FXML
    private MaterialIconView closeIconButton;

    @FXML
    private FontAwesomeIconView searchIcon;

    private Consumer<String> onSearchQueryChange;

    private boolean isSearchVisible = false;

    public StackPane getRootPane() {
        return rootPane;
    }

    public void setOnSearchQueryChange(Consumer<String> onSearchQueryChange) {
        this.onSearchQueryChange = onSearchQueryChange;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSearchBar();
    }

    private void initializeSearchBar() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            closeIconButton.setVisible(!newValue.isEmpty());
            if (onSearchQueryChange != null) {
                onSearchQueryChange.accept(newValue.trim());
            }
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
