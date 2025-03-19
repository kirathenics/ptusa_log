package org.example.ptusa_log.services;

import javafx.application.Platform;
import org.example.ptusa_log.models.LogRecord;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class LogFileReader {
    private final String logPath;

    private long lastReadPosition = 0;

    public LogFileReader(String logPath) {
        this.logPath = logPath;
    }

    public List<LogRecord> readNewLines() {
        List<LogRecord> logRecords = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(new File(logPath), "r")) {
            raf.seek(lastReadPosition);

            String line;
            while ((line = raf.readLine()) != null) {
                LogRecord log = LogRecord.parseLine(line);
                if (log != null) {
                    logRecords.add(log);
                }
            }

            lastReadPosition = raf.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logRecords;
    }
}
