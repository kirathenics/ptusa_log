package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.function.Consumer;

public class SearchBarController {
    private final TextField searchField;
    private final FontAwesomeIconView searchIconButton;
    private final MaterialIconView closeIconButton;
    private final FontAwesomeIconView searchIcon;
    private final Consumer<String> onSearchQueryChange;

    private boolean isSearchVisible = false;

    public SearchBarController(TextField searchField, FontAwesomeIconView searchIconButton,
                               MaterialIconView closeIconButton, FontAwesomeIconView searchIcon,
                               Consumer<String> onSearchQueryChange) {
        this.searchField = searchField;
        this.searchIconButton = searchIconButton;
        this.closeIconButton = closeIconButton;
        this.searchIcon = searchIcon;
        this.onSearchQueryChange = onSearchQueryChange;

        initializeSearchBar();
    }

    private void initializeSearchBar() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            closeIconButton.setVisible(!newValue.isEmpty());
            onSearchQueryChange.accept(newValue);
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
