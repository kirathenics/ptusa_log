package org.example.ptusa_log.services;

import javafx.application.Platform;
import org.example.ptusa_log.DAO.LogFileDAO;
import org.example.ptusa_log.listeners.LogFileListener;
import org.example.ptusa_log.utils.LogFileProcessor;
import org.example.ptusa_log.utils.SystemPaths;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LogFileMonitorService {
    private static final String LOGS_PATH = SystemPaths.defineLogFilesPath();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile boolean running = true;

    private final LogFileListener logFileListener;

    public LogFileMonitorService(LogFileListener logFileListener) {
        this.logFileListener = logFileListener;
    }

    public void loadInitialLogs() {
        readLogFiles();
        logFileListener.onLogsUpdated();
    }

    public void startWatching() {
        executorService.submit(this::watchDirectory);
    }

    public void stopWatching() {
        running = false;
        executorService.shutdownNow();
    }

    private void watchDirectory() {
        Path path = Paths.get(LOGS_PATH);

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            while (running) {
                WatchKey key = watchService.poll(1000, TimeUnit.MILLISECONDS); // Не блокируем поток
//                WatchKey key = watchService.take();

                if (key == null) {
//                    Thread.sleep(1000);
                    continue;
                }

                boolean hasValidEvents = key.pollEvents().stream()
                        .anyMatch(event -> event.kind() != StandardWatchEventKinds.OVERFLOW);

                if (hasValidEvents) {
                    System.out.println("Изменения обнаружены! Обновление...");
                    readLogFiles();
                    Platform.runLater(() -> logFileListener.onLogsUpdated());
                }

                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка мониторинга логов: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void readLogFiles() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LOGS_PATH), "ptusa_*.log")) {
            for (Path entry : stream) {
                String aliasName = LogFileProcessor.extractAliasName(entry);
                String deviceName = LogFileProcessor.extractDeviceName(entry);
                LogFileDAO.addLogFile(entry.toString(), aliasName, deviceName, 0);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения логов: " + e.getMessage());
        }

        LogFileDAO.removeDeletedLogFiles();
    }
}