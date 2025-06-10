package org.example.ptusa_log.services;

import javafx.application.Platform;
import org.example.ptusa_log.models.LogRecord;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class LogAlertService {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Path logFilePath;
    private final Consumer<LogRecord> onCriticalDetected;

    private volatile boolean running = true;

    public LogAlertService(Path logFilePath, Consumer<LogRecord> onCriticalDetected) {
        this.logFilePath = logFilePath;
        this.onCriticalDetected = onCriticalDetected;
    }

    public void start() {
        executor.submit(this::monitorLogFile);
    }

    public void stop() {
        running = false;
        executor.shutdownNow();
    }

    private void monitorLogFile() {
        try (RandomAccessFile reader = new RandomAccessFile(logFilePath.toFile(), "r")) {
            long filePointer = reader.length(); // начинаем с конца файла

            while (running) {
                long fileLength = logFilePath.toFile().length();
                if (fileLength < filePointer) {
                    filePointer = fileLength; // лог файл перезаписали
                }

                if (fileLength > filePointer) {
                    reader.seek(filePointer);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        LogRecord record = LogRecord.parseLine(line);
                        if (record != null && isCritical(record)) {
                            Platform.runLater(() -> onCriticalDetected.accept(record));
                        }
                    }
                    filePointer = reader.getFilePointer();
                }

                Thread.sleep(1000); // задержка между проверками
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка мониторинга критических логов: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    // TODO: create enum or smth
    private boolean isCritical(LogRecord record) {
        String p = record.getPriority().toUpperCase();
        return p.contains("ALERT") || p.contains("CRITIC") || p.contains("ERROR");
    }
}
