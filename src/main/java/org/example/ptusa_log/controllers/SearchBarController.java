package org.example.ptusa_log.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.example.ptusa_log.DAO.services.SearchHistoryService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class SearchBarController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private FontAwesomeIconView searchIconButton;

    @FXML
    private MaterialIconView closeIconButton;

    @FXML
    private FontAwesomeIconView searchIcon;

    @FXML
    private VBox historyBox;

    private Consumer<String> onSearchQueryChange;
    private SearchHistoryService searchHistoryService;
    private int context;

    private boolean isSearchVisible = false;

    private List<String> searchHistory = new ArrayList<>();
    private int selectedHistoryIndex = -1;

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public void setOnSearchQueryChange(Consumer<String> onSearchQueryChange) {
        this.onSearchQueryChange = onSearchQueryChange;
    }

    public void setSearchHistoryService(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    public void setContext(int context) {
        this.context = context;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSearchBar();
    }

    private void initializeSearchBar() {
        searchField.setPrefWidth(0);
        searchField.setVisible(false);
        closeIconButton.setVisible(false);
        searchIcon.setVisible(false);

        searchIconButton.setOnMouseClicked(event -> showSearchField());
        closeIconButton.setOnMouseClicked(event -> hideSearchField());

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                event.consume();

                if (!searchHistory.isEmpty()) {
                    selectedHistoryIndex = (selectedHistoryIndex + 1) % searchHistory.size();
                    highlightHistoryItem();
                }
            } else if (event.getCode() == KeyCode.UP) {
                event.consume();

                if (!searchHistory.isEmpty()) {
                    selectedHistoryIndex = (selectedHistoryIndex - 1 + searchHistory.size()) % searchHistory.size();
                    highlightHistoryItem();
                }
            }
            else if (event.getCode() == KeyCode.ENTER) {
                event.consume();

                String finalQuery = searchField.getText().trim();
                if (!finalQuery.isEmpty()) {
                    if (selectedHistoryIndex >= 0 && selectedHistoryIndex < searchHistory.size()) {
                        finalQuery = searchHistory.get(selectedHistoryIndex);
                        searchField.setText(finalQuery);
                        searchField.positionCaret(finalQuery.length());
                    }

                    searchHistoryService.saveQuery(finalQuery, context);
                    hideHistoryBox();

                    if (onSearchQueryChange != null) {
                        onSearchQueryChange.accept(finalQuery);
                    }
                }
            }
            else if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();

                if (searchField.getText().isEmpty()) {
                    hideSearchField();
                } else {
                    searchField.clear();
                    closeIconButton.setVisible(false);
                }
            }
        });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            closeIconButton.setVisible(!newVal.isEmpty());

            if (onSearchQueryChange != null) {
                onSearchQueryChange.accept(newVal.trim());
            }

            if (searchHistoryService != null) {
                if (!newVal.isEmpty()) {
                    updateSearchHistoryBox(newVal.trim());
                } else {
                    hideHistoryBox();
                }
            }
        });

        searchField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                PauseTransition pause = new PauseTransition(Duration.millis(150));
                pause.setOnFinished(e -> {
                    if (!historyBox.isHover()) { // если мышь не над historyBox
                        hideHistoryBox();
                    }
                });
                pause.play();
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

    private void updateSearchHistoryBox(String query) {
        searchHistory = searchHistoryService.getSearchHistory(query, context, 5);
        historyBox.getChildren().clear();

        if (searchHistory.isEmpty()) {
            historyBox.setVisible(false);
            historyBox.setManaged(false);
            return;
        }

        for (String historyItem : searchHistory) {
            HBox itemBox = new HBox();
            itemBox.setAlignment(Pos.CENTER_LEFT);
            itemBox.setSpacing(8);
            itemBox.getStyleClass().add("search-history-item");

            Label label = new Label(historyItem);
            label.setMaxWidth(Double.MAX_VALUE);
            label.getStyleClass().add("search-history-label");
            HBox.setHgrow(label, Priority.ALWAYS);

            MaterialIconView deleteButton = new MaterialIconView(MaterialIcon.CLOSE);
            deleteButton.setSize("1.2em");
            deleteButton.getStyleClass().add("close-icon");

            deleteButton.setOnMouseClicked(event -> {
                event.consume();

                System.out.println("Delete clicked: " + historyItem);
                searchHistoryService.deleteHistoryQuery(historyItem, context);
                updateSearchHistoryBox(query);
            });

            label.setOnMouseClicked(event -> {
                event.consume();

                System.out.println("Clicked history item: " + historyItem);

                searchField.setText(historyItem);
                searchField.requestFocus();
                searchField.positionCaret(historyItem.length());

                hideHistoryBox();

                if (onSearchQueryChange != null) {
                    onSearchQueryChange.accept(historyItem);
                }
            });

            itemBox.getChildren().addAll(label, deleteButton);

            historyBox.getChildren().add(itemBox);
        }

        historyBox.setVisible(true);
        historyBox.setManaged(true);
        selectedHistoryIndex = -1;

        System.out.println("Updated history box with " + historyBox.getChildren().size() + " items");
    }

    private void highlightHistoryItem() {
        for (int i = 0; i < historyBox.getChildren().size(); i++) {
            HBox itemBox = (HBox) historyBox.getChildren().get(i);
            itemBox.setStyle("-fx-background-color: transparent;");

            if (!itemBox.getChildren().isEmpty() && itemBox.getChildren().get(0) instanceof Label label) {
                label.setStyle("-fx-text-fill: #000000;");
            }
        }

        if (selectedHistoryIndex >= 0 && selectedHistoryIndex < historyBox.getChildren().size()) {
            HBox selectedBox = (HBox) historyBox.getChildren().get(selectedHistoryIndex);
            selectedBox.setStyle("-fx-background-color: #4083BB;");

            if (!selectedBox.getChildren().isEmpty() && selectedBox.getChildren().get(0) instanceof Label label) {
                label.setStyle("-fx-text-fill: #FFFFFF;");
            }
        }
    }

    private void hideHistoryBox() {
        historyBox.setVisible(false);
        historyBox.setManaged(false);
        selectedHistoryIndex = -1;
    }
}
