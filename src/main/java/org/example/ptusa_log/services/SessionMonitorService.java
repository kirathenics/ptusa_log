package org.example.ptusa_log.services;

import org.example.ptusa_log.listeners.LogFileListener;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SessionMonitorService {
    private final String logFilePath;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile boolean running = true;
    private final LogFileListener logFileListener;

    public SessionMonitorService(String logFilePath, LogFileListener logFileListener) {
        this.logFilePath = logFilePath;
        this.logFileListener = logFileListener;
    }

    public void loadInitialLogs() {
        logFileListener.onLogsUpdated();
    }

    public void startWatching() {
        executorService.submit(this::watchFile);
    }

    public void stopWatching() {
        running = false;
        executorService.shutdownNow();
    }

    private void watchFile() {
        Path path = Paths.get(logFilePath);
        Path parentDir = path.getParent();

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            parentDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (running) {
                WatchKey key = watchService.poll(1000, TimeUnit.MILLISECONDS);
                if (key == null) continue;

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path modifiedPath = (Path) event.context();
                        if (modifiedPath.getFileName().equals(path.getFileName())) {
                            System.out.println("Файл изменён: " + logFilePath);
                            logFileListener.onLogsUpdated();
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка мониторинга файла: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

