package org.example.ptusa_log.DAO;

import org.example.ptusa_log.helpers.GridPaneUpdater;
import org.example.ptusa_log.models.LogDoc;
import org.example.ptusa_log.utils.LogPath;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// TODO: можно ли выполнять мониторинг в отдельном потоке

//public class LogMonitorService {
//    private static final String LOGS_PATH = LogPath.defineLogPath();
//    private final GridPane sessionItemGridPane;
//    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
//
//    private final int MONITOR_SLEEP_TIME = 2000;
//    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
//
//    public LogMonitorService(GridPane gridPane) {
//        this.sessionItemGridPane = gridPane;
//    }
//
//    public void loadInitialLogs() {
//        List<LogDoc> logDocs = readLogFiles();
//        Platform.runLater(() -> updateGrid(logDocs));
//    }
//
//    public void startWatching() {
//        executorService.submit(this::watchDirectory);
//    }
//
//    private void watchDirectory() {
//        Path path = Paths.get(LOGS_PATH);
//
//        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
//            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
//
//            while (!Thread.currentThread().isInterrupted()) {
//                WatchKey key = watchService.poll(MONITOR_SLEEP_TIME, timeUnit);
//
//                if (key != null) {
//                    List<LogDoc> logDocs = readLogFiles();
//                    Platform.runLater(() -> updateGrid(logDocs));
//                    key.reset();
//                }
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<LogDoc> readLogFiles() {
//        List<LogDoc> logDocs = new ArrayList<>();
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LOGS_PATH), "ptusa_*.log")) {
//            for (Path entry : stream) {
//                logDocs.add(new LogDoc(entry.getFileName().toString(), "PLC NEXT DEMO"));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return logDocs;
//    }
//
//    private void updateGrid(List<LogDoc> logDocs) {
//        sessionItemGridPane.getChildren().clear();
//        int column = 0, row = 1;
//        int columnCount = 3;
//
//        for (LogDoc logDoc : logDocs) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.VIEW_PATH + "session_item_view.fxml"));
//                AnchorPane anchorPane = fxmlLoader.load();
//
//                SessionItemController controller = fxmlLoader.getController();
//                controller.setData(logDoc);
//
//                sessionItemGridPane.add(anchorPane, column, row);
//                GridPane.setMargin(anchorPane, new Insets(10));
//
//                column++;
//                if (column >= columnCount) {
//                    column = 0;
//                    row++;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private int lastColumnCount = -1;
//
//    public void updateGrid(double width) {
//        int columnCount = (int) Math.max(1, width / SessionItemController.getItemWidth());
//
//        if (columnCount == lastColumnCount) {
//            return;
//        }
//        lastColumnCount = columnCount;
//
//        sessionItemGridPane.getChildren().clear();
//
//        int column = 0, row = 1;
//
//        for (LogDoc logDoc : logDocs) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.VIEW_PATH + "session_item_view.fxml"));
//                AnchorPane anchorPane = fxmlLoader.load();
//
//                SessionItemController controller = fxmlLoader.getController();
//                controller.setData(logDoc);
//
//                sessionItemGridPane.add(anchorPane, column, row);
//                GridPane.setMargin(anchorPane, new Insets(10));
//
//                column++;
//                if (column >= columnCount) {
//                    column = 0;
//                    row++;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void stopWatching() {
//        executorService.shutdownNow();
//    }
//}


public class LogMonitorService {
    private static final String LOGS_PATH = LogPath.defineLogPath();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final GridPaneUpdater gridPaneUpdater;

    private static final int MONITOR_SLEEP_TIME = 2000;
    private static final TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    public LogMonitorService(GridPaneUpdater gridPaneUpdater) {
        this.gridPaneUpdater = gridPaneUpdater;
    }

    public void loadInitialLogs() {
        List<LogDoc> logDocs = readLogFiles();
        gridPaneUpdater.updateGrid(logDocs);
    }

    public void startWatching() {
        executorService.submit(this::watchDirectory);
    }

    private void watchDirectory() {
        Path path = Paths.get(LOGS_PATH);

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            while (!Thread.currentThread().isInterrupted()) {
                WatchKey key = watchService.poll(MONITOR_SLEEP_TIME, timeUnit);

                if (key != null) {
                    List<LogDoc> logDocs = readLogFiles();
                    gridPaneUpdater.updateGrid(logDocs);
                    key.reset();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<LogDoc> readLogFiles() {
        List<LogDoc> logDocs = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LOGS_PATH), "ptusa_*.log")) {
            for (Path entry : stream) {
                logDocs.add(new LogDoc(entry.getFileName().toString(), "PLC NEXT DEMO"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logDocs;
    }

    public void stopWatching() {
        executorService.shutdownNow();
    }
}