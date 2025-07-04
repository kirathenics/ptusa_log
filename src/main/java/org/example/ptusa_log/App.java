package org.example.ptusa_log;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = Objects.requireNonNull(getClass().getResource("css/styles.css")).toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("ptusa_log");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("icons/app.png"))));
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(680);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}