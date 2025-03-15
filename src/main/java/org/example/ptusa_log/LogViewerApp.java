package org.example.ptusa_log;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.ptusa_log.models.LogRecord;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// TODO: безопасность доступа к файлу

public class LogViewerApp extends Application {
    private static final int READ_INTERVAL = 1; // Интервал обновления (сек.)
    private String logPath;
    private long lastReadPosition = 0;
    private final ObservableList<LogRecord> logData = FXCollections.observableArrayList();
    private final FilteredList<LogRecord> filteredLogData = new FilteredList<>(logData, p -> true);
    private final TableView<LogRecord> logTable = new TableView<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private String deviceName = "Unknown Device"; // Имя устройства из заголовка

    @Override
    public void start(Stage primaryStage) {
        setupTableView();

        ComboBox<String> priorityFilter = new ComboBox<>();
        priorityFilter.getItems().addAll("ALL", "INFO", "WARNING", "ERROR", "DEBUG");
        priorityFilter.setValue("ALL");

        priorityFilter.setOnAction(event -> {
            String selectedPriority = priorityFilter.getValue();
            filteredLogData.setPredicate(log -> selectedPriority.equals("ALL") || log.getPriority().equals(selectedPriority));
        });

        HBox filterBox = new HBox(new Label("Фильтр по приоритету: "), priorityFilter);
        VBox root = new VBox(filterBox, logTable);
        Scene scene = new Scene(root, 800, 500);

        primaryStage.setTitle("Логи в реальном времени - " + deviceName);
        primaryStage.setScene(scene);
        primaryStage.show();

        findLatestLogFile();
        startLogMonitoring();
    }

    private void setupTableView() {
        TableColumn<LogRecord, String> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<LogRecord, String> timeColumn = new TableColumn<>("Время");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<LogRecord, String> priorityColumn = new TableColumn<>("Приоритет");
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        TableColumn<LogRecord, String> messageColumn = new TableColumn<>("Сообщение");
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageColumn.setMinWidth(400);

        logTable.getColumns().addAll(dateColumn, timeColumn, priorityColumn, messageColumn);
        logTable.setItems(filteredLogData);
    }

    private void findLatestLogFile() {
        String logDir = System.getProperty("os.name").toLowerCase().contains("win") ?
                "C:\\ProgramData\\ptusa\\logs" : "/var/log";

        try {
            Path dirPath = Paths.get(logDir);
            if (!Files.exists(dirPath)) return;

            logPath = Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().startsWith("ptusa_"))
                    .max((p1, p2) -> {
                        try {
                            return Files.getLastModifiedTime(p1).compareTo(Files.getLastModifiedTime(p2));
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .map(Path::toString)
                    .orElse(null);

            if (logPath != null) {
                readDeviceNameFromLog();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDeviceNameFromLog() {
        try {
            Path path = Paths.get(logPath);
            if (!Files.exists(path)) return;

            try (RandomAccessFile raf = new RandomAccessFile(logPath, "r")) {
                String firstLine = raf.readLine();
                if (firstLine != null && firstLine.startsWith("Device: ")) {
                    deviceName = firstLine.replace("Device: ", "");
                }
                System.out.println(deviceName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startLogMonitoring() {
        if (logPath == null) return;

        executorService.scheduleAtFixedRate(() -> {
            try (RandomAccessFile raf = new RandomAccessFile(new File(logPath), "r")) {
                raf.seek(lastReadPosition);

                String line;
                while ((line = raf.readLine()) != null) {
                    LogRecord log = parseLogLine(line);
                    if (log != null) {
                        Platform.runLater(() -> logData.add(log));
                    }
                }

                lastReadPosition = raf.getFilePointer(); // Запоминаем позицию для следующего чтения
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, READ_INTERVAL, TimeUnit.SECONDS);
    }

    private LogRecord parseLogLine(String line) {
        if (line.startsWith("Device:") || line.startsWith("---")) return null;

        String[] parts = line.split("\\|;");
        if (parts.length < 4) return null;

        return new LogRecord(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    public void stop() {
        executorService.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
