package org.example.ptusa_log.MonitorServices;

import javafx.application.Platform;
import org.example.ptusa_log.helpers.GridPaneUpdater;
import org.example.ptusa_log.models.LogFile;
import org.example.ptusa_log.utils.LogFileProcessor;
import org.example.ptusa_log.utils.LogPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogMonitorService {
    private static final String LOGS_PATH = LogPath.defineLogPath();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final GridPaneUpdater gridPaneUpdater;

    public LogMonitorService(GridPaneUpdater gridPaneUpdater) {
        this.gridPaneUpdater = gridPaneUpdater;
    }

    public void loadInitialLogs() {
        List<LogFile> logFiles = readLogFiles();
        gridPaneUpdater.updateGrid(logFiles);
    }

    public void startWatching() {
        executorService.submit(this::watchDirectory);
    }

    private void watchDirectory() {
        Path path = Paths.get(LOGS_PATH);

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                boolean hasValidEvents = false;

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    hasValidEvents = true;
                }

                if (hasValidEvents) {
                    System.out.println("Изменения обнаружены! Обновление...");
                    List<LogFile> logFiles = readLogFiles();
                    Platform.runLater(() -> gridPaneUpdater.updateGrid(logFiles));
                }

                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<LogFile> readLogFiles() {
        List<LogFile> logDocs = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LOGS_PATH), "ptusa_*.log")) {
            for (Path entry : stream) {
                String logName = LogFileProcessor.extractLogName(entry);
                String deviceName = LogFileProcessor.extractDeviceName(entry);
                logDocs.add(new LogFile(logName, deviceName));
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