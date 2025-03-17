package org.example.ptusa_log.services;

import javafx.application.Platform;
import org.example.ptusa_log.DAO.LogFileDAO;
import org.example.ptusa_log.DAO.SQLiteDatabaseManager;
import org.example.ptusa_log.models.LogFile;
import org.example.ptusa_log.utils.LogFileProcessor;
import org.example.ptusa_log.utils.SystemPaths;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class LogMonitorService {
    private static final String LOGS_PATH = SystemPaths.defineLogFilesPath();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Consumer<List<LogFile>> onLogsChanged;
    private volatile boolean running = true;

    public LogMonitorService(Consumer<List<LogFile>> onLogsChanged) {
        this.onLogsChanged = onLogsChanged;
    }

    public void loadInitialLogs() {
//        onLogsChanged.accept(readLogFiles());
        onLogsChanged.accept(getLogFiles());
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
                WatchKey key = watchService.poll(); // Не блокируем поток
//                WatchKey key = watchService.take();

                if (key == null) {
                    Thread.sleep(1000); // Избегаем перегрузки процессора
                    continue;
                }

                boolean hasValidEvents = key.pollEvents().stream()
                        .anyMatch(event -> event.kind() != StandardWatchEventKinds.OVERFLOW);

                if (hasValidEvents) {
                    System.out.println("Изменения обнаружены! Обновление...");
//                    List<LogFile> logFiles = readLogFiles();
                    List<LogFile> logFiles = getLogFiles();
                    Platform.runLater(() -> onLogsChanged.accept(logFiles));
                }

                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка мониторинга логов: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

//    private List<LogFile> readLogFiles() {
//        List<LogFile> logFiles = new ArrayList<>();
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LOGS_PATH), "ptusa_*.log")) {
//            for (Path entry : stream) {
//                String aliasName = LogFileProcessor.extractAliasName(entry);
//                String deviceName = LogFileProcessor.extractDeviceName(entry);
//                LogFileDAO.addLogFile(entry.toString(), aliasName, deviceName, 0);
////                logFiles.add(new LogFile(logName, deviceName));
//            }
//        } catch (IOException e) {
//            System.err.println("Ошибка чтения логов: " + e.getMessage());
//        }
//        return logFiles;
//    }

    private List<LogFile> getLogFiles() {
        readLogFiles();
        List<LogFile> logFiles = LogFileDAO.getLogFiles();
        return logFiles;
    }

    private void readLogFiles() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LOGS_PATH), "ptusa_*.log")) {
            for (Path entry : stream) {
                String aliasName = LogFileProcessor.extractAliasName(entry);
                String deviceName = LogFileProcessor.extractDeviceName(entry);
                LogFileDAO.addLogFile(entry.toString(), aliasName, deviceName, 0);
//                logFiles.add(new LogFile(logName, deviceName));
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения логов: " + e.getMessage());
        }
    }
}